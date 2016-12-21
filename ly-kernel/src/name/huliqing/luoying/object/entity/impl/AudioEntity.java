/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.object.entity.impl;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.ModelEntityData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.ModelEntity;
import name.huliqing.luoying.object.sound.Sound;
import name.huliqing.luoying.object.sound.SoundManager;

/**
 * @author huliqing
 */
public class AudioEntity extends ModelEntity {

    private boolean debug;
    private String soundId;
    
    // ---- inner
    private Sound sound;
    private Spatial debugNode;
    private Spatial debugInnerNode;

    @Override
    public void setData(ModelEntityData data) {
        super.setData(data);
        debug = data.getAsBoolean("debug", false);
        soundId = data.getAsString("sound");
    }

    @Override
    public void updateDatas() {
        super.updateDatas();
        sound.updateDatas();
    }
    
    @Override
    protected Spatial loadModel() {
        sound = Loader.load(soundId);
        return sound;
    }
    
    @Override
    public void initEntity() {
        super.initEntity();
        if (debug) {
            debugNode = new Geometry("debugAudioEnvDistance", new Sphere(20, 20, sound.getData().getMaxDistance()));
            debugNode.setMaterial(createDebugMaterial(ColorRGBA.Green));
            sound.attachChild(debugNode);
            
            debugInnerNode = new Geometry("debugAudioEnvRefDistance", new Sphere(20, 20, sound.getData().getRefDistance()));
            debugInnerNode.setMaterial(createDebugMaterial(ColorRGBA.Red));
            sound.attachChild(debugInnerNode);
        }
        SoundManager.getInstance().addAndPlay(sound);
    }
    
    private Material createDebugMaterial(ColorRGBA color) {
        Material mat = new Material(LuoYing.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.setColor("Color",  color);
        return mat;
    }

    @Override
    public void cleanup() {
        // 停止声音
        SoundManager.getInstance().removeAndStopDirectly(sound);
        if (debugNode != null) {
            sound.detachChild(debugNode);
            debugNode = null;
        }
        if (debugInnerNode != null) {
            sound.detachChild(debugInnerNode);
            debugInnerNode = null;
        }
        super.cleanup(); 
    }

    public Sound getSound() {
        return sound;
    }
    
}
