/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import java.util.List;
import name.huliqing.ly.Factory;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.object.module.SkinListener;
import name.huliqing.ly.object.module.SkinModule;
import name.huliqing.ly.object.skin.Skin;

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

    @Override
    public void addSkin(Entity actor, String skinId, int amount) {
        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            module.addSkin(skinId, amount);
        }
    }

    @Override
    public void removeSkin(Entity actor, String skinId, int amount) {
        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            module.removeSkin(skinId, amount);
        }
    }
    
    @Override
    public void attachSkin(Entity actor, Skin skin) {
//        // 为了防止与takeOnWeapon/takeOffWeapon在异步上的冲突，这里必须限制在取
//        // 摘武器的时候换装备
//        if (skillService.isPlayingSkill(actor, SkillType.skin)) {
//            return;
//        }

        SkinModule control = actor.getModule(SkinModule.class);
        if (control != null) {
            control.attachSkin(skin);
        }
    }
    
    @Override
    public void detachSkin(Entity actor, Skin skin) {
//        // 为了防止与takeOnWeapon/takeOffWeapon在异步上的冲突，这里必须限制在取
//        // 摘武器的时候换装备
//        if (skillService.isPlayingSkill(actor, SkillType.skin)) {
//            return;
//        }

        SkinModule control = actor.getModule(SkinModule.class);
        if (control != null) {
            control.detachSkin(skin);
        }
    }

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
        SkinModule module = actor.getModule(SkinModule.class);
        return module != null && module.isWeaponTakeOn();
    }

    @Override
    public void takeOnWeapon(Entity actor) {
        SkinModule control = actor.getModule(SkinModule.class);
        if (control != null) {
            control.takeOnWeapon();
        }
    }
    
    @Override
    public void takeOffWeapon(Entity actor) {
        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            module.takeOffWeapon();
        }
    }

    @Override
    public Skin getSkin(Entity actor, String skinId) {
        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            return module.getSkin(skinId);
        }
        return null;
    }

    /**
     * 获取角色的所有皮肤,返回的列表只能只读，如果角色没有皮肤则返回empty.
     * @param actor
     * @return 
     */
    @Override
    public List<Skin> getSkins(Entity actor) {
        SkinModule module = actor.getModule(SkinModule.class);
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
        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            return module.getUsingSkins();
        }
        return null;
    }
    
    @Override
    public long getWeaponState(Entity actor) {
        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            return module.getWeaponState();
        }
        return -1;
    }

    @Override
    public void addSkinListener(Entity actor, SkinListener skinListener) {
        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            module.addSkinListener(skinListener);
        }
    }

    @Override
    public boolean removeSkinListener(Entity actor, SkinListener skinListener) {
        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            return module.removeSkinListener(skinListener);
        }
        return false;
    }
   
}
