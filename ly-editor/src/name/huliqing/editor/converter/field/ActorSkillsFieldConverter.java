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
package name.huliqing.editor.converter.field;

import com.jme3.math.Vector3f;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.converter.Converter;
import name.huliqing.editor.ui.utils.JfxUtils;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ActorModule;
import name.huliqing.luoying.object.module.SkillModule;
import name.huliqing.luoying.object.skill.Skill;
import name.huliqing.luoying.object.skill.WalkSkill;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 角色Actor的Skills字段转换器, 特别为工具栏添加了一个Play按钮，以便执行技能。角色的技能数据SkillDatas存放于
 * EntityData的objectData字段中，所以需要继承自EntityObjectDatasFieldConverter
 * @author huliqing
 */
public class ActorSkillsFieldConverter extends EntityObjectDatasFieldConverter {
    private static final Logger LOG = Logger.getLogger(ActorSkillsFieldConverter.class.getName());

    public ActorSkillsFieldConverter() {
        super();
        Button playAndStop = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_PLAY));
        toolbar.getItems().add(playAndStop);
        
        playAndStop.setOnAction((e) -> {
            playSkill();
        });
        listView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                playSkill();
            }
        });
    }
    
    private void playSkill() {
        ObjectData od = listView.getSelectionModel().getSelectedItem();
        if (!(od instanceof SkillData)) {
            LOG.log(Level.WARNING, "SelectItem not a SkillData! data={0}", new Object[]{od.getId()});
            return;
        }
        EntityData ed = findEntityData(this);
        if (ed == null) {
            return;
        }
        Jfx.runOnJme(() -> {
            Entity entity = jfxEdit.getJmeEdit().getScene().getEntity(ed.getUniqueId());
            ActorModule am = entity.getModule(ActorModule.class);
            SkillModule sm = entity.getModule(SkillModule.class);
            if (sm == null) {
                LOG.log(Level.WARNING, "Could not find SkillModule! entity={0}", new Object[]{entity.getData().getId()});
                return;
            }
            Skill skill = findSkill(sm, (SkillData) od);
            if (skill != null) {
                if (am != null) {
                    Vector3f viewDir = am.getViewDirection();
                    if (skill instanceof WalkSkill) {
                        WalkSkill wSkill = (WalkSkill) skill;
                        wSkill.setWalkDirection(viewDir); // 使用view方法作为walkDir
                        wSkill.setViewDirection(viewDir);
                    }
                }
                sm.playSkill(skill, false);
            }
        });
    }
    
    private Skill findSkill(SkillModule sm, SkillData sd) {
        List<Skill> skills = sm.getSkills();
        if (skills == null || skills.isEmpty())
            return null;
        
        for (Skill s : skills) {
            if (s.getData() == sd) {
                return s;
            }
        }
        return null;
    }
    
    private EntityData findEntityData(Converter c) {
        if (c.getData() instanceof EntityData) {
            return (EntityData) c.getData();
        }
        
        if (c.getParent() != null) {
            return findEntityData(c.getParent());
        }
        return null;
    }
    
    @Override
    protected Node createLayout() {
        return layout;
    }

}
