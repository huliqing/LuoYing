/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader.data;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import name.huliqing.fighter.data.EmitterData;
import name.huliqing.fighter.data.Proto;

/**
 *
 * @author huliqing
 */
public class EmitterDataLoader implements DataLoader<EmitterData>{

    @Override
    public EmitterData loadData(Proto proto) {
        EmitterData data = new EmitterData(proto.getId());
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
        return data;
    }
    
}
