/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.SkinListener;
import name.huliqing.core.object.module.SkinModule;
import name.huliqing.core.object.skin.Skin;

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
    public void addSkin(Actor actor, String skinId, int amount) {
        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            module.addSkin(skinId, amount);
        }
    }

    @Override
    public void removeSkin(Actor actor, String skinId, int amount) {
        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            module.removeSkin(skinId, amount);
        }
    }
    
    @Override
    public void attachSkin(Actor actor, Skin skin) {
        // 为了防止与takeOnWeapon/takeOffWeapon在异步上的冲突，这里必须限制在取
        // 摘武器的时候换装备
        if (skillService.isPlayingSkill(actor, SkillType.skin)) {
            return;
        }

        SkinModule control = actor.getModule(SkinModule.class);
        if (control != null) {
            control.attachSkin(skin);
        }
    }
    
    @Override
    public void detachSkin(Actor actor, Skin skin) {
        // 为了防止与takeOnWeapon/takeOffWeapon在异步上的冲突，这里必须限制在取
        // 摘武器的时候换装备
        if (skillService.isPlayingSkill(actor, SkillType.skin)) {
            return;
        }

        SkinModule control = actor.getModule(SkinModule.class);
        if (control != null) {
            control.detachSkin(skin);
        }
    }

    @Override
    public boolean isCanTakeOnWeapon(Actor actor) {
        // 如果角色当前正在执行换装技能，则直接返回。因为换装技能可能是异步的。
        // 在换装完成之前不能再执行换装，否则可能造成多把武器穿在身上的BUG。
        return !skillService.isPlayingSkill(actor, SkillType.skin);
    }

    @Override
    public boolean isCanTakeOffWeapon(Actor actor) {
        return !skillService.isPlayingSkill(actor, SkillType.skin);
    }
        
    @Override
    public boolean isWeaponTakeOn(Actor actor) {
        SkinModule module = actor.getModule(SkinModule.class);
        return module != null && module.isWeaponTakeOn();
    }

    @Override
    public void takeOnWeapon(Actor actor, boolean force) {
        if (!force && !isCanTakeOnWeapon(actor)) {
            return;
        }
        SkinModule control = actor.getModule(SkinModule.class);
        if (control != null) {
            control.takeOnWeapon();
        }
    }
    
    @Override
    public void takeOffWeapon(Actor actor, boolean force) {
        if (!force && !isCanTakeOffWeapon(actor)) {
            return;
        }

        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            module.takeOffWeapon();
        }
    }

    @Override
    public Skin getSkin(Actor actor, String skinId) {
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
    public List<Skin> getSkins(Actor actor) {
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
    public List<Skin> getUsingSkins(Actor actor) {
        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            return module.getUsingSkins();
        }
        return null;
    }
      
    @Override
    public int getWeaponState(Actor actor) {
        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            return module.getWeaponState();
        }
        return -1;
    }

    @Override
    public void addSkinListener(Actor actor, SkinListener skinListener) {
        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            module.addSkinListener(skinListener);
        }
    }

    @Override
    public boolean removeSkinListener(Actor actor, SkinListener skinListener) {
        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            return module.removeSkinListener(skinListener);
        }
        return false;
    }
   
}
