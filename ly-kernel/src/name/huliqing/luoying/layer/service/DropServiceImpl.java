/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.data.DropData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.object.drop.Drop;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.DropModule;

/**
 *
 * @author huliqing
 */
public class DropServiceImpl implements DropService {
    
    @Override
    public void inject() {
        // ignore
    }

    @Override
    public DropData createDrop(String objectId) {
        return DataFactory.createData(objectId);
    }

    @Override
    public void addDrop(Entity actor, String dropId) {
        DropModule module = actor.getEntityModule().getModule(DropModule.class);
        if (module != null) {
            module.addDrop((Drop)Loader.load(dropId));
        }
    }

    @Override
    public void doDrop(Entity source, Entity target) {
        DropModule module = source.getEntityModule().getModule(DropModule.class);
        if (module != null) {
            module.doDrop(target);
        }
    }
    
    
}
