/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.data;

import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.converter.FieldConverter;
import name.huliqing.editor.edit.Mode;
import name.huliqing.editor.edit.scene.JfxSceneEdit;
import name.huliqing.editor.edit.scene.JfxSceneEditListener;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.editor.edit.controls.ControlTile;
import name.huliqing.editor.edit.controls.entity.EntityControlTileListener;

/**
 *
 * @author huliqing
 */
public class EntityDataConverter extends DataConverter<JfxSceneEdit, EntityData> 
        implements JfxSceneEditListener<ControlTile>, EntityControlTileListener{
//    private static final Logger LOG = Logger.getLogger(EntityDataConverter.class.getName());

    private EntityControlTile selectObj;
    
    @Override
    public void initialize() {
        super.initialize();
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
    public void notifyChanged() {
        jfxEdit.reloadEntity(data);
        jfxEdit.setModified(true);
        super.notifyChanged();
    }

    @Override
    public void onModeChanged(Mode mode) {
        // ignore
    }

    @Override
    public void onSelectChanged(ControlTile newSelectObj) {
        if (!(newSelectObj instanceof EntityControlTile)) {
            return;
        }
        EntityControlTile newEso = (EntityControlTile) newSelectObj;
        if (newEso.getTarget().getData() != this.data) {
            return;
        }
        if (selectObj != null) {
            selectObj.removeListener(this);
        }
        selectObj = newEso;
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
            FieldConverter pc = fieldConverters.get(property);
            if (pc != null) {
                pc.updateView();
//                LOG.log(Level.INFO, "onPropertyChanged, data={0}, property={1}, value={2}", new Object[] {data.getId(), property, value});
            }
        });
    }
}
