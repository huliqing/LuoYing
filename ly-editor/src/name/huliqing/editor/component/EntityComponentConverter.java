/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.component;

import name.huliqing.editor.edit.scene.JfxSceneEdit;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.Loader;

/**
 *
 * @author huliqing
 */
public class EntityComponentConverter extends AbstractComponentConverter<JfxSceneEdit> {

    @Override
    public void create(ComponentDefine cd, JfxSceneEdit jfxEdit) {
        EntityData ed = Loader.loadData(cd.getId());
        jfxEdit.addEntity(ed);
    }
    
}
