/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.SkinListener;
import name.huliqing.luoying.object.module.SkinModule;

/**
 * @author huliqing
 */
public class SkinServiceImpl implements SkinService {
    
    @Override
    public void inject() {
        // ignore
    }

    @Override
    public boolean isWeaponTakeOn(Entity actor) {
        SkinModule module = actor.getModuleManager().getModule(SkinModule.class);
        return module != null && module.isWeaponTakeOn();
    }

    @Override
    public void takeOnWeapon(Entity actor) {
        SkinModule control = actor.getModuleManager().getModule(SkinModule.class);
        if (control != null) {
            control.takeOnWeapon();
        }
    }
    
    @Override
    public void takeOffWeapon(Entity actor) {
        SkinModule module = actor.getModuleManager().getModule(SkinModule.class);
        if (module != null) {
            module.takeOffWeapon();
        }
    }

    @Override
    public long getWeaponState(Entity actor) {
        SkinModule module = actor.getModuleManager().getModule(SkinModule.class);
        if (module != null) {
            return module.getWeaponState();
        }
        return -1;
    }

    @Override
    public void addSkinListener(Entity actor, SkinListener skinListener) {
        SkinModule module = actor.getModuleManager().getModule(SkinModule.class);
        if (module != null) {
            module.addSkinListener(skinListener);
        }
    }

    @Override
    public boolean removeSkinListener(Entity actor, SkinListener skinListener) {
        SkinModule module = actor.getModuleManager().getModule(SkinModule.class);
        if (module != null) {
            return module.removeSkinListener(skinListener);
        }
        return false;
    }
   
}
