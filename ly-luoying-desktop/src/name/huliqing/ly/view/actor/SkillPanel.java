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
package name.huliqing.ly.view.actor;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.ActorNetwork;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.network.SkillNetwork;
import name.huliqing.luoying.layer.service.DefineService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.skill.Skill;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.UI;
import name.huliqing.ly.constants.SkillConstants;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 * 技能面板，用于显示角色的技能列表。
 * @author huliqing
 */
public class SkillPanel extends ListView<Skill> implements ActorPanel {
    private final SkillService skillService = Factory.get(SkillService.class);
    private final DefineService defineService = Factory.get(DefineService.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final GameService gameService = Factory.get(GameService.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);

    private Entity actor;
    private final List<Skill> datas = new ArrayList<Skill>();
    private final long filterSkillTypes;
    
    public SkillPanel(float width, float height) {
        super(width, height);
        filterSkillTypes = defineService.getSkillTypeDefine().convert(
                SkillConstants.SKILL_TYPE_WAIT
                , SkillConstants.SKILL_TYPE_WALK
                , SkillConstants.SKILL_TYPE_RUN
                , SkillConstants.SKILL_TYPE_HURT
                , SkillConstants.SKILL_TYPE_DEAD
                , SkillConstants.SKILL_TYPE_RESET
                , SkillConstants.SKILL_TYPE_SKIN
                , SkillConstants.SKILL_TYPE_DEFEND
                , SkillConstants.SKILL_TYPE_DUCK
                , SkillConstants.SKILL_TYPE_ATTACK
        );
    }
    
    @Override
    public List<Skill> getDatas() {
        if (actor == null || !actor.isInitialized()) {
            return datas;
        }
        datas.clear();
        
        List<Skill> allSkills = skillService.getSkills(actor);
        if (allSkills != null && !allSkills.isEmpty()) {
            for (Skill s : allSkills) {
                if ((s.getTypes() & filterSkillTypes) != 0) {
                    continue;
                }
                datas.add(s);
            }
        }
        
        return datas;
    }

    @Override
    protected boolean filter(Skill data) {
        return false;
    }
    
    @Override
    protected Row createEmptyRow() {
        final SkillRow row = new SkillRow();
        row.setRowClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    
                    // 一些技能在执行前必须设置目标对象。
                    // 注意：gameService.getTarget()是获取当前游戏主目标，是“玩家行为”，不能把它
                    // 放到skillNetwork.playSkill中去。
                    Entity target = gameService.getTarget();
                    if (target != null) {
                        gameNetwork.setTarget(actor, target.getEntityId());
                    }
                    
                    entityNetwork.useObjectData(actor, row.getData().getData().getUniqueId());
                    
                    // skill不会删除,所以不需要刷新
//                    refreshPageData();
                }
            }
        });
        row.setShortcutListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    gameService.addShortcut(actor, row.getData().getData());
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
    public void setPanelUpdate(Entity actor) {
        this.actor = actor;
        refreshPageData();
    }
    
    public Entity getActor() {
        return actor;
    }
}
