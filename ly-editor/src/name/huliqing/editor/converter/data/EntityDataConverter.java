/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.data;

import name.huliqing.editor.converter.AbstractDataConverter;
import name.huliqing.editor.edit.Mode;
import name.huliqing.editor.edit.scene.JfxSceneEdit;
import name.huliqing.editor.edit.scene.JfxSceneEditListener;
import name.huliqing.editor.edit.select.EntitySelectObj;
import name.huliqing.editor.edit.select.EntitySelectObjListener;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.editor.converter.PropertyConverter;

/**
 *
 * @author huliqing
 */
public class EntityDataConverter extends AbstractDataConverter<JfxSceneEdit, EntityData> 
        implements JfxSceneEditListener<EntitySelectObj>, EntitySelectObjListener{

    private EntitySelectObj selectObj;
    
    @Override
    public void initialize(PropertyConverter parent) {
        super.initialize(parent);
        this.jfxEdit.addListener(this);
    }

    @Override
    public void cleanup() {
        if (selectObj != null) {
            selectObj.removeListener(this);
        }
        jfxEdit.removeListener(this);
        super.cleanup(); 
    }
    
    @Override
    public void notifyChangedToParent() {
        jfxEdit.reloadEntity(data);
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
    public void onEntityAdded(EntityData entity) {
        // ignore
    }

    @Override
    public void onEntityRemoved(EntityData ed) {
        // ignore
    }

    @Override
    public void onPropertyChanged(EntityData data, String property, Object value) {
        if (this.data != data) {
            return;
        }
        Jfx.runOnJfx(() -> {
            PropertyConverter pc = propertyConverters.get(property);
            if (pc != null) {
                pc.updateView(value);
            }
        });
    }
}
