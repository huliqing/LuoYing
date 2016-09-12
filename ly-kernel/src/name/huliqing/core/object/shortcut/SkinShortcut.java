/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.shortcut;

import com.jme3.math.ColorRGBA;
import name.huliqing.core.Factory;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.mvc.service.SkinService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.SkinListener;
import name.huliqing.core.object.skin.Skin;
import name.huliqing.core.ui.UIFactory;

/**
 * 用于皮肤(Skin)的快捷方式
 * @author huliqing
 */
public class SkinShortcut extends BaseUIShortcut<SkinData> implements SkinListener {
    private final SkinService skinService = Factory.get(SkinService.class);

    @Override
    public void initialize() {
        super.initialize();
        skinService.addSkinListener(actor, this);
    }

    @Override
    public void cleanup() {
        skinService.removeSkinListener(actor, this);
        super.cleanup(); 
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
        super.onShortcutClick(pressed); 
//To change body of generated methods, choose Tools | Templates.
    }
    
}
