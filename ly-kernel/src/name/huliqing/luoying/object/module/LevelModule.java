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
import name.huliqing.luoying.object.attribute.BooleanAttribute;
import name.huliqing.luoying.object.attribute.LevelAttribute;
import name.huliqing.luoying.object.attribute.LimitAttribute;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.attribute.ValueChangeListener;
import name.huliqing.luoying.object.effect.Effect;
import name.huliqing.luoying.object.el.LNumberEl;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 可以让角色升级的模块,这个模块的作用是监听角色的经验值属性，并在角色的经验值达到一定程度时改变角色的等级值。
 * @author huliqing
 */
public class LevelModule extends AbstractModule implements ValueChangeListener{

    private static final Logger LOG = Logger.getLogger(LevelModule.class.getName());
    
    private final ElService elService = Factory.get(ElService.class);

    // 角色等级公式,关联一个Level El, 这个公式用来计算角色在升级到每一个等级时需要的经验值(xp)数量。
    private LNumberEl xpLevelEl;

    // 限制最高等级
    private int maxLevel = Integer.MAX_VALUE;
    
    // 升级时的特效
    private String effect;

    // 绑定角色的等级属性，这个属性用于存放角色的等级值,当角色的等级变化后会存取在这个属性中。
    private String bindLevelAttribute;
    // 绑定角色的经验值属性
    private String bindXpAttribute;
    // 到达下一个等级角色需要的经验值（xp）属性名称，模块在运行时会自动把下一等级需要的经验值计算后放在这个属性上。
    private String bindXpNextAttribute;
    // 绑定角色的一个Boolean属性，该属性用于控制是否允许角色在游戏过程中动态升级，即通过xp的累积来升级角色等级，
    // 角色初始化、或者直接设置角色的等级属性则不受这个参数的影响。
    private String bindLevelUpEnabledAttribute;
    
    // ---- inner
    private NumberAttribute levelAttribute;
    private NumberAttribute xpAttribute;
    private NumberAttribute xpNextAttribute;
    private BooleanAttribute levelUpEnabledAttribute;

    @Override
    public void setData(ModuleData data) {
        super.setData(data);
        xpLevelEl = elService.createLNumberEl(data.getAsString("xpLevelEl", "#{100}")); // #{100}作为在不设置时的默认值
        maxLevel = data.getAsInteger("maxLevel", maxLevel);
        effect = data.getAsString("effect");
        bindLevelAttribute = data.getAsString("bindLevelAttribute");
        bindXpAttribute = data.getAsString("bindXpAttribute");
        bindXpNextAttribute = data.getAsString("bindXpNextAttribute");
        bindLevelUpEnabledAttribute = data.getAsString("bindLevelUpEnabledAttribute");
    }

    @Override
    public void updateDatas() {
        // xxx updateDatas.
    }
    
    private <T extends Attribute> T getAttribute(String attributeName, Class<T> type) {
        if (attributeName == null) {
            return null;
        }
        T attribute = entity.getAttributeManager().getAttribute(attributeName, type);
        if (attribute == null) {
            LOG.log(Level.WARNING, "Attribute not found by attributeName={0}, attributeType={1}, entity={2}"
                    , new Object[] {attributeName, type.getName(), entity.getData().getId()});
        }
        return attribute;
    }
    
    @Override
    public void initialize(Entity entity) {
        super.initialize(entity);
        // 角色的等级属性
        levelAttribute = getAttribute(bindLevelAttribute, NumberAttribute.class);
        
        // 角色的xp属性
        xpAttribute = getAttribute(bindXpAttribute, NumberAttribute.class);
        
        // 这个属性用于存放要到达下一个等级所需要的经验值
        xpNextAttribute = entity.getAttributeManager().getAttribute(bindXpNextAttribute);
        
        // 等级功能开关属性
        levelUpEnabledAttribute = getAttribute(bindLevelUpEnabledAttribute, BooleanAttribute.class);
        
        if (xpAttribute != null) {
            xpAttribute.addListener(this);
        }
        // 初始化等级
        if (levelAttribute != null) {
            setLevel(levelAttribute.intValue());
            levelAttribute.addListener(this);
        }
    }

    @Override
    public void cleanup() {
        if (xpAttribute != null) {
            xpAttribute.removeListener(this);
        }
        if (levelAttribute != null) {
            levelAttribute.removeListener(this);
        }
        super.cleanup(); 
    }

    // 1.当经验值属性发生变化时检查角色是否可以升级.
    // 2.当等级值直接发生变化时，改变其它属性值。
    @Override
    public void onValueChanged(Attribute attribute) {
        if (attribute == xpAttribute) {
            if (xpLevelEl != null && levelAttribute != null && isLevelUpEnabled()) {
                levelUpCheck();
            }
            return;
        }
        if (attribute == levelAttribute) {
            setLevel(levelAttribute.getValue().intValue());
        }
    }
    
    /**
     * 直接设置角色的等级
     * @param newLevel 
     */
    private void setLevel(int newLevel) {
        if (levelAttribute == null) {
            return;
        }
        
        // 确保等级一致。
        if (levelAttribute.intValue() != newLevel) {
            levelAttribute.setValue(newLevel);
        }
        
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
            xpNextAttribute.setValue(xpLevelEl.setLevel(newLevel + 1).getValue());
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
     * 检查可以升多少级和需要多少经验
     * @param xpEl 经验值el公式，用于计算每一个特定等级需要多少经验值才可以升级。
     * @param currentLevel 当前的等级
     * @param currentXp 当前可用于换算等级的经验值
     * @param store 存放结果的数组，store[2] {upCount, needXp} upCount表示可以升多少级，needXp表示需要多少xp.
     */
    private void countLevelUp(LNumberEl xpEl, int currentLevel, int currentXp, int maxLevel, int[] store) {
        if (currentLevel >= maxLevel) {
            return;
        }
        int nextXp = xpEl.setLevel(currentLevel + 1).getValue().intValue();
        if (currentXp >= nextXp) {
            currentLevel++;
            currentXp -= nextXp;
            store[0] += 1;
            store[1] += nextXp;
            countLevelUp(xpEl, currentLevel, currentXp, maxLevel, store);
        }
    }

    /**
     * 判断等级功能是否打开。
     * @return 
     */
    private boolean isLevelUpEnabled() {
        return levelUpEnabledAttribute == null || levelUpEnabledAttribute.getValue();
    }
}
