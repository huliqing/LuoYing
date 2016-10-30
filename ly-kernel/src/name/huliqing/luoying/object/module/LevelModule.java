/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.module;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.attribute.LevelAttribute;
import name.huliqing.luoying.object.attribute.LimitAttribute;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.attribute.ValueChangeListener;
import name.huliqing.luoying.object.effect.Effect;
import name.huliqing.luoying.object.el.LevelEl;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 可以让角色升级的模块
 * @author huliqing
 */
public class LevelModule extends AbstractModule implements ValueChangeListener<Number>{

    private static final Logger LOG = Logger.getLogger(LevelModule.class.getName());
    
    private final ElService elService = Factory.get(ElService.class);

    // 角色的等级属性名称，这个属性用于存放角色的等级值,当角色的等级变化后会存取在这个属性中。
    private String bindLevelAttribute;
    
    // 角色的xp属性名称，用于查找角色的xp属性，模块通过监听这个属性的值变来操作角色的等级。
    private String bindXpAttribute;
    
    // 到达下一个等级角色需要的经验值（xp）属性名称，模块在运行时会自动把下一等级需要的经验值计算后放在这个属性上。
    private String bindXpNextAttribute;

    // 角色等级公式,关联一个Level El, 这个公式用来计算角色在升级到每一个等级时需要的经验值(xp)数量。
    private String xpLevelElId;

    // 限制最高等级
    private int maxLevel = Integer.MAX_VALUE;
    
    // 升级时的特效
    private String effect;
    
    // ---- inner
    private NumberAttribute levelAttribute;
    private NumberAttribute xpAttribute;
    private NumberAttribute xpNextAttribute;
    private LevelEl xpLevelEl;

    @Override
    public void setData(ModuleData data) {
        super.setData(data);
        bindLevelAttribute = data.getAsString("bindLevelAttribute");
        bindXpAttribute = data.getAsString("bindXpAttribute");
        bindXpNextAttribute = data.getAsString("bindXpNextAttribute");
        xpLevelElId = data.getAsString("xpLevelEl");
        maxLevel = data.getAsInteger("maxLevel", maxLevel);
        effect = data.getAsString("effect");
    }

    @Override
    public void updateDatas() {
        // xxx updateDatas.
    }
    
    @Override
    public void initialize(Entity entity) {
        super.initialize(entity);
        // 查找角色的等级属性
        levelAttribute = entity.getAttributeManager().getAttribute(bindLevelAttribute);
        if (levelAttribute == null) {
            LOG.log(Level.WARNING, "levelAttribute not found by bindLevelAttribute={0}, actorId={1}"
                    , new Object[] {bindLevelAttribute, entity.getData().getId()});
        }
        
        // 角色的xp属性
        xpAttribute = entity.getAttributeManager().getAttribute(bindXpAttribute);
        if (xpAttribute != null) {
            xpAttribute.addListener(this);
        } else {
            LOG.log(Level.WARNING, "xpAttribute not found by bindXpAttribute={0}, actorId={1}"
                    , new Object[] {bindXpAttribute, entity.getData().getId()});
        }
        
        // 这个属性用于存放要到达下一个等级所需要的经验值
        xpNextAttribute = entity.getAttributeManager().getAttribute(bindXpNextAttribute);
        
        // 经验值计算公式。
        xpLevelEl = (LevelEl) elService.getEl(xpLevelElId);
        
        // 初始化等级
        if (levelAttribute != null) {
            setLevel(levelAttribute.intValue());
        }
    }

    @Override
    public void cleanup() {
        super.cleanup(); 
    }

    /**
     * 获取角色的等级，注：如果当前模块没有绑定角色的等级属性，则该方法将返回0.
     * @return 
     */
    public int getLevel() {
        if (levelAttribute != null) {
            return levelAttribute.intValue();
        }
        return 0;
    }
    
    /**
     * 直接设置角色的等级
     * @param newLevel 
     */
    public void setLevel(int newLevel) {
        if (levelAttribute == null) {
            return;
        }
        
        // 1.设置等级并
        levelAttribute.setValue(newLevel);
        
        // 2.升级其它等级属性,注：只有等级属性(LevelAttribute)才可以升级
        List<Attribute> attributes = entity.getAttributeManager().getAttributes();
        for (Attribute attr : attributes) {
            if (attr == levelAttribute || attr == xpAttribute) {
                continue;
            }
            if (attr instanceof LevelAttribute) {
                ((LevelAttribute) attr).setLevel(newLevel);
            }
        }
        
        // 3.重置LimitAttribute,让这些属性“涨满”如，当前生命值，魔法值
        // 注：这一步要放在“升级其它等级属性”之后处理，因为一些LimitAttribute属性可能依赖于一些等级属性的值。
        for (Attribute attr : attributes) {
            if (attr == levelAttribute || attr == xpAttribute) {
                continue;
            }
            if (attr instanceof LimitAttribute) {
                ((LimitAttribute) attr).setMax();
            }
        }
        
        // 更新下一个等级时角色需要的XP数量
        if (xpNextAttribute != null && xpLevelEl != null) {
            xpNextAttribute.setValue(xpLevelEl.getValue(newLevel + 1));
        }
    }
    
    /**
     * 获取指定等级需要的xp值, 如果模块没有配置任何等级公式，则该方法将始终返回0.
     * @param level 指定等级
     * @return 
     */
    public int getLevelXp(int level) {
        if (xpLevelEl != null) {
            return (int) xpLevelEl.getValue(level);
        }
        return 0;
    }
    
    // 当角色的xp属性的值发生变化时检查角色是否可以升级
    @Override
    public void onValueChanged(Attribute attribute, Number oldValue, Number newValue) {
        if (attribute == this.xpAttribute) {
            if (xpLevelEl != null && levelAttribute != null) {
                levelUpCheck();
            }
        }
    }
    
    // 检查并升级角色
    private void levelUpCheck() {
        // 检查升级
        // store[0] = upCount 可以升多少级
        // store[1] = needXp 需要多少XP
        int[] store = new int[] {0, 0};
        countLevelUp(xpLevelEl, levelAttribute.intValue(), xpAttribute.intValue(), maxLevel, store);
        int upCount = store[0];
        int needXp = store[1];

        // 升级角色，如果可以升级
        if (upCount > 0) {

            // 1.升级等级
            setLevel(levelAttribute.intValue() + upCount);
            
            // 2. 消耗xp, 升级其它属性。
            xpAttribute.setValue(xpAttribute.intValue() - needXp);
            
            // 3.提示升级(效果）
            if (effect != null) {
                Effect levelUpEffect = Loader.load(effect);
                levelUpEffect.setTraceEntity(entity.getEntityId());
                this.entity.getScene().addEntity(levelUpEffect);
            }
        }
    }
            
    /**
     * 检查可以升多少级和需要多少经验
     * @param xpEl 经验值el公式，用于计算每一个特定等级需要多少经验值才可以升级。
     * @param currentLevel 当前的等级
     * @param currentXp 当前可用于换算等级的经验值
     * @param store 存放结果的数组，store[2] {upCount, needXp} upCount表示可以升多少级，needXp表示需要多少xp.
     */
    private void countLevelUp(LevelEl xpEl, int currentLevel, int currentXp, int maxLevel, int[] store) {
        if (currentLevel >= maxLevel) {
            return;
        }
        long nextXp = (long) xpEl.getValue(currentLevel + 1);
        if (currentXp >= nextXp) {
            currentLevel++;
            currentXp -= nextXp;
            store[0] += 1;
            store[1] += (int) nextXp;
            countLevelUp(xpEl, currentLevel, currentXp, maxLevel, store);
        }
    }

}
