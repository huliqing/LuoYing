/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import name.huliqing.ly.data.DropData;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.xml.DataFactory;
import name.huliqing.ly.object.drop.Drop;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.object.module.DropModule;

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
        DropModule module = actor.getModule(DropModule.class);
        if (module != null) {
            module.addDrop((Drop)Loader.load(dropId));
        }
    }

    @Override
    public void doDrop(Entity source, Entity target) {
        DropModule module = source.getModule(DropModule.class);
        if (module != null) {
            module.doDrop(target);
        }
    }
    
    
}
