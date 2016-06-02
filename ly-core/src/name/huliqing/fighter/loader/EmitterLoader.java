/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import name.huliqing.fighter.data.EmitterData;
import name.huliqing.fighter.object.position.EmitterShapeWrap;
import name.huliqing.fighter.utils.MatUtils;

/**
 *
 * @author huliqing
 */
class EmitterLoader {
 
    
    public static ParticleEmitter loadEmitter(EmitterData data, ParticleEmitter store) {
//        store.setName(data.getProto().getName());
        store.setName("emitter");
        store.setMeshType(ParticleMesh.Type.Triangle);
        store.setNumParticles(data.getNumParticles());
        
        if (data.getEndColor() != null) {
            store.setEndColor(data.getEndColor());
        }
        if (data.getEndSize() != null) {
            store.setEndSize(data.getEndSize());
        }
        if (data.getFaceNormal() != null) {
            store.setFaceNormal(data.getFaceNormal());
        }
        if (data.getFacingVelocity() != null) {
            store.setFacingVelocity(data.getFacingVelocity());
        }
        if (data.getGravity() != null) {
            store.setGravity(data.getGravity());
        }
        if (data.getHighLife() != null) {
            store.setHighLife(data.getHighLife());
        }
        if (data.getImagesX() != null) {
            store.setImagesX(data.getImagesX());
        }
        if (data.getImagesY() != null) {
            store.setImagesY(data.getImagesY());
        }
        if (data.getLowLife() != null) {
            store.setLowLife(data.getLowLife());
        }
        if (data.getParticlesPerSec() != null) {
            store.setParticlesPerSec(data.getParticlesPerSec());
        }
        if (data.getRandomAngle() != null) {
            store.setRandomAngle(data.getRandomAngle());
        }
        if (data.getRotateSpeed() != null) {
            store.setRotateSpeed(data.getRotateSpeed());
        }
        if (data.getSelectRandomImage() != null) {
            store.setSelectRandomImage(data.getSelectRandomImage());
        }
        if (data.getStartColor() != null) {
            store.setStartColor(data.getStartColor());
        }
        if (data.getStartSize() != null) {
            store.setStartSize(data.getStartSize());
        }
        if (data.getInitialVelocity() != null) {
            store.getParticleInfluencer().setInitialVelocity(data.getInitialVelocity());
        }
        if (data.getVelocityVariation() != null) {
            store.getParticleInfluencer().setVelocityVariation(data.getVelocityVariation());
        }
        if (data.getShape() != null) {
            store.setShape(new EmitterShapeWrap(Loader.loadPosition(data.getShape())));
        }
        
        // texture
//        AssetManager assetManager = Common.getAssetManager();
//        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
//        mat.setTexture("Texture", assetManager.loadTexture(data.getTexture()));
//        store.setMaterial(mat);
        
        Material mat = MatUtils.createParticle(data.getTexture());
        store.setMaterial(mat);
        return store;
    }
}
