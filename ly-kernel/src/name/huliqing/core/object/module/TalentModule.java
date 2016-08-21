/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import com.jme3.scene.control.Control;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.TalentData;
import name.huliqing.core.data.module.TalentModuleData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.TalentListener;
import name.huliqing.core.object.talent.Talent;

/**
 * @author huliqing
 * @param <T>
 */
public class TalentModule<T extends TalentModuleData> extends AbstractModule<T> {
    
    private Actor actor;
    private final SafeArrayList<Talent> talents = new SafeArrayList<Talent>(Talent.class);
    private List<TalentListener> talentListeners;
    
    private Control updateControl;
    
    @Override
    public void initialize(Actor actor) {
        super.initialize(actor); 
        this.actor = actor;
        
        // remove
//        // 从存档中获取需要初始化的talentDatas，如果不是存档，则从原始xml配置中获取
//        List<TalentData> talentInits = (List<TalentData>) data.getAttribute("talentDatas");
//        if (talentInits == null) {
//            String[] talentArr = data.getAsArray("talents");
//            if (talentArr != null) {
//                talentInits = new ArrayList<TalentData>(talentArr.length);
//                for (String talentId : talentArr) {
//                    talentInits.add((TalentData) DataFactory.createData(talentId));
//                }
//            }
//        }
//        
//        if (talentInits != null) {
//            for (TalentData td : talentInits) {
//                addTalent((Talent) Loader.load(td));
//            }
//        }
//        // 重新设置talentDatas
//        data.setAttribute("talentDatas", talentDatas);

        List<TalentData> talentDatas = actor.getData().getObjectDatas(TalentData.class, null);
        if (talentDatas != null) {
            for (TalentData td : talentDatas) {
                addTalent((Talent) Loader.load(td));
            }
        }
        
        updateControl = new AdapterControl() {
            @Override
            public void update(float tpf) {talentUpdate(tpf);}
        };
        this.actor.getSpatial().addControl(updateControl);
    }
    
    private void talentUpdate(float tpf) {
        for (Talent t : talents.getArray()) {
            t.update(tpf);
        }
    }
    
    @Override
    public void cleanup() {
        for (Talent t : talents.getArray()) {
            t.cleanup();
        }
        talents.clear();
        if (updateControl != null) {
            actor.getSpatial().removeControl(updateControl);
        }
        super.cleanup();
    }

    public void addTalent(Talent talent) {
        // 判断天赋是否已经存在
        if (existsTalent(talent.getData().getId()))
            return;
        
        talents.add(talent);
        actor.getData().addObjectData(talent.getData());
        
        talent.setActor(actor);
        talent.initialize();
        
        if (talentListeners != null) {
            for (TalentListener listener : talentListeners) {
                listener.onTalentAdded(actor, talent.getData());
            }
        }
    }
    
    /**
     * 增加角色某个天赋的点数,注：角色必须拥有足够的可用天赋点数才能增加。
     * 否则该方法将什么也不处理，当天赋的点数增加后，角色的可用天赋将会减少。
     * @param talentId 天赋ID
     * @param points 增加的点数，必须是正数
     */
    public void addTalentPoints(String talentId, int points) {
        // 天赋点必须大于0，并且角色必须有足够的天赋点可用
        if (points <= 0 || actor.getData().getTalentPoints() < points)
            return;
        
        // 如果指定的天赋ID不存在则不处理
        Talent talent = getTalent(talentId);
        if (talent == null) {
            return;
        }
        
        // 如果天赋点数已经满，则不再处理
        TalentData talentData = talent.getData();
        if (talentData.isMax())
            return;
        
        // 处理实际增加的天赋点数
        int newLevel = talentData.getLevel() + points;
        int trueAdd = points;
        if (newLevel > talentData.getMaxLevel()) {
            newLevel = talentData.getMaxLevel();
            trueAdd = newLevel - talentData.getLevel();
        }
        talentData.setLevel(newLevel);
        
        // 更新处理器的值
        talent.updateLevel(talentData.getLevel());
        
        // 减少可用的天赋点数
        actor.getData().setTalentPoints(actor.getData().getTalentPoints() - trueAdd);
        
        // 告诉侦听器
        if (talentListeners != null) {
            for (TalentListener tl : talentListeners) {
                tl.onTalentPointsChange(actor, talentData, trueAdd);
            }
        }
    }
    
    public boolean removeTalent(Talent talent) {
        if (!talents.contains(talent)) 
            return false;
        
        talents.remove(talent);
        actor.getData().removeObjectData(talent.getData());
        talent.cleanup();
        
        if (talentListeners != null) {
            for (TalentListener tl : talentListeners) {
                tl.onTalentRemoved(actor, talent.getData());
            }
        }
        return true;
    }
    
    public Talent getTalent(String talentId) {
        for (Talent t : talents.getArray()) {
            if (t.getData().getId().equals(talentId)) {
                return t;
            }
        }
        return null;
    }

    public List<Talent> getTalents() {
        return talents;
    }
    
    public void addTalentListener(TalentListener talentListener) {
        if (talentListeners == null) {
            talentListeners = new ArrayList<TalentListener>();
        }
        if (!talentListeners.contains(talentListener)) {
            talentListeners.add(talentListener);
        }
    }

    public boolean removeTalentListener(TalentListener talentListener) {
        return talentListeners != null && talentListeners.remove(talentListener);
    }
        
    // remove20160821
//    /**
//     * 设置可用的天赋点数,天赋点数可用到角色的天赋上
//     * @param talentPoints 
//     */
//    public void setTalentPoints(int talentPoints) {
//        data.setTalentPoints(talentPoints);
//    }
//    
//    /**
//     * 获取角色当前可用的所有天赋点数量.
//     * @return 
//     */
//    public int getTalentPoints() {
//        return data.getTalentPoints();
//    }
//    
//    /**
//     * 天赋点数的奖励公式
//     * @return 
//     */
//    public String getTalentPointsLevelEl() {
//        return data.getTalentPointsLevelEl();
//    }
//
//    /**
//     * 天赋点数的奖励公式
//     * @param talentPointsLevelEl 
//     */
//    public void setTalentPointsLevelEl(String talentPointsLevelEl) {
//        data.setTalentPointsLevelEl(talentPointsLevelEl);
//    }
    
    /**
     * 根据天赋id判断天赋是否已经存在，天赋不能重复，只要ID相同就视为已经存在。
     * @param talentId
     * @return 
     */
    private boolean existsTalent(String talentId) {
        for (Talent t : talents) {
            if (t.getData().getId().equals(talentId)) {
                return true;
            }
        }
        return false;
    }


}
