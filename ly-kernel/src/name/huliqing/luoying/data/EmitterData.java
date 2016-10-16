/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author huliqing
 */
@Serializable
public class EmitterData extends ObjectData {
    private Integer numParticles;
    private ColorRGBA startColor;
    private ColorRGBA endColor;
    private Float startSize;
    private Float endSize;
    private Vector3f gravity;
    private Float highLife;
    private Float lowLife;
    private String texture;
    private Integer imagesX;
    private Integer imagesY;
    private Integer particlesPerSec;
    private Boolean randomAngle;
    private Float rotateSpeed;
    private Boolean selectRandomImage;
    private Vector3f faceNormal;
    private Boolean facingVelocity;
    private Vector3f initialVelocity;
    private Float velocityVariation;
    private String shape;
                                
    public EmitterData() {}

    public Integer getNumParticles() {
        return numParticles;
    }

    public void setNumParticles(Integer numParticles) {
        this.numParticles = numParticles;
    }

    public ColorRGBA getStartColor() {
        return startColor;
    }

    public void setStartColor(ColorRGBA startColor) {
        this.startColor = startColor;
    }

    public ColorRGBA getEndColor() {
        return endColor;
    }

    public void setEndColor(ColorRGBA endColor) {
        this.endColor = endColor;
    }

    public Float getStartSize() {
        return startSize;
    }

    public void setStartSize(Float startSize) {
        this.startSize = startSize;
    }

    public Float getEndSize() {
        return endSize;
    }

    public void setEndSize(Float endSize) {
        this.endSize = endSize;
    }

    public Vector3f getGravity() {
        return gravity;
    }

    public void setGravity(Vector3f gravity) {
        this.gravity = gravity;
    }

    public Float getHighLife() {
        return highLife;
    }

    public void setHighLife(Float highLife) {
        this.highLife = highLife;
    }

    public Float getLowLife() {
        return lowLife;
    }

    public void setLowLife(Float lowLife) {
        this.lowLife = lowLife;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public Integer getImagesX() {
        return imagesX;
    }

    public void setImagesX(Integer imagesX) {
        this.imagesX = imagesX;
    }

    public Integer getImagesY() {
        return imagesY;
    }

    public void setImagesY(Integer imagesY) {
        this.imagesY = imagesY;
    }

    public Integer getParticlesPerSec() {
        return particlesPerSec;
    }

    public void setParticlesPerSec(Integer particlesPerSec) {
        this.particlesPerSec = particlesPerSec;
    }

    public Boolean getRandomAngle() {
        return randomAngle;
    }

    public void setRandomAngle(Boolean randomAngle) {
        this.randomAngle = randomAngle;
    }

    public Float getRotateSpeed() {
        return rotateSpeed;
    }

    public void setRotateSpeed(Float rotateSpeed) {
        this.rotateSpeed = rotateSpeed;
    }

    public Boolean getSelectRandomImage() {
        return selectRandomImage;
    }

    public void setSelectRandomImage(Boolean selectRandomImage) {
        this.selectRandomImage = selectRandomImage;
    }

    public Vector3f getFaceNormal() {
        return faceNormal;
    }

    public void setFaceNormal(Vector3f faceNormal) {
        this.faceNormal = faceNormal;
    }

    public Boolean getFacingVelocity() {
        return facingVelocity;
    }

    public void setFacingVelocity(Boolean facingVelocity) {
        this.facingVelocity = facingVelocity;
    }

    public Vector3f getInitialVelocity() {
        return initialVelocity;
    }

    public void setInitialVelocity(Vector3f initialVelocity) {
        this.initialVelocity = initialVelocity;
    }

    public Float getVelocityVariation() {
        return velocityVariation;
    }

    public void setVelocityVariation(Float velocityVariation) {
        this.velocityVariation = velocityVariation;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

   
}
