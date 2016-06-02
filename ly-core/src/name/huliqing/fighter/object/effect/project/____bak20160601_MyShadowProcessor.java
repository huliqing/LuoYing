///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.effect.project;
//
//import com.jme3.asset.AssetManager;
//import com.jme3.material.Material;
//import com.jme3.math.ColorRGBA;
//import com.jme3.math.Matrix4f;
//import com.jme3.math.Vector3f;
//import com.jme3.post.SceneProcessor;
//import com.jme3.renderer.Camera;
//import com.jme3.renderer.Caps;
//import com.jme3.renderer.RenderManager;
//import com.jme3.renderer.Renderer;
//import com.jme3.renderer.ViewPort;
//import com.jme3.renderer.queue.GeometryList;
//import com.jme3.renderer.queue.OpaqueComparator;
//import com.jme3.renderer.queue.RenderQueue;
//import com.jme3.scene.Geometry;
//import com.jme3.scene.Spatial;
//import com.jme3.scene.debug.WireFrustum;
//import com.jme3.shadow.CompareMode;
//import com.jme3.shadow.EdgeFilteringMode;
//import com.jme3.texture.FrameBuffer;
//import com.jme3.texture.Image;
//import com.jme3.texture.Texture;
//import com.jme3.texture.Texture2D;
//import com.jme3.ui.Picture;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// *
// */
//public abstract class MyShadowProcessor implements SceneProcessor {
//
//    protected int nbShadowMaps = 1;
//    protected float shadowMapSize;
//    protected float shadowIntensity = 0.7f;
//    protected RenderManager renderManager;
//    protected ViewPort viewPort;
//    protected FrameBuffer[] shadowFB;
//    protected Texture2D[] shadowMaps;
//    protected Texture2D dummyTex;
//    protected Material preshadowMat;
//    protected Material postshadowMat;
//    protected Matrix4f[] lightViewProjectionsMatrices;
//    protected AssetManager assetManager;
//    protected float edgesThickness = 1.0f;
//    protected EdgeFilteringMode edgeFilteringMode = EdgeFilteringMode.Bilinear;
//    protected CompareMode shadowCompareMode = CompareMode.Hardware;
//    protected boolean flushQueues = true;
//    
//    protected String postTechniqueName = "Default";
//    
//    /**
//     * list of materials for post shadow queue geometries
//     */
//    protected List<Material> matCache = new ArrayList<Material>();
//    protected GeometryList sceneReceivers;
//    protected GeometryList lightReceivers = new GeometryList(new OpaqueComparator());
//    protected GeometryList shadowMapOccluders = new GeometryList(new OpaqueComparator());
//    private String[] shadowMapStringCache;
//    private String[] lightViewStringCache;
//    
//    /**
//     * true to skip the post pass when there are no shadow casters
//     */
//    protected boolean skipPostPass;
//    
//    public MyShadowProcessor() {}
//    
//    public MyShadowProcessor(AssetManager assetManager, int shadowMapSize, int nbShadowMaps) {
//        this.assetManager = assetManager;
//        this.nbShadowMaps = nbShadowMaps;
//        this.shadowMapSize = shadowMapSize;
//        init(assetManager, nbShadowMaps, shadowMapSize);
//    }
//    
//    private void init(AssetManager assetManager, int nbShadowMaps, int shadowMapSize) {
//        this.postshadowMat = new Material(assetManager, "MatDefs/Projection/Projection.j3md");
//        shadowFB = new FrameBuffer[nbShadowMaps];
//        shadowMaps = new Texture2D[nbShadowMaps];
//        lightViewProjectionsMatrices = new Matrix4f[nbShadowMaps];
//        shadowMapStringCache = new String[nbShadowMaps];
//        lightViewStringCache = new String[nbShadowMaps];
//
//        //DO NOT COMMENT THIS (it prevent the OSX incomplete read buffer crash)
//        dummyTex = new Texture2D(shadowMapSize, shadowMapSize, Image.Format.RGBA8);
//
//        preshadowMat = new Material(assetManager, "MatDefs/Projection/Projection.j3md");
//        postshadowMat.setFloat("ShadowMapSize", shadowMapSize);
//
//        for (int i = 0; i < nbShadowMaps; i++) {
//            lightViewProjectionsMatrices[i] = new Matrix4f();
//            shadowFB[i] = new FrameBuffer(shadowMapSize, shadowMapSize, 1);
//            
//            shadowMaps[i] = new Texture2D(shadowMapSize, shadowMapSize, Image.Format.Depth);
//            shadowFB[i].setDepthTexture(shadowMaps[i]);
//
//            //DO NOT COMMENT THIS (it prevent the OSX incomplete read buffer crash)
//            shadowFB[i].setColorTexture(dummyTex);
//            shadowMapStringCache[i] = "ShadowMap" + i; 
//            lightViewStringCache[i] = "LightViewProjectionMatrix" + i;
//
//            postshadowMat.setTexture(shadowMapStringCache[i], shadowMaps[i]);
//        }
//
//        setShadowCompareMode(shadowCompareMode);
//        setEdgeFilteringMode(edgeFilteringMode);
//        setShadowIntensity(shadowIntensity);
//    }
//    
//
//    /**
//     * Sets the filtering mode for shadow edges. See {@link EdgeFilteringMode}
//     * for more info.
//     *
//     * @param filterMode the desired filter mode (not null)
//     */
//    final public void setEdgeFilteringMode(EdgeFilteringMode filterMode) {
//        if (filterMode == null) {
//            throw new NullPointerException();
//        }
//
//        this.edgeFilteringMode = filterMode;
//        postshadowMat.setInt("FilterMode", filterMode.getMaterialParamValue());
//        postshadowMat.setFloat("PCFEdge", edgesThickness);
//        if (shadowCompareMode == CompareMode.Hardware) {
//            for (Texture2D shadowMap : shadowMaps) {
//                if (filterMode == EdgeFilteringMode.Bilinear) {
//                    shadowMap.setMagFilter(Texture.MagFilter.Bilinear);
//                    shadowMap.setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
//                } else {
//                    shadowMap.setMagFilter(Texture.MagFilter.Nearest);
//                    shadowMap.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
//                }
//            }
//        }
//    }
//
//    /**
//     * Sets the shadow compare mode. See {@link CompareMode} for more info.
//     *
//     * @param compareMode the desired compare mode (not null)
//     */
//    final public void setShadowCompareMode(CompareMode compareMode) {
//        if (compareMode == null) {
//            throw new IllegalArgumentException("Shadow compare mode cannot be null");
//        }
//
//        this.shadowCompareMode = compareMode;
//        for (Texture2D shadowMap : shadowMaps) {
//            if (compareMode == CompareMode.Hardware) {
//                shadowMap.setShadowCompareMode(Texture.ShadowCompareMode.LessOrEqual);
//                if (edgeFilteringMode == EdgeFilteringMode.Bilinear) {
//                    shadowMap.setMagFilter(Texture.MagFilter.Bilinear);
//                    shadowMap.setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
//                } else {
//                    shadowMap.setMagFilter(Texture.MagFilter.Nearest);
//                    shadowMap.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
//                }
//            } else {
//                shadowMap.setShadowCompareMode(Texture.ShadowCompareMode.Off);
//                shadowMap.setMagFilter(Texture.MagFilter.Nearest);
//                shadowMap.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
//            }
//        }
//        postshadowMat.setBoolean("HardwareShadows", compareMode == CompareMode.Hardware);
//    }
//
//
//    /**
//     * Initialize this shadow renderer prior to its first update.
//     *
//     * @param rm the render manager
//     * @param vp the viewport
//     */
//    @Override
//    public void initialize(RenderManager rm, ViewPort vp) {
//        renderManager = rm;
//        viewPort = vp;
//    }
//
//    @Override
//    public boolean isInitialized() {
//        return viewPort != null;
//    }
//
//    /**
//     * Invoked once per frame to update the shadow cams according to the light
//     * view.
//     * 
//     * @param viewCam the scene cam
//     */
//    protected abstract void updateShadowCams(Camera viewCam);
//
//    /**
//     * Returns a subclass-specific geometryList containing the occluders to be
//     * rendered in the shadow map
//     *
//     * @param shadowMapIndex the index of the shadow map being rendered
//     * @param sceneOccluders the occluders of the whole scene
//     * @param sceneReceivers the receivers of the whole scene
//     * @param shadowMapOcculders
//     * @return
//     */
//    protected abstract GeometryList getOccludersToRender(int shadowMapIndex, GeometryList sceneOccluders, GeometryList sceneReceivers, GeometryList shadowMapOccluders);
//
//    /**
//     * return the shadow camera to use for rendering the shadow map according
//     * the given index
//     *
//     * @param shadowMapIndex the index of the shadow map being rendered
//     * @return the shadowCam
//     */
//    protected abstract Camera getShadowCam(int shadowMapIndex);
//
//    /**
//     * responsible for displaying the frustum of the shadow cam for debug
//     * purpose
//     *
//     * @param shadowMapIndex
//     */
//    protected void doDisplayFrustumDebug(int shadowMapIndex) {
//    }
//
//    @SuppressWarnings("fallthrough")
//    @Override
//    public void postQueue(RenderQueue rq) {
//        GeometryList occluders = rq.getShadowQueueContent(RenderQueue.ShadowMode.Cast);
//        sceneReceivers = rq.getShadowQueueContent(RenderQueue.ShadowMode.Receive);
//        skipPostPass = false;
//        // 如果场景中没有阴影接收者或没有阴影投影者，则超过阴影处理器
//        if (sceneReceivers.size() == 0 || occluders.size() == 0) {
//            skipPostPass = true;
//            return;
//        }
//
//        updateShadowCams(viewPort.getCamera());
//
//        Renderer r = renderManager.getRenderer();
//        renderManager.setForcedMaterial(preshadowMat);
////        renderManager.setForcedTechnique("PreShadow");
//        renderManager.setForcedTechnique("Default");
//
//        for (int shadowMapIndex = 0; shadowMapIndex < nbShadowMaps; shadowMapIndex++) {
//            renderShadowMap(shadowMapIndex, occluders, sceneReceivers);
//        }
//        
//        if (flushQueues) {
//            occluders.clear();
//        }
//        //restore setting for future rendering
//        r.setFrameBuffer(viewPort.getOutputFrameBuffer());
//        renderManager.setForcedMaterial(null);
//        renderManager.setForcedTechnique(null);
//        renderManager.setCamera(viewPort.getCamera(), false);
//
//    }
//
//    protected void renderShadowMap(int shadowMapIndex, GeometryList occluders, GeometryList receivers) {
//        shadowMapOccluders = getOccludersToRender(shadowMapIndex, occluders, receivers, shadowMapOccluders);
//        Camera shadowCam = getShadowCam(shadowMapIndex);
//
//        //saving light view projection matrix for this split            
//        lightViewProjectionsMatrices[shadowMapIndex].set(shadowCam.getViewProjectionMatrix());
//        renderManager.setCamera(shadowCam, false);
//
//        renderManager.getRenderer().setFrameBuffer(shadowFB[shadowMapIndex]);
//        renderManager.getRenderer().clearBuffers(false, true, false);
//
//        // render shadow casters to shadow map
//        viewPort.getQueue().renderShadowQueue(shadowMapOccluders, renderManager, shadowCam, true);
//    }
//
//    abstract GeometryList getReceivers(GeometryList sceneReceivers, GeometryList lightReceivers);
//
//    @Override
//    public void postFrame(FrameBuffer out) {
//        if (skipPostPass) {
//            return;
//        }
//
//        lightReceivers = getReceivers(sceneReceivers, lightReceivers);
//
//        if (lightReceivers.size() != 0) {
//            
//            setMaterialParameters(postshadowMat);
//            for (int j = 0; j < nbShadowMaps; j++) {
//                postshadowMat.setMatrix4(lightViewStringCache[j], lightViewProjectionsMatrices[j]);
//                postshadowMat.setTexture(shadowMapStringCache[j], shadowMaps[j]);
//            }
//
//            
//            
//            Camera cam = viewPort.getCamera();
//            renderManager.setForcedMaterial(postshadowMat);
//            renderManager.setForcedTechnique(postTechniqueName);
//
//            //rendering the post shadow pass
//            viewPort.getQueue().renderShadowQueue(lightReceivers, renderManager, cam, false);
//            if (flushQueues) {
//                sceneReceivers.clear();
//            }
//
//            //resetting renderManager settings
//            renderManager.setForcedTechnique(null);
//            renderManager.setForcedMaterial(null);
//            renderManager.setCamera(cam, false);
//
//        }
//
//    }
//
//    /**
//     * This method is called once per frame and is responsible for setting any
//     * material parameters than subclass may need to set on the post material.
//     *
//     * @param material the material to use for the post shadow pass
//     */
//    protected abstract void setMaterialParameters(Material material);
//
//
//    @Override
//    public void preFrame(float tpf) {
//        
//    }
//
//    @Override
//    public void cleanup() {
//    }
//
//    @Override
//    public void reshape(ViewPort vp, int w, int h) {
//    }
//
//    /**
//     * Set the shadowIntensity. The value should be between 0 and 1. A 0 value
//     * gives a bright and invisible shadow, a 1 value gives a pitch black
//     * shadow. The default is 0.7
//     *
//     * @param shadowIntensity the darkness of the shadow
//     */
//    final public void setShadowIntensity(float shadowIntensity) {
//        this.shadowIntensity = shadowIntensity;
//        postshadowMat.setFloat("ShadowIntensity", shadowIntensity);
//    }
//
//
//}
