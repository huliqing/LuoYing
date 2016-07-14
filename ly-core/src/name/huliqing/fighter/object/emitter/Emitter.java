/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.emitter;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.data.EmitterData;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.object.DataProcessor;
import name.huliqing.fighter.object.position.EmitterShapeWrap;

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
            pe.setShape(new EmitterShapeWrap(Loader.loadPosition(data.getShape())));
        }
        AssetManager am = Common.getAssetManager();
        Material mat = new Material(am, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", am.loadTexture(data.getTexture()));
        pe.setMaterial(mat);
        return pe;
    }
}
