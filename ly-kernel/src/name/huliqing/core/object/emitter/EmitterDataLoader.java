/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.emitter;

import name.huliqing.core.data.EmitterData;
import name.huliqing.core.data.Proto;
import name.huliqing.core.object.DataLoader;

/**
 *
 * @author huliqing
 */
public class EmitterDataLoader implements DataLoader<EmitterData> {

    @Override
    public void load(Proto proto, EmitterData data) {
        data.setNumParticles(proto.getAsInteger("numParticles"));
        data.setStartColor(proto.getAsColor("startColor"));
        data.setEndColor(proto.getAsColor("endColor"));
        data.setStartSize(proto.getAsFloat("startSize"));
        data.setEndSize(proto.getAsFloat("endSize"));
        data.setGravity(proto.getAsVector3f("gravity"));
        data.setHighLife(proto.getAsFloat("highLife"));
        data.setLowLife(proto.getAsFloat("lowLife"));
        data.setTexture(proto.getAttribute("texture"));
        data.setImagesX(proto.getAsInteger("imagesX"));
        data.setImagesY(proto.getAsInteger("imagesY"));
        data.setParticlesPerSec(proto.getAsInteger("particlesPerSec"));
        data.setRandomAngle(proto.getAsBoolean("randomAngle", false));
        data.setRotateSpeed(proto.getAsFloat("rotateSpeed"));
        data.setSelectRandomImage(proto.getAsBoolean("selectRandomImage", false));
        data.setFaceNormal(proto.getAsVector3f("faceNormal"));
        data.setFacingVelocity(proto.getAsBoolean("facingVelocity", false));
        data.setInitialVelocity(proto.getAsVector3f("initialVelocity"));
        data.setVelocityVariation(proto.getAsFloat("velocityVariation"));
        data.setShape(proto.getAttribute("shape"));
    }
    
}
