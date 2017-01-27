/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.components;

import name.huliqing.editor.edit.scene.JfxSceneEdit;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.Loader;

/**
 *
 * @author huliqing
 */
public class EntityComponent extends BaseComponent<JfxSceneEdit> {

    public EntityComponent(String id, String name) {
        super(id, name);
    }
    
    public EntityComponent(String id, String name, String icon) {
        super(id, name, icon);
    }
    
    @Override
    public void create(JfxSceneEdit jfxEdit) {
        EntityData ed = Loader.loadData(id);
        jfxEdit.addEntity(ed);
    }
    
}
