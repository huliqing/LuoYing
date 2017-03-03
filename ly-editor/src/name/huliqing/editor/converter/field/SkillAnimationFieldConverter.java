/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class SkillAnimationFieldConverter extends SimpleFieldConverter{

    private final ComboBox<String> animField = new ComboBox();
    private final AutoCompleteComboBoxListener acControl = new AutoCompleteComboBoxListener(animField);
    private String lastValueSaved;
    
    public SkillAnimationFieldConverter() {
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
