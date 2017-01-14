/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.entity;

import java.util.List;
import name.huliqing.editor.converter.AbstractDataConverter;
import name.huliqing.editor.converter.PropertyConverter;
import name.huliqing.editor.converter.PropertyConverterDefine;
import name.huliqing.editor.edit.Mode;
import name.huliqing.editor.edit.scene.JfxSceneEdit;
import name.huliqing.editor.edit.scene.JfxSceneEditListener;
import name.huliqing.editor.edit.select.EntitySelectObj;
import name.huliqing.editor.edit.select.EntitySelectObjListener;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.data.EntityData;

/**
 *
 * @author huliqing
 */
public class EntityDataConverter extends AbstractDataConverter<JfxSceneEdit, EntityData> 
        implements JfxSceneEditListener<EntitySelectObj>, EntitySelectObjListener{

    private EntitySelectObj selectObj;
    
    @Override
    public void initialize(JfxSceneEdit editView, EntityData data, List<PropertyConverterDefine> propertyConvertDefines, PropertyConverter parent) {
        super.initialize(editView, data, propertyConvertDefines, parent);
        this.editView.addListener(this);
    }

    @Override
    public void cleanup() {
        if (selectObj != null) {
            selectObj.removeListener(this);
        }
        editView.removeListener(this);
        super.cleanup(); 
    }
    
    @Override
    public void notifyChangedToParent() {
        editView.reloadEntity(data);
        super.notifyChangedToParent();
    }

    @Override
    public void onModeChanged(Mode mode) {
        // ignore
    }

    @Override
    public void onSelectChanged(EntitySelectObj newSelectObj) {
        if (newSelectObj == null || newSelectObj.getObject().getData() != this.data) {
            return;
        }
        if (selectObj != null) {
            selectObj.removeListener(this);
        }
        selectObj = newSelectObj;
        selectObj.addListener(this);
    }

    @Override
    public void onPropertyChanged(EntityData data, String property, Object value) {
        if (this.data != data) {
            return;
        }
        Jfx.runOnJfx(() -> {
            PropertyConverter pc = this.propertyConverters.get(property);
            if (pc != null) {
                pc.updateView(value);
            }
        });
    }
}
