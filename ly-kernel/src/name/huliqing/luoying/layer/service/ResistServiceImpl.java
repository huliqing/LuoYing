/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ResistModule;

/**
 *
 * @author huliqing
 */
public class ResistServiceImpl implements ResistService {

    @Override
    public void inject() {
    }
    
    @Override
    public float getResist(Entity entity, String state) {
        ResistModule module = entity.getModuleManager().getModule(ResistModule.class);
        return module != null ? module.getResist(state) : 0;
    }
    
}
