/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.view.actor;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.Factory;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.layer.network.ActorNetwork;
import name.huliqing.ly.layer.network.SkillNetwork;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.layer.service.SkillService;
import name.huliqing.ly.object.skill.Skill;
import name.huliqing.ly.ui.ListView;
import name.huliqing.ly.ui.Row;
import name.huliqing.ly.ui.UI;

/**
 *
 * @author huliqing
 */
public class SkillPanel extends ListView<Skill> implements ActorPanel {
    private final PlayService playService = Factory.get(PlayService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);

    private Actor actor;
    // 最近一次获取技能列表的时间,当角色切换或者技能列表发生变化时应该重新载入
    private final List<Skill> datas = new ArrayList<Skill>();
    
    public SkillPanel(float width, float height) {
        super(width, height);
    }
    
    @Override
    public List<Skill> getDatas() {
        if (actor == null) {
            return datas;
        }
        datas.clear();
        datas.addAll(skillService.getSkills(actor));
        return datas;
    }

    @Override
    protected boolean filter(Skill data) {
        return false;
    }
    
    @Override
    protected Row createEmptyRow() {
        final SkillRow row = new SkillRow(this);
        row.setRowClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    
                    // 一些技能在执行前必须设置目标对象。
                    actorNetwork.setTarget(actor, playService.getTarget());
            
                    // 执行技能
                    skillNetwork.playSkill(actor, row.getData(), false);
                    
//                    refreshPageData();// skill不会删除
                }
            }
        });
        row.setShortcutListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    playService.addShortcut(actor, row.getData().getData());
                }
            }
        });
        return row;
    }

    @Override
    public void setPanelVisible(boolean visible) {
        this.setVisible(visible);
    }

    @Override
    public void setPanelUpdate(Actor actor) {
        this.actor = actor;
        refreshPageData();
    }
    
    public Actor getActor() {
        return actor;
    }
}
