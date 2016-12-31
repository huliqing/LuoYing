/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package name.huliqing.editor.utils;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.input.CameraInput;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.util.clone.Cloner;
import com.jme3.util.clone.JmeCloneable;
import java.io.IOException;


public class SimpleChaseCamera implements ActionListener, AnalogListener, Control, JmeCloneable {
    
    protected float minVerticalRotation = 0.00f;
    protected float maxVerticalRotation = FastMath.PI / 2;
    protected float minDistance = 1.0f;
    protected float maxDistance = 1000.0f;
    protected float distance = 20;    
    protected float rotationSpeed = 1.0f;
    protected float zoomSensitivity = 2f;
    protected float rotation = 0;
    protected float vRotation = FastMath.PI / 6;
    protected InputManager inputManager;
    protected Vector3f initialUpVec;
//    protected boolean veryCloseRotation = true;
    protected boolean canRotate;
    protected boolean enabled = true;
    protected Camera cam = null;
    protected final Vector3f pos = new Vector3f();
    protected Spatial target = null;
    protected Vector3f targetLocation = new Vector3f(0, 0, 0);
    protected float targetRotation = rotation;
    protected float targetVRotation = vRotation;
    protected float targetDistance = distance;
    protected boolean dragToRotate = true;
    protected Vector3f temp = new Vector3f(0, 0, 0);
    protected boolean invertYaxis = false;
    protected boolean invertXaxis = false;
    protected boolean zoomin;

    public SimpleChaseCamera(Camera cam, InputManager inputManager) {
        this.cam = cam;
        initialUpVec = cam.getUp().clone();
        registerWithInput(inputManager);
    }
    
    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        if (dragToRotate) {
            if (name.equals(CameraInput.CHASECAM_TOGGLEROTATE) && enabled) {
                if (keyPressed) {
                    canRotate = true;
                } else {
                    canRotate = false;
                }
            }
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (name.equals(CameraInput.CHASECAM_MOVELEFT)) {
            rotateCamera(-value);
        } else if (name.equals(CameraInput.CHASECAM_MOVERIGHT)) {
            rotateCamera(value);
        } else if (name.equals(CameraInput.CHASECAM_UP)) {
            vRotateCamera(value);
        } else if (name.equals(CameraInput.CHASECAM_DOWN)) {
            vRotateCamera(-value);
        } else if (name.equals(CameraInput.CHASECAM_ZOOMIN)) {
            zoomCamera(-value);
            zoomin = true;
        } else if (name.equals(CameraInput.CHASECAM_ZOOMOUT)) {
            zoomCamera(+value);
            zoomin = false;
        }
    }

    /**
     * Registers inputs with the input manager
     * @param inputManager
     */
    public final void registerWithInput(InputManager inputManager) {

        String[] inputs = {CameraInput.CHASECAM_TOGGLEROTATE,
            CameraInput.CHASECAM_DOWN,
            CameraInput.CHASECAM_UP,
            CameraInput.CHASECAM_MOVELEFT,
            CameraInput.CHASECAM_MOVERIGHT,
            CameraInput.CHASECAM_ZOOMIN,
            CameraInput.CHASECAM_ZOOMOUT};

        this.inputManager = inputManager;
        if (!invertYaxis) {
            inputManager.addMapping(CameraInput.CHASECAM_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
            inputManager.addMapping(CameraInput.CHASECAM_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        } else {
            inputManager.addMapping(CameraInput.CHASECAM_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
            inputManager.addMapping(CameraInput.CHASECAM_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        }
        inputManager.addMapping(CameraInput.CHASECAM_ZOOMIN, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping(CameraInput.CHASECAM_ZOOMOUT, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        if (!invertXaxis) {
            inputManager.addMapping(CameraInput.CHASECAM_MOVELEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
            inputManager.addMapping(CameraInput.CHASECAM_MOVERIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
        } else {
            inputManager.addMapping(CameraInput.CHASECAM_MOVELEFT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
            inputManager.addMapping(CameraInput.CHASECAM_MOVERIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
        }
        inputManager.addMapping(CameraInput.CHASECAM_TOGGLEROTATE, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping(CameraInput.CHASECAM_TOGGLEROTATE, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        inputManager.addListener(this, inputs);
    }
    
    /**
     * 清理相机的按键绑定
     */
    public void cleanup() {
        String[] inputs = {CameraInput.CHASECAM_TOGGLEROTATE,
            CameraInput.CHASECAM_DOWN,
            CameraInput.CHASECAM_UP,
            CameraInput.CHASECAM_MOVELEFT,
            CameraInput.CHASECAM_MOVERIGHT,
            CameraInput.CHASECAM_ZOOMIN,
            CameraInput.CHASECAM_ZOOMOUT};
        for (String input : inputs) {
            inputManager.deleteMapping(input);
        }
    }

    /**
     * Sets custom triggers for toggleing the rotation of the cam
     * deafult are
     * new MouseButtonTrigger(MouseInput.BUTTON_LEFT)  left mouse button
     * new MouseButtonTrigger(MouseInput.BUTTON_RIGHT)  right mouse button
     * @param triggers
     */
    public void setToggleRotationTrigger(Trigger... triggers) {
        inputManager.deleteMapping(CameraInput.CHASECAM_TOGGLEROTATE);
        inputManager.addMapping(CameraInput.CHASECAM_TOGGLEROTATE, triggers);
        inputManager.addListener(this, CameraInput.CHASECAM_TOGGLEROTATE);
    }

    /**
     * Sets custom triggers for zomming in the cam
     * default is
     * new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true)  mouse wheel up
     * @param triggers
     */
    public void setZoomInTrigger(Trigger... triggers) {
        inputManager.deleteMapping(CameraInput.CHASECAM_ZOOMIN);
        inputManager.addMapping(CameraInput.CHASECAM_ZOOMIN, triggers);
        inputManager.addListener(this, CameraInput.CHASECAM_ZOOMIN);
    }

    /**
     * Sets custom triggers for zomming out the cam
     * default is
     * new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false)  mouse wheel down
     * @param triggers
     */
    public void setZoomOutTrigger(Trigger... triggers) {
        inputManager.deleteMapping(CameraInput.CHASECAM_ZOOMOUT);
        inputManager.addMapping(CameraInput.CHASECAM_ZOOMOUT, triggers);
        inputManager.addListener(this, CameraInput.CHASECAM_ZOOMOUT);
    }

    //rotate the camera around the target on the horizontal plane
    protected void rotateCamera(float value) {
        if (!canRotate || !enabled) {
            return;
        }
        targetRotation += value * rotationSpeed;
    }

    //move the camera toward or away the target
    protected void zoomCamera(float value) {
        if (!enabled) {
            return;
        }
        targetDistance += value * zoomSensitivity;
        if (targetDistance > maxDistance) {
            targetDistance = maxDistance;
        }
        if (targetDistance < minDistance) {
            targetDistance = minDistance;
        }
    }

    //rotate the camera around the target on the vertical plane
    protected void vRotateCamera(float value) {
        if (!canRotate || !enabled) {
            return;
        }
        float lastGoodRot = targetVRotation;
        targetVRotation += value * rotationSpeed;
        if (targetVRotation > maxVerticalRotation) {
            targetVRotation = lastGoodRot;
        }
    }

    /**
     * Updates the camera, should only be called internally
     * @param tpf
     */
    protected void updateCamera(float tpf) {
        if (enabled) {
            targetLocation.set(target.getWorldTranslation());
            
            //easy no smooth motion
            vRotation = targetVRotation;
            rotation = targetRotation;
            distance = targetDistance;
            computePosition();
            
            cam.setLocation(pos);
            cam.lookAt(targetLocation, initialUpVec);
        }
    }
    
    protected void computePosition() {
        float hDistance = (distance) * FastMath.sin((FastMath.PI / 2) - vRotation);
        pos.set(hDistance * FastMath.cos(rotation), (distance) * FastMath.sin(vRotation), hDistance * FastMath.sin(rotation));
        pos.addLocal(target.getWorldTranslation());
    }
    
    /**
     * Return the enabled/disabled state of the camera
     * @return true if the camera is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Enable or disable the camera
     * @param enabled true to enable
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            canRotate = false; // reset this flag in-case it was on before
        }
    }
    
    /**
     * Sets the max zoom distance of the camera (default is 40)
     * @param maxDistance
     */
    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
        if (maxDistance < distance) {
            zoomCamera(maxDistance - distance);
        }
    }

    /**
     * Sets the min zoom distance of the camera (default is 1)
     * @param minDistance
     */
    public void setMinDistance(float minDistance) {
        this.minDistance = minDistance;
        if (minDistance > distance) {
            zoomCamera(distance - minDistance);
        }
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException();
    }

    @Override   
    public Object jmeClone() {
        throw new UnsupportedOperationException();
    }     

    @Override   
    public void cloneFields( Cloner cloner, Object original ) {
        throw new UnsupportedOperationException();
    }
         
    /**
     * Sets the spacial for the camera control, should only be used internally
     * @param spatial
     */
    @Override
    public void setSpatial(Spatial spatial) {
        target = spatial;
        if (spatial == null) {
            return;
        }
        computePosition();
        cam.setLocation(pos);
    }

    /**
     * update the camera control, should only be used internally
     * @param tpf
     */
    @Override
    public void update(float tpf) {
        updateCamera(tpf);
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
        //nothing to render
    }

    /**
     * Write the camera
     * @param ex the exporter
     * @throws IOException
     */
    @Override
    public void write(JmeExporter ex) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Read the camera
     * @param im
     * @throws IOException
     */
    @Override
    public void read(JmeImporter im) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets the maximal vertical rotation angle in radian of the camera around the target. Default is Pi/2;
     * @param maxVerticalRotation
     */
    public void setMaxVerticalRotation(float maxVerticalRotation) {
        this.maxVerticalRotation = maxVerticalRotation;
    }

    /**
     * Sets the minimal vertical rotation angle in radian of the camera around the target default is 0;
     * @param minHeight
     */
    public void setMinVerticalRotation(float minHeight) {
        this.minVerticalRotation = minHeight;
    }

    /**
     * Sets the zoom sensitivity, the lower the value, the slower the camera will zoom in and out.
     * default is 2.
     * @param zoomSensitivity
     */
    public void setZoomSensitivity(float zoomSensitivity) {
        this.zoomSensitivity = zoomSensitivity;
    }

    /**
     * Sets the rotate amount when user moves his mouse, the lower the value,
     * the slower the camera will rotate. default is 1.
     *
     * @param rotationSpeed Rotation speed on mouse movement, default is 1.
     */
    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    /**
     * Sets the default distance at start of applicaiton
     * @param defaultDistance
     */
    public void setDefaultDistance(float defaultDistance) {
        distance = defaultDistance;
        targetDistance = distance;
    }

    /**
     * sets the default horizontal rotation in radian of the camera at start of the application
     * @param angleInRad
     */
    public void setDefaultHorizontalRotation(float angleInRad) {
        rotation = angleInRad;
        targetRotation = angleInRad;
    }

    /**
     * sets the default vertical rotation in radian of the camera at start of the application
     * @param angleInRad
     */
    public void setDefaultVerticalRotation(float angleInRad) {
        vRotation = angleInRad;
        targetVRotation = angleInRad;
    }
    
    public void setDragToRotate(boolean dragToRotate) {
        this.dragToRotate = dragToRotate;
        this.canRotate = !dragToRotate;
        inputManager.setCursorVisible(dragToRotate);
    }

//    public void setDownRotateOnCloseViewOnly(boolean rotateOnlyWhenClose) {
//        veryCloseRotation = rotateOnlyWhenClose;
//    }
//    
//    public boolean getDownRotateOnCloseViewOnly() {
//        return veryCloseRotation;
//    }

    /**
     * Sets the up vector of the camera used for the lookAt on the target
     * @param up
     */
    public void setUpVector(Vector3f up) {
        initialUpVec = up;
    }

    /**
     * invert the vertical axis movement of the mouse
     * @param invertYaxis
     */
    public void setInvertVerticalAxis(boolean invertYaxis) {
        this.invertYaxis = invertYaxis;
        inputManager.deleteMapping(CameraInput.CHASECAM_DOWN);
        inputManager.deleteMapping(CameraInput.CHASECAM_UP);
        if (!invertYaxis) {
            inputManager.addMapping(CameraInput.CHASECAM_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
            inputManager.addMapping(CameraInput.CHASECAM_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        } else {
            inputManager.addMapping(CameraInput.CHASECAM_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
            inputManager.addMapping(CameraInput.CHASECAM_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        }
        inputManager.addListener(this, CameraInput.CHASECAM_DOWN, CameraInput.CHASECAM_UP);
    }

    /**
     * invert the Horizontal axis movement of the mouse
     * @param invertXaxis
     */
    public void setInvertHorizontalAxis(boolean invertXaxis) {
        this.invertXaxis = invertXaxis;
        inputManager.deleteMapping(CameraInput.CHASECAM_MOVELEFT);
        inputManager.deleteMapping(CameraInput.CHASECAM_MOVERIGHT);
        if (!invertXaxis) {
            inputManager.addMapping(CameraInput.CHASECAM_MOVELEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
            inputManager.addMapping(CameraInput.CHASECAM_MOVERIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
        } else {
            inputManager.addMapping(CameraInput.CHASECAM_MOVELEFT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
            inputManager.addMapping(CameraInput.CHASECAM_MOVERIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
        }
        inputManager.addListener(this, CameraInput.CHASECAM_MOVELEFT, CameraInput.CHASECAM_MOVERIGHT);
    }
}
