/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import name.huliqing.core.data.TalentData;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.TalentListener;
import name.huliqing.core.object.talent.Talent;
import name.huliqing.core.object.talent.TalentProcessor;

/**
 *
 * @author huliqing
 */
public class TalentServiceImpl implements TalentService {

    @Override
    public void inject() {
        // 
    }
    
    @Override
    public void addTalent(Actor actor, String talentId) {
        TalentData data = DataFactory.createData(talentId);
        addTalent(actor, data);
    }

    @Override
    public void addTalent(Actor actor, TalentData talentData) {
        List<TalentData> datas = actor.getData().getTalents();
        if (datas == null) {
            datas = new ArrayList<TalentData>();
            actor.getData().setTalents((ArrayList) datas);
        }
        for (TalentData data : datas) {
            if (data.getId().equals(talentData.getId())) {
                // 天赋已经存在
                return;
            }
        }
        datas.add(talentData);
        TalentProcessor tp = actor.getTalentProcessor();
        tp.addTalent(Loader.loadTalent(talentData));
        
        // listener
        List<TalentListener> tls = actor.getTalentListeners();
        if (tls != null) {
            for (TalentListener tl : tls) {
                tl.onTalentAdded(actor, talentData);
            }
        }
    }

    @Override
    public void removeTalent(Actor actor, String talentId) {
        List<TalentData> datas = actor.getData().getTalents();
        if (datas == null) {
            return;
        }
        // 从data中移除
        Iterator<TalentData> it = datas.iterator();
        while (it.hasNext()) {
            if (it.next().getId().equals(talentId)) {
                it.remove();
            }
        }
        // 从processor中移除
        TalentProcessor tp = actor.getTalentProcessor();
        tp.removeTalent(talentId);
    }

    @Override
    public List<TalentData> getTalents(Actor actor) {
        return actor.getData().getTalents();
    }

    @Override
    public void addTalentPoints(Actor actor, String talentId, int points) {
        List<TalentData> datas = actor.getData().getTalents();
        // 天赋点必须大于0，并且角色必须有足够的天赋点可用
        if (datas == null || points <= 0 || actor.getData().getTalentPoints() < points)
            return;
        
        // 查找指定ID的天赋,如果不存在或达到指定天赋级别则不再允许增加
        TalentData target = null;
        for (TalentData data : datas) {
            if (data.getId().equals(talentId)) {
                target = data;
                break;
            }
        }
        if (target == null || target.isMax())
            return;
        
        // 处理实际增加的天赋点数
        int newLevel = target.getLevel() + points;
        int trueAdd = points;
        if (newLevel > target.getMaxLevel()) {
            newLevel = target.getMaxLevel();
            trueAdd = newLevel - target.getLevel();
        }
        target.setLevel(newLevel);
        
        // 更新处理器的值
        TalentProcessor tp = actor.getTalentProcessor();
        Talent talent = tp.getTalent(talentId);
        talent.updateLevel(target.getLevel());
        
        // 减少可用的天赋点数
        actor.getData().setTalentPoints(actor.getData().getTalentPoints() - trueAdd);
        
        // 告诉侦听器
        List<TalentListener> tls = actor.getTalentListeners();
        if (tls != null) {
            for (TalentListener tl : tls) {
                tl.onTalentPointsChange(actor, talentId, trueAdd);
            }
        }
    }

    
}
