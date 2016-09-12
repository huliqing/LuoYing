/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.shortcut;

import com.jme3.math.ColorRGBA;
import name.huliqing.core.Factory;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.mvc.network.SkinNetwork;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.SkinListener;
import name.huliqing.core.object.module.SkinModule;
import name.huliqing.core.object.skin.Skin;
import name.huliqing.core.ui.UIFactory;

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
    public void onSkinAdded(Actor actor, Skin skinAdded) {
        if (skinAdded.getData().getId().equals(objectData.getId())) {
            updateObjectData(skinAdded.getData());
        }
    }
    
    @Override
    public void onSkinRemoved(Actor actor, Skin skinRemoved) {
        if (skinRemoved.getData().getId().equals(objectData.getId())) {
            updateObjectData(skinRemoved.getData());
        }
    }

    @Override
    public void onSkinAttached(Actor actor, Skin skin) {
        if (objectData != skin.getData()) {
            return;
        }
        icon.setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
    }

    @Override
    public void onSkinDetached(Actor actor, Skin skin) {
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
        
        // Outfit
        if (!skin.isWeapon()) {
            if (skin.isAttached()) {
                skinNetwork.detachSkin(actor, skin);
            } else {
                skinNetwork.attachSkin(actor, skin);
            }
            return;
        }
        
        // weapon
        if (skin.isAttached()) {
            if (skinModule.isWeaponTakeOn()) {
                skinNetwork.takeOffWeapon(actor, false);
            } else {
                skinNetwork.detachSkin(actor, skin);
            }
        } else {
            skinNetwork.attachSkin(actor, skin);
        }

    }
    
}
