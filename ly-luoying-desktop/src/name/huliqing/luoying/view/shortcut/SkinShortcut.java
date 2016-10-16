/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.view.shortcut;

import com.jme3.math.ColorRGBA;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.luoying.layer.network.SkinNetwork;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.SkinListener;
import name.huliqing.luoying.object.module.SkinModule;
import name.huliqing.luoying.object.skin.Skin;
import name.huliqing.luoying.object.skin.Weapon;
import name.huliqing.luoying.ui.UIFactory;

/**
 * 用于皮肤(Skin)的快捷方式
 * @author huliqing
 */
public class SkinShortcut extends BaseUIShortcut<SkinData> implements SkinListener {
    private final SkinNetwork skinNetwork = Factory.get(SkinNetwork.class);
    private SkinModule skinModule;

    @Override
    public void initialize() {
        super.initialize();
        skinModule = actor.getModule(SkinModule.class);
        skinModule.addSkinListener(this);
    }

    @Override
    public void cleanup() {
        if (skinModule != null) {
            skinModule.removeSkinListener(this);
        }
        super.cleanup(); 
    }

    @Override
    public void removeObject() {
        Skin skin = skinModule.getSkin(objectData.getId());
        if (skin != null && skin.isAttached()) {
            // 正在使用的Skin不能删除 
            return;
        }
        skinNetwork.removeSkin(actor, objectData.getId(), objectData.getTotal());
    }
    
    @Override
    public void onSkinAdded(Entity actor, Skin skinAdded) {
        if (skinAdded.getData().getId().equals(objectData.getId())) {
            updateObjectData(skinAdded.getData());
        }
    }
    
    @Override
    public void onSkinRemoved(Entity actor, Skin skinRemoved) {
        if (skinRemoved.getData().getId().equals(objectData.getId())) {
            updateObjectData(skinRemoved.getData());
        }
    }

    @Override
    public void onSkinAttached(Entity actor, Skin skin) {
        if (objectData != skin.getData()) {
            return;
        }
        icon.setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
    }

    @Override
    public void onSkinDetached(Entity actor, Skin skin) {
        if (objectData != skin.getData()) {
            return;
        }
        icon.setBackgroundColor(ColorRGBA.White, true);
    }

    @Override
    public void onShortcutClick(boolean pressed) {
        if (pressed) {
            return;
        }
        Skin skin = skinModule.getSkin(objectData.getId());
        
        // 有可能存在shortcut存在，但物品已经被从角色包裹删除的现象，所以skin有可能为null.
        if (skin == null) {
            return;
        }
        
        // Outfit
        if (!(skin instanceof Weapon)) {
            if (skin.isAttached()) {
                skinNetwork.detachSkin(actor, skin);
            } else {
                skinNetwork.attachSkin(actor, skin);
            }
            return;
        }
        
        // Weapon
        if (skin.isAttached()) {
            if (skinModule.isWeaponTakeOn()) {
                skinNetwork.takeOffWeapon(actor);
            } else {
                skinNetwork.detachSkin(actor, skin);
            }
        } else {
            skinNetwork.attachSkin(actor, skin);
        }

    }

    @Override
    protected void updateShortcutViewChildren(float width, float height) {
        super.updateShortcutViewChildren(width, height);
        Skin skin = skinModule.getSkin(objectData.getId());
        if (skin != null) {
            if (skin.isAttached()) {
                icon.setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
            } else {
                icon.setBackgroundColor(ColorRGBA.White, true);
            }
        }
    }
    
}
