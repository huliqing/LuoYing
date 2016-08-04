/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view.actor;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.LY;
import name.huliqing.core.Factory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.mvc.network.UserCommandNetwork;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.ui.ListView;
import name.huliqing.core.ui.Row;
import name.huliqing.core.ui.UI;

/**
 *
 * @author huliqing
 */
public class SkillPanel extends ListView<SkillData> implements ActorPanel {
    private PlayService playService = Factory.get(PlayService.class);
    private final UserCommandNetwork userCommandNetwork = Factory.get(UserCommandNetwork.class);

    private Actor actor;
    // 最近一次获取技能列表的时间,当角色切换或者技能列表发生变化时应该重新载入
    private long lastLoadSkills;
    private final List<SkillData> datas = new ArrayList<SkillData>();
    
    public SkillPanel(float width, float height) {
        super(width, height);
    }
    
    @Override
    public List<SkillData> getDatas() {
        if (actor == null) {
            return datas;
        }
        // 当技能列表发生变化时重新获取
        if (this.lastLoadSkills < actor.getData().getSkillStore().getLastModifyTime()) {
            this.lastLoadSkills = LY.getGameTime();
            List<SkillData> temps = actor.getData().getSkillStore().getSkills();
            datas.clear();
            if (temps != null && !temps.isEmpty()) {
                for (SkillData as : temps) {
                    // 过滤掉一些不要显示的技能
                    if (!filter(as.getSkillType())) {
                        datas.add(as);
                    }
                }
            }
        }
        return datas;
    }
    
    // 是否过滤指定的技能
    private boolean filter(SkillType st) {
        switch (st) {
            case trick:
            case common:
            case dance:
            case jump:
            case sit:
            case magic:
                return false;
            default:
                return true;
        }
    }
    
    @Override
    protected Row createEmptyRow() {
        final SkillRow row = new SkillRow(this);
        row.setRowClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    userCommandNetwork.useObject(actor, row.getData());
//                    refreshPageData();// skill不会删除
                }
            }
        });
        row.setShortcutListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    ObjectData data = row.getData();
                    data.setTotal(1);
                    playService.addShortcut(actor, data);
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
        this.lastLoadSkills = 0; // 切换角色时技能列表也应该重新载入
        refreshPageData();
    }
    
    public Actor getActor() {
        return actor;
    }
}
