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
    private SkillService skillService;
    
    @Override
    public void inject() {
        skillService = Factory.get(SkillService.class);
    }

    // remove20161111
//    @Override
//    public void addSkin(Entity actor, String skinId, int amount) {
//        SkinModule module = actor.getModuleManager().getModule(SkinModule.class);
//        if (module != null) {
//            module.addSkin(skinId, amount);
//        }
//    }
//
//    @Override
//    public void removeSkin(Entity actor, String skinId, int amount) {
//        SkinModule module = actor.getModuleManager().getModule(SkinModule.class);
//        if (module != null) {
//            module.removeSkin(skinId, amount);
//        }
//    }
//    
//    @Override
//    public void attachSkin(Entity actor, Skin skin) {
//        SkinModule skinMoudle = actor.getModuleManager().getModule(SkinModule.class);
//        if (skinMoudle != null) {
//            skinMoudle.attachSkin(skin);
//        }
//    }
//    
//    @Override
//    public void detachSkin(Entity actor, Skin skin) {
//        SkinModule control = actor.getModuleManager().getModule(SkinModule.class);
//        if (control != null) {
//            control.detachSkin(skin);
//        }
//    }

    // remove20160927
//    @Override
//    public boolean isCanAttach(Entity actor, Skin skin) {
//        
//    }
//    
//    @Override
//    public boolean isCanTakeOnWeapon(Entity actor) {
//        return true;
//    }
//    
//    @Override
//    public boolean isCanTakeOffWeapon(Entity actor) {
//        return true;
//    }
        
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

//    @Override
//    public Skin getSkin(Entity actor, String skinId) {
//        SkinModule module = actor.getModuleManager().getModule(SkinModule.class);
//        if (module != null) {
//            return module.getSkin(skinId);
//        }
//        return null;
//    }

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
