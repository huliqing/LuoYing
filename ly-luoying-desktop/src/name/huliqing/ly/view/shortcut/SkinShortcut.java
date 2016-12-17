/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.shortcut;

import com.jme3.math.ColorRGBA;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.luoying.object.entity.EntityDataListener;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.ly.layer.network.GameNetwork;

/**
 * 用于皮肤(Skin)的快捷方式
 * @author huliqing
 */
public class SkinShortcut extends BaseUIShortcut<SkinData> implements EntityDataListener {
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);

    @Override
    public void initialize() {
        super.initialize();
        entity.addEntityDataListener(this);
    }

    @Override
    public void cleanup() {
        entity.removeEntityDataListener(this);
        super.cleanup(); 
    }

    @Override
    public void removeObject() {
        gameNetwork.removeObjectData(entity, objectData.getUniqueId(), objectData.getTotal());
    }

    @Override
    public void onShortcutClick(boolean pressed) {
        if (pressed) {
            return;
        }
        gameNetwork.useObjectData(entity, objectData.getUniqueId());
    }

    @Override
    protected void updateShortcutViewChildren(float width, float height) {
        super.updateShortcutViewChildren(width, height);
        if (objectData.isUsed()) {
            icon.setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
        } else {
            icon.setBackgroundColor(ColorRGBA.White, true);
        }
    }

    @Override
    public void onDataAdded(ObjectData data, int amount) {
        if (!data.getId().equals(objectData.getId())) {
            return;
        }
        SkinData temp = entity.getData().getObjectData(objectData.getId());
        if (temp == null) {
            this.objectData.setTotal(0);
        } else if (temp != this.objectData) {
            this.objectData = temp;
        }
        updateObjectData();
    }

    @Override
    public void onDataRemoved(ObjectData data, int amount) {
        if (!data.getId().equals(objectData.getId())) {
            return;
        }
        SkinData temp = entity.getData().getObjectData(objectData.getId());
        if (temp == null) {
            this.objectData.setTotal(0);
        } else if (temp != this.objectData) {
            this.objectData = temp;
        }
        updateObjectData();
    }

    @Override
    public void onDataUsed(ObjectData data) {
        if (data != objectData) {
            return;
        }
        if (objectData.isUsed()) {
            icon.setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
        } else {
            icon.setBackgroundColor(ColorRGBA.White, true);
        }
    }
    
    
}
