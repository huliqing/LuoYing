/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.SkinListener;
import name.huliqing.luoying.object.module.SkinModule;
import name.huliqing.luoying.object.skin.Skin;

/**
 *
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

    /**
     * 获取角色的所有皮肤,返回的列表只能只读，如果角色没有皮肤则返回empty.
     * @param actor
     * @return 
     */
    @Override
    public List<Skin> getSkins(Entity actor) {
        SkinModule module = actor.getModuleManager().getModule(SkinModule.class);
        if (module != null) {
            return module.getSkins();
        }
        return null;
    }

    /**
     * 获取当前正在使用中的皮肤，返回的列表只能只读，如果没有则返回空列表.
     * @param actor
     * @return 
     */
    @Override
    public List<Skin> getUsingSkins(Entity actor) {
        SkinModule module = actor.getModuleManager().getModule(SkinModule.class);
        if (module != null) {
            return module.getUsingSkins();
        }
        return null;
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
