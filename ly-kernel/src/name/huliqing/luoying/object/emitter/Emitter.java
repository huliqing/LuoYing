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
package name.huliqing.luoying.object.emitter;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.constants.AssetConstants;
import name.huliqing.luoying.data.EmitterData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.DataProcessor;
import name.huliqing.luoying.object.position.EmitterShapeWrap;
import name.huliqing.luoying.object.position.Position;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class Emitter<T extends EmitterData> implements DataProcessor<T> {

    private T data;
    private ParticleEmitter emitter;

    @Override
    public void setData(T data) {
        this.data = data;
    }
    
    @Override
    public T getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
    }
    
    public ParticleEmitter getParticleEmitter() {
        if (emitter == null) {
            emitter = loadEmitter(new ParticleEmitter(data.getId(), ParticleMesh.Type.Triangle, 0));
        }
        return emitter;
    }
    
    public ParticleEmitter getParticleEmitter(ParticleEmitter store) {
        if (store == null) {
            store = new ParticleEmitter(data.getId(), ParticleMesh.Type.Triangle, 0);
        }
        loadEmitter(store);
        return store;
    }
    
    private ParticleEmitter loadEmitter(ParticleEmitter pe) {
        pe.setName("emitter");
        pe.setMeshType(ParticleMesh.Type.Triangle);
        pe.setNumParticles(data.getNumParticles());
        
        if (data.getEndColor() != null) {
            pe.setEndColor(data.getEndColor());
        }
        if (data.getEndSize() != null) {
            pe.setEndSize(data.getEndSize());
        }
        if (data.getFaceNormal() != null) {
            pe.setFaceNormal(data.getFaceNormal());
        }
        if (data.getFacingVelocity() != null) {
            pe.setFacingVelocity(data.getFacingVelocity());
        }
        if (data.getGravity() != null) {
            pe.setGravity(data.getGravity());
        }
        if (data.getHighLife() != null) {
            pe.setHighLife(data.getHighLife());
        }
        if (data.getImagesX() != null) {
            pe.setImagesX(data.getImagesX());
        }
        if (data.getImagesY() != null) {
            pe.setImagesY(data.getImagesY());
        }
        if (data.getLowLife() != null) {
            pe.setLowLife(data.getLowLife());
        }
        if (data.getParticlesPerSec() != null) {
            pe.setParticlesPerSec(data.getParticlesPerSec());
        }
        if (data.getRandomAngle() != null) {
            pe.setRandomAngle(data.getRandomAngle());
        }
        if (data.getRotateSpeed() != null) {
            pe.setRotateSpeed(data.getRotateSpeed());
        }
        if (data.getSelectRandomImage() != null) {
            pe.setSelectRandomImage(data.getSelectRandomImage());
        }
        if (data.getStartColor() != null) {
            pe.setStartColor(data.getStartColor());
        }
        if (data.getStartSize() != null) {
            pe.setStartSize(data.getStartSize());
        }
        if (data.getInitialVelocity() != null) {
            pe.getParticleInfluencer().setInitialVelocity(data.getInitialVelocity());
        }
        if (data.getVelocityVariation() != null) {
            pe.getParticleInfluencer().setVelocityVariation(data.getVelocityVariation());
        }
        if (data.getShape() != null) {
            pe.setShape(new EmitterShapeWrap((Position) Loader.load(data.getShape())));
        }
        AssetManager am = LuoYing.getAssetManager();
        Material mat = new Material(am, AssetConstants.MATERIAL_PARTICLE);
        mat.setTexture("Texture", am.loadTexture(data.getTexture()));
        pe.setMaterial(mat);
        return pe;
    }
}
