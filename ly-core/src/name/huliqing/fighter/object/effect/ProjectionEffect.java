/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.effect;

import com.jme3.app.Application;
import com.jme3.bounding.BoundingVolume;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.Camera.FrustumIntersect;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.MaterialConstants;
import name.huliqing.fighter.constants.TextureConstants;
import name.huliqing.fighter.data.EffectData;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.utils.GeometryUtils;

/**
 * 投射类的效果,这个效果可以把一个材质贴图投射到指定的物体上。注意：每次效果
 * 结束后都需要重新添加接受投射的物体。
 * @author huliqing
 */
public class ProjectionEffect extends AbstractEffect {
    private final PlayService playService = Factory.get(PlayService.class);

    // 需要投射的材质贴图
    private Texture texture;
    private ColorRGBA color;
    
    // 投射的源位置
    private final Vector3f projPos = new Vector3f(0, 0, 0);
    // 投射方向,默认向下
    private final Vector3f projDir = new Vector3f(0,-1,0);
    // 投射时的up方向，注意：不要把up方向设置和投射方向平行，否则投射会存在一些变形.
    private final Vector3f projUp = new Vector3f(0,0,-1);

    // ---- inner
    // 用于计算投射的摄像机
    private Camera castCam;
    private RenderManager rm;
    private ViewPort vp;
    
    // 用于包装，控制投射的Geo
    private Material projMat;
    // 投射体，用于各种检查并控制投射方向
    private Geometry projGeo;
    // 用于debug
    private Geometry debugGeo;
    // 是否打开debug
    private boolean debug;
    // 用于临时计算用,避免重复创建对象
    private final Vector3f tempProjConer = new Vector3f();
    // 所有接受投射的物体,只有这些物体有可能会接受到投射。
    private final List<Geometry> receivers = new ArrayList<Geometry>();
    // 所有用于渲染的物体,因为并不是所有receivers都会受到投射，比如一些太远
    // 或隐藏的物体会剔除，所以availableReceivers是receivers的子集，并且每帧都会
    // 进行重新计算，以确定哪些可接受到投射。
    private final List<Geometry> availableReceivers = new ArrayList<Geometry>(5);
    // 用于处理和渲染被投射物体
    private final ProjectionSceneProcessor processor = new ProjectionSceneProcessor();

    @Override
    public void initData(EffectData data) {
        super.initData(data); 
        // TextureConstants.TEX_MAGIC default value for test.
        texture = Common.getAssetManager().loadTexture(data.getAttribute("texture", TextureConstants.TEX_MAGIC));
        color = data.getAsColor("color");
        projPos.set(data.getAsVector3f("projPos", projPos));
        projDir.set(data.getAsVector3f("projDir", projDir)).normalizeLocal();
        projUp.set(data.getAsVector3f("projUp", projUp)).normalizeLocal();
        debug = data.getAsBoolean("debug", false);
        
        // ---- 初始化，这些是固定的，不会动态改变
        castCam = new Camera(1, 1);
        castCam.setParallelProjection(true);
        castCam.setFrustum(-1, 1, -1, 1, 1, -1);
        
        projMat = new Material(Common.getAssetManager(), MaterialConstants.MAT_PROJECTION);
        projMat.setTexture("Texture", texture);
        // 注：初始贴图颜色，如果效果包含有ColorAnim,则这个Color设置会受影响。
        if (color != null) {
            projMat.setColor("Color", color);
        }
 
        // 创建单位Box来作为投射计算和检测,这个检查体决定了是否进行投射。当这个
        // 检测体在视角范围内以及与被投射体相交时(使用BoundingVolume检测)，才进行
        // 投射渲染。以避免浪费资源。
        // 根据这个方式，有可能投射出来的效果并不在projGeo这个包含围盒内,所以尽量
        // 让这个检测体靠近或包围住想要被投射的物体的所在面，这样看起来会比较合理。
        projGeo = new Geometry("ProjGeo", new Box(0.5f, 0.5f, 0.5f));
        projGeo.setCullHint(Spatial.CullHint.Always);
        
        // 把projMat设置到当前effect中主要是为了使各种AnimColor动画能够控制这个
        // 效果的颜色,当effect存在AnimColor的时候，AnimColor会在运行时去查找被
        // 控制节点下的所有material,并动态控制这些material的颜色属性。
        
        // 注：材质必须设置到Geometry中去，如果是给节点(Node)添加设置材质,则必须
        // "先确认"节点下有子Geometry,否则setMaterial无效。
        projGeo.setMaterial(projMat);
        localRoot.attachChild(projGeo);
    }
    
    public void addReceiver(Spatial spatial) {
        if (spatial instanceof Geometry) {
            Geometry geo = (Geometry) spatial;
            if (!receivers.contains(geo)) {
                receivers.add(geo);
            }
        } else if (spatial instanceof Node) {
            List<Geometry> geos = GeometryUtils.findAllGeometry(spatial);
            for (Geometry geo : geos) {
                if (!receivers.contains(geo)) {
                    receivers.add(geo);
                }
            }
        }
    }

    @Override
    protected void doInit() {
        super.doInit();
        // 把SceneProcessor加入场景处理器列表，注意在cleanup时要清理掉，避免浪费资源
        Application app = playService.getApplication();
        if (!app.getViewPort().getProcessors().contains(processor)) {
            app.getViewPort().addProcessor(processor);
        }
        
        // 判断是否有接受投射的物体，如果没有的话偿试把地形添加进来。
        if (receivers.isEmpty()) {
            Spatial terrain = playService.getTerrain();
            if (terrain != null) {
                addReceiver(terrain);
            }
        }
    }
    
    // 如果projGeo检查体不在视角范围内则不渲染投射
    private boolean isVisiableProj() {
        FrustumIntersect fi = vp.getCamera().contains(projGeo.getWorldBound());
        return fi != FrustumIntersect.Outside;
    }

    private void getAvailableReceiversForRender(List<Geometry> store) {
        BoundingVolume bv = projGeo.getWorldBound();
        int size = receivers.size();
        for (int i = 0; i < size; i++) {
            if (receivers.get(i).getCullHint() != CullHint.Always 
                    && receivers.get(i).getWorldBound().intersects(bv)) {
                store.add(receivers.get(i));
            }
        }
    }
    
    private Material getDebugMaterial() {
        Material debugMat = new Material(Common.getAssetManager(), MaterialConstants.COMMON_UNSHADED);
        debugMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        debugMat.getAdditionalRenderState().setWireframe(true);
        return debugMat;
    }
    
    @Override
    public void cleanup() {
        // 注：这里要清理所有引用,所以每次重用这个效果时都需要重新添加接受投射的物体
        receivers.clear();
        // 移除处理器
        playService.getApplication().getViewPort().removeProcessor(processor);
        super.cleanup();
    }
    
    // -------------------------------------------------------------------------
    // 渲染投射处理器
    // -------------------------------------------------------------------------
    private class ProjectionSceneProcessor implements SceneProcessor {
        private boolean initialized;
        @Override
        public void initialize(RenderManager _rm, ViewPort _vp) {
            rm = _rm;
            vp = _vp;
            initialized = true;
        }

        @Override
        public boolean isInitialized() {
            return initialized;
        }

        @Override
        public void reshape(ViewPort vp, int w, int h) {}
        @Override
        public void preFrame(float tpf) {}
        @Override
        public void postQueue(RenderQueue rq) {}

        @Override
        public void postFrame(FrameBuffer out) {
            if (debug) {
                if (debugGeo == null) {
                    // 注意：clone要把CullHint打开，因为projGeo默认是“隐藏”的
                    debugGeo = projGeo.clone();
                    debugGeo.setCullHint(CullHint.Never);
                    debugGeo.setMaterial(getDebugMaterial());
                    localRoot.attachChild(debugGeo);
                }
                debugGeo.setLocalTransform(projGeo.getLocalTransform());
            }

            // 如果ProjGeo不在可视范围内则
            if (!isVisiableProj()) {
                if (Config.debug) {
                    Logger.getLogger(name.huliqing.fighter.object.effect.ProjectionEffect.class.getName()).log(Level.INFO
                            , "ProjectionEffect: projGeo not visiable, do not projection!");
                }
                return;
            }

            // 检查是否有可用的被投射目标
            availableReceivers.clear();
            getAvailableReceiversForRender(availableReceivers);
            if (availableReceivers.size() <= 0) {
                if (Config.debug) {
                    Logger.getLogger(name.huliqing.fighter.object.effect.ProjectionEffect.class.getName()).log(Level.INFO
                                , "ProjectionEffect: no availableReceivers! skip render.");
                }
                return;
            }
//            if (Config.debug) {
//                Logger.getLogger(name.huliqing.fighter.object.effect.ProjectionEffect.class.getName()).log(Level.INFO
//                        , "ProjectionEffect: availableReceivers={0}, all={1}"
//                        , new Object[]{availableReceivers.size(), receivers.size()});
//            }

            projGeo.setLocalTranslation(projPos);
            projGeo.getLocalRotation().lookAt(projDir, projUp);

            castCam.setLocation(projGeo.getWorldTranslation());
            castCam.setRotation(projGeo.getWorldRotation());
            castCam.update();
            castCam.updateViewProjection();

            tempProjConer.set(projGeo.getWorldTranslation());
            castCam.getViewProjectionMatrix().mult(tempProjConer, tempProjConer);

            float tw = projGeo.getWorldScale().x;
            float th = projGeo.getWorldScale().y;

            projMat.setFloat("ProjLeftX", (tempProjConer.x - tw * 0.5f));
            projMat.setFloat("ProjLeftY", (tempProjConer.y - th * 0.5f));
            projMat.setFloat("ProjWidth", tw);
            projMat.setFloat("ProjHeight", th);
            projMat.setMatrix4("CastViewProjectionMatrix", castCam.getViewProjectionMatrix());

            // 强制指定材质进行渲染
            rm.setForcedMaterial(projMat);
            
            // remove20160603,JME3.1后不能再设置Default,会导致无法正确渲染，这里直接清除掉就可以,让它自动选择Technique
//            rm.setForcedTechnique("Default"); 
            rm.setForcedTechnique(null);

            for (Geometry geo : availableReceivers) {
                rm.renderGeometry(geo);
            }
            // 清除复原
            rm.setForcedMaterial(null);
            rm.setForcedTechnique(null);
        }

        @Override
        public void cleanup() {
            initialized = false;
        }
    }
}
