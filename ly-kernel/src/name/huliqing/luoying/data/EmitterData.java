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
package name.huliqing.luoying.data;

import name.huliqing.luoying.xml.ObjectData;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author huliqing
 */
@Serializable
public class EmitterData extends ObjectData {
 
    public Integer getNumParticles() {
        return getAsInteger("numParticles");
    }

    public void setNumParticles(Integer numParticles) {
        setAttribute("numParticles", numParticles);
    }

    public ColorRGBA getStartColor() {
        return getAsColor("startColor");
    }

    public void setStartColor(ColorRGBA startColor) {
        setAttribute("startColor", startColor);
    }

    public ColorRGBA getEndColor() {
        return getAsColor("endColor");
    }

    public void setEndColor(ColorRGBA endColor) {
        setAttribute("endColor", endColor);
    }

    public Float getStartSize() {
        return getAsFloat("startSize");
    }

    public void setStartSize(Float startSize) {
        setAttribute("startSize", startSize);
    }

    public Float getEndSize() {
        return getAsFloat("endSize");
    }

    public void setEndSize(Float endSize) {
        setAttribute("endSize", endSize);
    }

    public Vector3f getGravity() {
        return getAsVector3f("gravity");
    }

    public void setGravity(Vector3f gravity) {
        setAttribute("gravity", gravity);
    }

    public Float getHighLife() {
        return getAsFloat("highLife");
    }

    public void setHighLife(Float highLife) {
        setAttribute("highLife", highLife);
    }

    public Float getLowLife() {
        return getAsFloat("lowLife");
    }

    public void setLowLife(Float lowLife) {
        setAttribute("lowLife", lowLife);
    }

    public String getTexture() {
        return getAsString("texture");
    }

    public void setTexture(String texture) {
        setAttribute("texture", texture);
    }

    public Integer getImagesX() {
        return getAsInteger("imagesX");
    }

    public void setImagesX(Integer imagesX) {
        setAttribute("imagesX", imagesX);
    }

    public Integer getImagesY() {
        return getAsInteger("imagesY");
    }

    public void setImagesY(Integer imagesY) {
        setAttribute("imagesY", imagesY);
    }

    public Integer getParticlesPerSec() {
        return getAsInteger("particlesPerSec");
    }

    public void setParticlesPerSec(Integer particlesPerSec) {
        setAttribute("particlesPerSec", particlesPerSec);
    }

    public Boolean getRandomAngle() {
        return getAsBoolean("randomAngle");
    }

    public void setRandomAngle(Boolean randomAngle) {
        setAttribute("randomAngle", randomAngle);
    }

    public Float getRotateSpeed() {
        return getAsFloat("rotateSpeed");
    }

    public void setRotateSpeed(Float rotateSpeed) {
        setAttribute("rotateSpeed", rotateSpeed);
    }

    public Boolean getSelectRandomImage() {
        return getAsBoolean("selectRandomImage");
    }

    public void setSelectRandomImage(Boolean selectRandomImage) {
        setAttribute("selectRandomImage", selectRandomImage);
    }

    public Vector3f getFaceNormal() {
        return getAsVector3f("faceNormal");
    }

    public void setFaceNormal(Vector3f faceNormal) {
        setAttribute("faceNormal", faceNormal);
    }

    public Boolean getFacingVelocity() {
        return getAsBoolean("facingVelocity");
    }

    public void setFacingVelocity(Boolean facingVelocity) {
        setAttribute("facingVelocity", facingVelocity);
    }

    public Vector3f getInitialVelocity() {
        return getAsVector3f("initialVelocity");
    }

    public void setInitialVelocity(Vector3f initialVelocity) {
        setAttribute("initialVelocity", initialVelocity);
    }

    public Float getVelocityVariation() {
        return getAsFloat("velocityVariation");
    }

    public void setVelocityVariation(Float velocityVariation) {
        setAttribute("velocityVariation", velocityVariation);
    }

    public String getShape() {
        return getAsString("shape");
    }

    public void setShape(String shape) {
        setAttribute("shape", shape);
    }
    
}
