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

package name.huliqing.ly.processor;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.Camera.FrustumIntersect;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.Texture2D;
import com.jme3.water.ReflectionProcessor;
import com.jme3.water.WaterUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import name.huliqing.ly.constants.MaterialConstants;
import name.huliqing.ly.constants.TextureConstants;

/**
 *
 * Simple Water renders a simple plane that use reflection and refraction to look like water.
 * It's pretty basic, but much faster than the WaterFilter
 * It's useful if you aim low specs hardware and still want a good looking water.
 * Usage is :
 * <code>
 *      SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(assetManager);
 *      //setting the scene to use for reflection
 *      waterProcessor.setReflectionScene(mainScene);
 *      //setting the light position
 *      waterProcessor.setLightPosition(lightPos);
 *
 *      //setting the water plane
 *      Vector3f waterLocation=new Vector3f(0,-20,0);
 *      waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
 *      //setting the water color
 *      waterProcessor.setWaterColor(ColorRGBA.Brown);
 *
 *      //creating a quad to render water to
 *      Quad quad = new Quad(400,400);
 *
 *      //the texture coordinates define the general size of the waves
 *      quad.scaleTextureCoordinates(new Vector2f(6f,6f));
 *
 *      //creating a geom to attach the water material
 *      Geometry water=new Geometry("water", quad);
 *      water.setLocalTranslation(-200, -20, 250);
 *      water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
 *      //finally setting the material
 *      water.setMaterial(waterProcessor.getMaterial());
 *
 *      //attaching the water to the root node
 *      rootNode.attachChild(water);
 * </code>
 * @author Normen Hansen & R茅my Bouquet
 * 
 * 这个类修改自com.jme3.water.SimpleWaterProcessor, 主要处理在Android下发生的异常,并进行了廋身
 */
public class VerySimpleWaterProcessor implements SceneProcessor {

//    private static final Logger LOG = Logger.getLogger(VerySimpleWaterProcessor.class.getName());

    protected RenderManager rm;
    protected ViewPort vp;
    protected ViewPort reflectionView;
    protected FrameBuffer reflectionBuffer;
    protected Camera reflectionCam;
    protected Texture2D reflectionTexture;
    protected Texture2D normalTexture;
    protected Texture2D dudvTexture;
    protected int renderWidth = 512;
    protected int renderHeight = 512;
    protected Plane plane = new Plane(Vector3f.UNIT_Y, Vector3f.ZERO.dot(Vector3f.UNIT_Y));
    protected float speed = 0.05f;
    protected Ray ray = new Ray();
    protected Vector3f targetLocation = new Vector3f();
    protected AssetManager manager;
    protected Material material;
    protected float waterDepth = 1;
    protected float waterTransparency = 0.4f;
    protected boolean debug = false;
    
    private Plane reflectionClipPlane;

    private float reflectionClippingOffset = -5f;        
    private float distortionScale = 0.2f;
    private float distortionMix = 0.5f;
    private float texScale = 1f;
    private ColorRGBA waterColor;
    
    private final Spatial waterPlane;
    
    private final List<Spatial> tempReflectionScenes = new ArrayList<Spatial>();
       
    /**
     * Creates a SimpleWaterProcessor
     * @param manager the asset manager
     * @param waterPlane
     */
    public VerySimpleWaterProcessor(AssetManager manager, Spatial waterPlane) {
        this.manager = manager;
        this.waterPlane = waterPlane;
        
        material = new Material(manager, MaterialConstants.MAT_SIMPLE_WATER);
        material.setFloat("distortionScale", distortionScale);
        material.setFloat("distortionMix", distortionMix);
        material.setFloat("texScale", texScale);
        
        setFoamScale(1.0f, 1.0f);
        setFoamMaskScale(1.0f, 1.0f);
        
        updateClipPlanes();

        waterPlane.setMaterial(material);
    }

    @Override
    public void initialize(RenderManager rm, ViewPort vp) {
        this.rm = rm;
        this.vp = vp;

        loadTextures(manager);
        createTextures();
        applyTextures(material);

        createPreViews();
//        material.setVector2("FrustumNearFar", new Vector2f(vp.getCamera().getFrustumNear(), vp.getCamera().getFrustumFar()));
    }

    @Override
    public void reshape(ViewPort vp, int w, int h) {
    }

    @Override
    public boolean isInitialized() {
        return rm != null;
    }
    float time = 0;
    float savedTpf = 0;

    @Override
    public void preFrame(float tpf) {
        time = time + (tpf * speed);
        if (time > 1f) {
            time = 0;
        }
        material.setFloat("time", time);
        savedTpf = tpf;
    }

    @Override
    public void postQueue(RenderQueue rq) {
        Camera sceneCam = rm.getCurrentCamera();

        // 如果水体在镜头外就不要渲染，浪费性能。
        FrustumIntersect fi = sceneCam.contains(waterPlane.getWorldBound());
        if (fi != FrustumIntersect.Outside) {
//            LOG.log(Level.INFO, "Need render simple water. FrustumIntersect={0}", fi);
            WaterUtils.updateReflectionCam(reflectionCam, plane, sceneCam);
            rm.renderViewPort(reflectionView, savedTpf);
            rm.getRenderer().setFrameBuffer(vp.getOutputFrameBuffer());
            rm.setCamera(sceneCam, false);
        } 
//        else {
//            LOG.log(Level.INFO, "Don't need render simple water.");
//        }

    }

    @Override
    public void postFrame(FrameBuffer out) {}

    @Override
    public void cleanup() {
        // 注意：这里必须释放framebuffer,否则该framebuffer会一直常驻内存无法释放。
        rm.removePreView(reflectionView);
        reflectionView.setOutputFrameBuffer(null);
        reflectionView.clearScenes();
        reflectionView.clearProcessors();
        reflectionView = null;
        reflectionBuffer = null;
    }

    protected void loadTextures(AssetManager manager) {
        normalTexture = (Texture2D) manager.loadTexture(TextureConstants.TEX_SIMPLE_WATER_NORMAL);
        dudvTexture = (Texture2D) manager.loadTexture(TextureConstants.TEX_SIMPLE_WATER_DUDV);
        normalTexture.setWrap(WrapMode.Repeat);
        dudvTexture.setWrap(WrapMode.Repeat);
    }

    protected void createTextures() {
        reflectionTexture = new Texture2D(renderWidth, renderHeight, Format.RGBA8);
        
        // MinFilter.Trilinear这个过滤设置可能是导致在三星平板下(Android5.0.2, Galaxy Note 10.1 - GT N8010)水体效果性能极其缓慢的问题。
        // 该问题导致在Galaxy Note 10.1 - GT N8010下画面几乎降低20~30帧（50降低至20帧左右）
        // (这个问题在华为HUWEI G6-U00(Android4.3)下却没有问题)
//        reflectionTexture.setMinFilter(Texture.MinFilter.Trilinear);
//        reflectionTexture.setMagFilter(Texture.MagFilter.Bilinear);
    }

    protected void applyTextures(Material mat) {
        mat.setTexture("water_reflection", reflectionTexture);
//        mat.setTexture("water_refraction", refractionTexture);
//        mat.setTexture("water_depthmap", depthTexture);
        mat.setTexture("water_normalmap", normalTexture);
        mat.setTexture("water_dudvmap", dudvTexture);
        
    }

    protected void createPreViews() {
        reflectionCam = new Camera(renderWidth, renderHeight);
//        refractionCam = new Camera(renderWidth, renderHeight);

        // create a pre-view. a view that is rendered before the main view
        reflectionView = new ViewPort("Reflection View", reflectionCam);
        reflectionView.setClearFlags(true, true, true);
        reflectionView.setBackgroundColor(ColorRGBA.Black);
        // create offscreen framebuffer
        reflectionBuffer = new FrameBuffer(renderWidth, renderHeight, 1);
        //setup framebuffer to use texture
        reflectionBuffer.setColorTexture(reflectionTexture);
        
        // remove20160704，不要使用DepthBuffer，这在Android下无法支持
//        reflectionBuffer.setDepthBuffer(Format.Depth);
        
        //set viewport to render to offscreen framebuffer
        reflectionView.setOutputFrameBuffer(reflectionBuffer);
        reflectionView.addProcessor(new ReflectionProcessor(reflectionCam, reflectionBuffer, reflectionClipPlane));
        
        // attach the scene to the viewport to be rendered
        if (!tempReflectionScenes.isEmpty()) {
            for (Spatial scene : tempReflectionScenes) {
                addReflectionScene(scene);
            }
            tempReflectionScenes.clear();
        }
    }
    
    public void addReflectionScene(Spatial scene) {
        if (reflectionView == null) {
            tempReflectionScenes.add(scene);
            return;
        }
        if (!reflectionView.getScenes().contains(scene)) {
            reflectionView.attachScene(scene);
            // 特殊更新一下reflectionScene，当reflectionScene是在特殊节点下（非JME的rootNode）时必须手动更新一下。
            scene.updateGeometricState();
        }
    }

    /**
     * Get the water material from this processor, apply this to your water quad.
     * @return
     */
    public Material getMaterial() {
        return material;
    }

    // remove20160706,use addReflectionScene instead
//    /**
//     * Sets the reflected scene, should not include the water quad!
//     * Set before adding processor.
//     * @param spat
//     */
//    public void setReflectionScene(Spatial spat) {
//        reflectionScene = spat;
//    }

    /**
     * returns the width of the reflection and refraction textures
     * @return
     */
    public int getRenderWidth() {
        return renderWidth;
    }

    /**
     * returns the height of the reflection and refraction textures
     * @return
     */
    public int getRenderHeight() {
        return renderHeight;
    }

    /**
     * Set the reflection Texture render size,
     * set before adding the processor!
     * @param width
     * @param height
     */
    public void setRenderSize(int width, int height) {
        renderWidth = width;
        renderHeight = height;
    }

    /**
     * returns the water plane
     * @return
     */
    public Plane getPlane() {
        return plane;
    }

    /**
     * Set the water plane for this processor.
     * @param plane
     */
    public void setPlane(Plane plane) {
        this.plane.setConstant(plane.getConstant());
        this.plane.setNormal(plane.getNormal());
        updateClipPlanes();
    }

    /**
     * Set the water plane using an origin (location) and a normal (reflection direction).
     * @param origin Set to 0,-6,0 if your water quad is at that location for correct reflection
     * @param normal Set to 0,1,0 (Vector3f.UNIT_Y) for normal planar water
     */
    public void setPlane(Vector3f origin, Vector3f normal) {
        this.plane.setOriginNormal(origin, normal);
        updateClipPlanes();
    }

    private void updateClipPlanes() {
        reflectionClipPlane = plane.clone();
        reflectionClipPlane.setConstant(reflectionClipPlane.getConstant() + reflectionClippingOffset);
    }

    /**
     * Sets the speed of the wave animation, default = 0.05f.
     * @param speed
     */
    public void setWaveSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * returns the speed of the wave animation.
     * @return the speed
     */
    public float getWaveSpeed(){
        return speed;
    }
    
    /**
     * Sets the scale of distortion by the normal map, default = 0.2
     * @param value
     */
    public void setDistortionScale(float value) {
        distortionScale  = value;
        material.setFloat("distortionScale", distortionScale);
    }

    public ColorRGBA getWaterColor() {
        return waterColor;
    }
    
    public void setWaterColor(ColorRGBA color) {
        this.waterColor = color;
        material.setColor("waterColor", color);
    }

    /**
     * Sets how the normal and dudv map are mixed to create the wave effect, default = 0.5
     * @param value
     */
    public void setDistortionMix(float value) {
        distortionMix = value;
        material.setFloat("distortionMix", distortionMix);
    }

    /**
     * Sets the scale of the normal/dudv texture, default = 1.
     * Note that the waves should be scaled by the texture coordinates of the quad to avoid animation artifacts,
     * use mesh.scaleTextureCoordinates(Vector2f) for that.
     */
    public void setTexScale(float value) {
        texScale = value;
        material.setFloat("texScale", texScale);
    }

    /**
     * returns the scale of distortion by the normal map, default = 0.2
     *
     * @return the distortion scale
     */
    public float getDistortionScale() {
        return distortionScale;
    }

    /**
     * returns how the normal and dudv map are mixed to create the wave effect,
     * default = 0.5
     *
     * @return the distortion mix
     */
    public float getDistortionMix() {
        return distortionMix;
    }

    /**
     * returns the scale of the normal/dudv texture, default = 1. Note that the
     * waves should be scaled by the texture coordinates of the quad to avoid
     * animation artifacts, use mesh.scaleTextureCoordinates(Vector2f) for that.
     *
     * @return the textures scale
     */
    public float getTexScale() {
        return texScale;
    }


    /**
     * retruns true if the waterprocessor is in debug mode
     * @return
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * set to true to display reflection and refraction textures in the GUI for debug purpose
     * @param debug
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Creates a quad with the water material applied to it.
     * @param width
     * @param height
     * @return
     */
    public Geometry createWaterGeometry(float width, float height) {
        Quad quad = new Quad(width, height);
        Geometry geom = new Geometry("WaterGeometry", quad);
        geom.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        geom.setMaterial(material);
        return geom;
    }

    /**
     * returns the reflection clipping plane offset
     * @return
     */
    public float getReflectionClippingOffset() {
        return reflectionClippingOffset;
    }

    /**
     * sets the reflection clipping plane offset
     * set a nagetive value to lower the clipping plane for relection texture rendering.
     * @param reflectionClippingOffset
     */
    public void setReflectionClippingOffset(float reflectionClippingOffset) {
        this.reflectionClippingOffset = reflectionClippingOffset;
        updateClipPlanes();
    }

    public void setFoamMap(String foamTexture) {
        Texture2D foam = (Texture2D) manager.loadTexture(foamTexture);
        foam.setWrap(WrapMode.MirroredRepeat);
        material.setTexture("foamMap", foam);
    }
    
    public void setFoamMaskMap(String foamMaskTexture) {
        Texture2D foamMask = (Texture2D) manager.loadTexture(foamMaskTexture);
        material.setTexture("foamMaskMap", foamMask);
    }
    
    public final void setFoamScale(float xScale, float yScale) {
        material.setVector2("foamScale", new Vector2f(xScale,  yScale));
    }
    
    public final void setFoamMaskScale(float xScale, float yScale) {
        material.setVector2("foamMaskScale", new Vector2f(xScale,  yScale));
    }

}

