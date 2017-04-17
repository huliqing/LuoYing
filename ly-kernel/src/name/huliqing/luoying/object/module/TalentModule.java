/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.object.module;

import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.TalentData;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.layer.service.MessageService;
import name.huliqing.luoying.message.EntityTalentPointAddedMessage;
import name.huliqing.luoying.message.StateCode;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.attribute.ValueChangeListener;
import name.huliqing.luoying.object.el.LNumberEl;
import name.huliqing.luoying.object.talent.Talent;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 天赋模块，这个模块可以让实体具有各种各样的天赋能力，主要处理数据为TalentData
 * @author huliqing
 */
public class TalentModule extends AbstractModule implements ValueChangeListener{
//    private static final Logger LOG = Logger.getLogger(TalentModule.class.getName());
    private final ElService elService = Factory.get(ElService.class);
    private final MessageService messageService = Factory.get(MessageService.class);
    
    // 角色等级属性的名称，用于查找角色的“等级属性”并进行绑定监听等级变化
    private String bindLevelAttribute;
    
    // 天赋属性名称，这个属性将作为天赋点数的容器，属性类型必须是Number类型。
    // 当角色升级时获得的天赋点数将累加在这个属性上。
    private String bindTalentPointsAttribute;
    
    // 天赋公式ID,如果用于为每个变化等级计算天赋点数的奖励
    private LNumberEl talentPointsLevelEl;
    
    // ---- inner
    // 天赋实例
    private final SafeArrayList<Talent> talents = new SafeArrayList<Talent>(Talent.class);
    
    // 天赋点数侦听器
    private List<TalentListener> talentListeners;
    
    // 角色的等级属性，用于监听角色等级变化
    private NumberAttribute levelAttribute;
    
    // 角色的天赋点数容器属性,用于存放角色所增加的天赋点数 
    private NumberAttribute talentPointsAttribute;
    
    // 最近一次给角色添加天赋点数时角色的等级,记住这个值用于避免重复给角色添加天赋点数。
    private int lastApplyTalentPointsLevel;

    @Override
    public void setData(ModuleData data) {
        super.setData(data); 
        bindLevelAttribute = data.getAsString("bindLevelAttribute");
        bindTalentPointsAttribute = data.getAsString("bindTalentPointsAttribute");
        talentPointsLevelEl = elService.createLNumberEl(data.getAsString("talentPointsLevelEl", "#{0}"));
        lastApplyTalentPointsLevel = data.getAsInteger("lastApplyTalentPointsLevel", 0);
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("lastApplyTalentPointsLevel", lastApplyTalentPointsLevel);
    }
    
    @Override
    public void initialize() {
        super.initialize(); 
        // 绑定并监听角色等级变化
        levelAttribute = getAttribute(bindLevelAttribute, NumberAttribute.class);
        if (levelAttribute != null) {
            levelAttribute.addListener(this);
        }
        
        // 获取天赋点数容器属性
        talentPointsAttribute = getAttribute(bindTalentPointsAttribute, NumberAttribute.class);
        
        // 初始化，载入天赋
        List<TalentData> talentDatas = entity.getData().getObjectDatas(TalentData.class, new ArrayList<TalentData>());
        for (TalentData td : talentDatas) {
            addTalent((Talent) Loader.load(td));
        }
    }
    
    @Override
    public void cleanup() {
        for (Talent t : talents.getArray()) {
            t.cleanup();
        }
        talents.clear();
        super.cleanup();
    }

    /**
     * 如果天赋已经存在则不处理
     * @param talent 
     * @return  
     */
    private boolean addTalent(Talent talent) {
        Talent oldTalent = getTalent(talent.getData().getId());
        if (oldTalent != null) {
            return false;
        }
        talents.add(talent);
        entity.getData().addObjectData(talent.getData());
        talent.setActor(entity);
        talent.initialize();
        return true;
    }
    
    /**
     * 增加角色某个天赋的点数,注：角色必须拥有足够的可用天赋点数才能增加。
     * 否则该方法将什么也不处理，当天赋的点数增加后，角色的可用天赋将会减少。
     * @param talentId 天赋ID
     * @param points 增加的点数
     */
    public void addTalentPoints(String talentId, int points) {
        if (points == 0 || talentPointsAttribute == null || talentPointsAttribute.intValue() < points)
            return;
        
        // 如果指定的天赋ID不存在则不处理
        Talent talent = getTalent(talentId);
        if (talent == null) {
            return;
        }
        
        // 处理实际增加的天赋点数
        int newLevel = talent.getLevel() + points;
        int trueAdd = points;
        if (newLevel > talent.getMaxLevel()) {
            newLevel = talent.getMaxLevel();
            trueAdd = newLevel - talent.getLevel();
        }
        
        // 更新处理器的值
        talent.setLevel(newLevel);
        
        // 减少可用的天赋点数
        talentPointsAttribute.setValue(talentPointsAttribute.intValue() - trueAdd);
        
        // 告诉侦听器
        if (talentListeners != null) {
            for (TalentListener tl : talentListeners) {
                tl.onTalentPointsChanged(entity, talent, trueAdd);
            }
        }
    }
    
    /**
     * 获取角色身上指定ID的天赋,如果角色不存在指定天赋的id，则该方法返回null.
     * @param talentId
     * @return 
     */
    public Talent getTalent(String talentId) {
        for (Talent t : talents.getArray()) {
            if (t.getData().getId().equals(talentId)) {
                return t;
            }
        }
        return null;
    }

    /**
     * 获取角色当前的所有天赋,注：返回的列表只可以只读，不可以直接修改。
     * @return 
     */
    public List<Talent> getTalents() {
        return Collections.unmodifiableList(talents);
    }
    
    /**
     * 获取角色当前可用的天赋点数
     * @return 
     */
    public int getTalentPoints() {
        if (talentPointsAttribute != null) {
            return talentPointsAttribute.intValue();
        }
        return 0;
    }
    
    /**
     * 添加天赋侦听器
     * @param talentListener 
     */
    public void addTalentListener(TalentListener talentListener) {
        if (talentListeners == null) {
            talentListeners = new ArrayList<TalentListener>();
        }
        if (!talentListeners.contains(talentListener)) {
            talentListeners.add(talentListener);
        }
    }

    /**
     * 移除指定天赋侦听器
     * @param talentListener
     * @return 
     */
    public boolean removeTalentListener(TalentListener talentListener) {
        return talentListeners != null && talentListeners.remove(talentListener);
    }

    // 监听等级的变化，并当等级变化时为角色增加天赋点数。
    @Override
    public void onValueChanged(Attribute attribute) {
        // 如果监听的属性不一致或者角色的等级没有变化，则不处理。
        // 注意：这里要以talentModule内部lastApplyTalentPointsLevel的值进行比较来判断是否需要再给角色添加天赋点数
        // 因为onValueChanged的变化是不确定的,可能变多，也可能变少。
        if (attribute != levelAttribute || levelAttribute.intValue() <= lastApplyTalentPointsLevel) {
            return;
        }
        // 计算出可得的天赋点数并加到属性上
        int pointsAdded = talentPointsLevelEl.setLevel(levelAttribute.intValue()).getValue().intValue();
        talentPointsAttribute.add(pointsAdded);
        // 记住最近一次添加点数的等级，避免重复添加，注：updateData用于把数据写回talentModule的data中去,
        // 这样角色在存档并重新读回的时候可以还原这个值。
        lastApplyTalentPointsLevel = levelAttribute.intValue();
        updateDatas();
        
        // 消息通知
        if (isMessageEnabled()) {
            String message = entity.getData().getId() + " get new talent points: " + pointsAdded;
            messageService.addMessage(new EntityTalentPointAddedMessage(StateCode.TALENT_POINTS_ADDED, message, entity, pointsAdded));
        }
    }

    @Override
    public boolean handleDataAdd(ObjectData hData, int amount) {
        if (!(hData instanceof TalentData)) 
            return false;
            
        Talent talent = getTalent(hData.getId());
        if (talent != null) {
            addEntityDataAddMessage(StateCode.DATA_ADD_FAILURE_DATA_EXISTS, hData, amount);
            return false;
        }
        addTalent((Talent) Loader.load(hData));
        // message
        addEntityDataAddMessage(StateCode.DATA_ADD, hData, amount);
        return true;
    }
    
    @Override
    public boolean handleDataRemove(ObjectData hData, int amount) {
        if (!(hData instanceof TalentData)) 
            return false;
            
        Talent talent = getTalent(hData.getId());
        if (talent == null || talent.getData() != hData || !talents.contains(talent))  {
            addEntityDataRemoveMessage(StateCode.DATA_REMOVE_FAILURE_NOT_FOUND, hData, amount);
            return false;
        }
        talents.remove(talent);
        entity.getData().removeObjectData(talent.getData());
        talent.cleanup();
        // message
        addEntityDataRemoveMessage(StateCode.DATA_REMOVE, hData, amount);
        return true;
    }

    @Override
    public boolean handleDataUse(ObjectData hData) {
        if (!(hData instanceof TalentData)) 
            return false;
            
        // 天赋不能使用
        return false;
    }


}
