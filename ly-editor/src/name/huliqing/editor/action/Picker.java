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
package name.huliqing.editor.action;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import name.huliqing.editor.edit.Mode;
import name.huliqing.luoying.manager.PickManager;

/**
 *
 * @author huliqing
 */
public class Picker {
    
    public final static Quaternion PLANE_XY = new Quaternion().fromAngleAxis(0, new Vector3f(1, 0, 0));
    public final static Quaternion PLANE_YZ = new Quaternion().fromAngleAxis(-FastMath.PI / 2, new Vector3f(0, 1, 0));
    public final static Quaternion PLANE_XZ = new Quaternion().fromAngleAxis(FastMath.PI / 2, new Vector3f(1, 0, 0));
    
    private final Node plane = new Node();
    private Quaternion origineRotation;
    private Mode mode;
    
    // 被
    private Spatial selectedSpatial;
    private Vector3f startSpatialLocation;
    private Vector3f startPickLoc;
    private Vector3f endPickLoc;
    
    public Picker() {
        float size = 10000;
        Geometry g = new Geometry("plane", new Quad(size, size));
        g.setLocalTranslation(-size / 2, -size / 2, 0);
        plane.attachChild(g);
    }
    
    public void startPick(Spatial selectedSpatial, Mode mode, Camera cam, Vector2f mouseLoc, Quaternion planeRotation) {
        this.selectedSpatial = selectedSpatial;
        this.mode = mode;
        
        startSpatialLocation = selectedSpatial.getWorldTranslation().clone();
        setTransformation(planeRotation, cam);
        plane.setLocalTranslation(startSpatialLocation);
        
        startPickLoc = PickManager.pickPoint(cam, mouseLoc, plane);
    }
    
    public void setTransformation(Quaternion planeRotation, Camera camera) {
        Quaternion rot = new Quaternion();
        if (null != mode) {
            switch (mode) {
                case LOCAL:
                    rot.set(selectedSpatial.getWorldRotation());
                    rot.multLocal(planeRotation);
                    origineRotation = selectedSpatial.getWorldRotation().clone();
                    break;
                    
                case GLOBAL:
                    rot.set(planeRotation);
                    origineRotation = new Quaternion(Quaternion.IDENTITY);
                    break;
                    
                case CAMERA: 
                    rot.set(camera.getRotation());
                    origineRotation = camera.getRotation();
                    break;
                    
                default:
                    throw new UnsupportedOperationException("Unsupported mode=" + mode);
            }
        }
        plane.setLocalRotation(rot);
    }
    
    public boolean updatePick(Camera cam, Vector2f screenLoc) {
        if (startPickLoc == null) {
            return false;
        }
        endPickLoc = PickManager.pickPoint(cam, screenLoc, plane);
        return endPickLoc != null;
    }
    
    public void endPick() {
        startPickLoc = null;
        endPickLoc = null;
    }
    
    public Vector3f getTranslation() {
        return endPickLoc.subtract(startPickLoc);
    }
    
    public Vector3f getTranslation(Vector3f axisConstrainte) {
        Vector3f localConstrainte = (origineRotation.mult(axisConstrainte)).normalize();
        Vector3f constrainedTranslation = localConstrainte.mult(getTranslation().dot(localConstrainte));
        return constrainedTranslation;
    }
    
    public Vector3f getLocalTranslation(Vector3f axisConstrainte) {
        return getTranslation(origineRotation.inverse().mult(axisConstrainte));
    }
    
    /**
     * @return the Quaternion rotation in the WorldSpace
     */
    public Quaternion getRotation() {
        return getRotation(Quaternion.IDENTITY);
    }

    /**
     *
     * @return the Quaternion rotation in the ToolSpace
     */
    public Quaternion getLocalRotation() {
        return getRotation(origineRotation.inverse());
    }

    /**
     * Get the Rotation into a specific custom space.
     * @param transforme the rotation to the custom space (World to Custom space)
     * @return the Rotation in the custom space
     */
    public Quaternion getRotation(Quaternion transforme) {
        Vector3f v1, v2;
        v1 = transforme.mult(startPickLoc.subtract(startSpatialLocation).normalize());
        v2 = transforme.mult(endPickLoc.subtract(startSpatialLocation).normalize());
        Vector3f axis = v1.cross(v2);
        float angle = v1.angleBetween(v2);
        return new Quaternion().fromAngleAxis(angle, axis);
    }
    
     /**
     *
     * @param store
     * @return the vector from the tool origin to the start location, in
     * WorldSpace
     */
    public Vector3f getStartOffset(Vector3f store) {
        return startPickLoc.subtract(startSpatialLocation, store);
    }
    
    /**
     *
     * @return the angle between the start location and the final location
     */
    public float getAngle() {
        Vector3f v1, v2;
        v1 = startPickLoc.subtract(startSpatialLocation);
        v2 = endPickLoc.subtract(startSpatialLocation);
        return v1.angleBetween(v2);
    }
    
    /**
     * @return the vector from the tool origin to the final location, in
     * WorldSpace
     */
    public Vector3f getFinalOffset() {
        return endPickLoc.subtract(startSpatialLocation);
    }
}
