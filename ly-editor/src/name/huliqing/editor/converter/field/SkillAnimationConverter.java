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

import com.jme3.animation.AnimControl;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.converter.Converter;
import name.huliqing.editor.converter.SimpleFieldConverter;
import name.huliqing.editor.edit.scene.SceneEdit;
import name.huliqing.editor.ui.utils.AutoCompleteComboBoxListener;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 用于转换技能的animation字段,从模型中获取所有的动画，让用户选择用哪一个动画设置到该字段中。
 * @author huliqing
 */
public class SkillAnimationConverter extends SimpleFieldConverter{

    private final ComboBox<String> animField = new ComboBox();
    private final AutoCompleteComboBoxListener acControl = new AutoCompleteComboBoxListener(animField);
    private String lastValueSaved;
    
    public SkillAnimationConverter() {
        animField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable
                , Boolean oldValue, Boolean newValue) -> {
            if (newValue) return;
            updateChangedAndSave();
        });
        animField.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateChangedAndSave();
            }
        });
        animField.getEditor().getStyleClass().add(StyleConstants.CLASS_CORNER_ROUND_LB);
    }
    
    private void updateChangedAndSave() {
        String newValue = animField.getValue();
        if (newValue != null && newValue.equals(lastValueSaved)) {
            return;
        }
        updateAttribute(newValue);
        addUndoRedo(lastValueSaved, newValue);
        lastValueSaved = newValue;
    }

    @Override
    public void initialize() {
        super.initialize();
        Jfx.runOnJme(() -> {
            EntityData ed = findEntityData(this);
            if (ed != null && (jfxEdit.getJmeEdit() instanceof SceneEdit)) {
                SceneEdit se = (SceneEdit) jfxEdit.getJmeEdit();
                Entity en = se.getScene().getEntity(ed.getUniqueId());
                if (en.getSpatial() != null) {
                    AnimControl ac = en.getSpatial().getControl(AnimControl.class);
                    if (ac != null) {
                        List<String> anims = new ArrayList(ac.getAnimationNames());
                        Jfx.runOnJfx(() -> {
                            acControl.setItems(anims);
                        });
                    }
                }
            }
        });
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
    protected void updateUI() {
        String animation = data.getAsString(field);
        animField.setValue(animation);
    }
    
    @Override
    protected Node createLayout() {
        return animField;
    }
    
}
