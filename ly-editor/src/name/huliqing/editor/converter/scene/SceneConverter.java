/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.scene;

import name.huliqing.editor.converter.AbstractDataConverter;
import name.huliqing.editor.edit.scene.JfxSceneEdit;
import name.huliqing.luoying.data.EntityData;

/**
 * @author huliqing
 */
public class SceneConverter extends AbstractDataConverter {
    
    private JfxSceneEdit editView;
    
    public void setEditView(JfxSceneEdit editView) {
        this.editView = editView; 
    }
    
    public void setSelected(EntityData entityData) {
        editView.setSelected(entityData);
    }
    
    public void addEntityData(EntityData entityData) {
        // do add...
    }
    
    public void removeEntityData(EntityData entityData) {
        // do remove...
    }

}
