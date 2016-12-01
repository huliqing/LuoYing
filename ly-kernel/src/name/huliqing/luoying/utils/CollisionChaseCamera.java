/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.utils;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.input.CameraInput;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.TouchInput;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.util.TempVars;

/**
 * 可防止穿墙的相机,当该相机与场景中带”物理特性“的物体产生碰撞时，相机会拉近与被跟随物体的距离，
 * 来防止穿过被碰撞物体。使用时需要给相机设置物理空间，使用示例：
 * <pre>
 * <code>
 * CollisionChaseCamera ccc = new CollisionChaseCamera(app.getCamera(), app.getInputManager());
 * ccc.setPhysicsSpace(xyz.getPhysicsSpace());
 * ccc.setChase(spatial);
 * </code>
 * </pre>
 * @author huliqing
 */
public class CollisionChaseCamera extends ChaseCamera 
        implements PhysicsCollisionListener, com.jme3.input.controls.TouchListener{
//    private final static Logger LOG = Logger.getLogger(CollisionChaseCamera.class.getName());
    
    // 相机旋转;
    private float tempRotationSpeed = rotationSpeed;

    // 为提高性能,特殊使用collisionChecker来跟踪相机的位置，当检测到该
    // 物体与碰撞组所在的物体列表（collisionObjects）发生碰撞时，才启动相机防穿墙检测。避免相机频繁使用光线
    // 检测碰撞的大量性能开销。
    private Spatial collisionChecker;
    
    // 物理空间
    private PhysicsSpace physicsSpace;
    
    // 以下为相机穿墙问题处理，使用光线检测
    private final CollisionResults collisionResults = new CollisionResults();
    // 射线的起始位置的偏移,在Y轴上加大一点可防立光线一开始就与地面发生碰撞交叉。
    // 避免误差
    private final Vector3f collisionRayOriginOffset = new Vector3f(0, 1f, 0);
    
    // 允许自动调整相机与目标的最近距离
    // 如果距离太近的话，角色会出现“空洞”的现象
    private float collisionNearDistanceLimit = 2;
    private float collisionNearDistanceLimitSquared = 4;
    
    // 用于处理在JME3.1后不能在Android环境下自动缩放镜头的问题 
    private final static String TOUCH_SCALE_EVENT = "TouchSE";
    
    // 当前和相机发生碰撞的物体
    private Spatial collisionTarget;
    private final Ray ray = new Ray();
    
    public CollisionChaseCamera(Camera cam, InputManager inputManager) {
        super(cam, inputManager);

        setCollisionBox(new Box(2.5f, 0.5f, 2.5f));

        // 用于提供在移动平台下双手指缩放调整镜头远近的事件监听
        registerTouchListener(inputManager);
    }
    
    /**
     * 射线的起始位置的偏移,在Y轴上加大一点可防立光线一开始就与地面发生碰撞交叉。避免误差
     * @param collisionRayOriginOffset 
     */
    public void setCollisionRayOriginOffset(Vector3f collisionRayOriginOffset) {
        this.collisionRayOriginOffset.set(collisionRayOriginOffset);
    }

    /**
     * 设置相机与目标的允许的最近距离
     * @param collisionNearDistanceLimit 
     */
    public void setCollisionNearDistanceLimit(float collisionNearDistanceLimit) {
        this.collisionNearDistanceLimit = collisionNearDistanceLimit;
        this.collisionNearDistanceLimitSquared = collisionNearDistanceLimit * collisionNearDistanceLimit;
    }

    /**
     * 设置物理空间,如果space为null则清理物理空间。
     * @param space 
     */
    public void setPhysicsSpace(PhysicsSpace space) {
        // 移除旧的
        if (this.physicsSpace != null) {
            this.physicsSpace.removeCollisionListener(this);
        }
        this.physicsSpace = space;
        if (this.physicsSpace != null) {
            this.physicsSpace.add(collisionChecker);
            this.physicsSpace.addCollisionListener(this);
        }
    }
    
    /**
     * 设置一个BOX用于作为相机碰撞检测的物体。
     * @param box 
     */
    public final void setCollisionBox(Box box) {
        // 移除旧的
        if (collisionChecker != null) {
            if (physicsSpace != null) {
                physicsSpace.remove(collisionChecker);
            }
            collisionChecker.removeFromParent();
        }
        
        // 重建新的碰撞检测
        collisionChecker = new Geometry("camCollisionChecker", box);
        collisionChecker.setMaterial(MaterialUtils.createWireFrame());
        collisionChecker.setCullHint(Spatial.CullHint.Always);
        
        // GhostControl
        // 给物理体设置一个不同的碰撞组,这会优化碰撞检测性能.
        GhostControl gc = new GhostControl(CollisionShapeFactory.createBoxShape(collisionChecker));
        collisionChecker.addControl(gc);
    }

    @Override
    public void setRotationSpeed(float rotationSpeed) {
        super.setRotationSpeed(rotationSpeed); 
        this.tempRotationSpeed = rotationSpeed;
    }
    
    /**
     * 开关相机的旋转功能
     * @param bool 
     */
    public void setEnabledRotation(boolean bool) {
        if (!bool) {
            super.setRotationSpeed(0);
        } else {
            super.setRotationSpeed(tempRotationSpeed);
        }
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        
        if (collisionChecker.getParent() == null) {
            if (target != null) {
                Spatial rootParent = getRoot(target);
                if (rootParent instanceof Node) {
                    ((Node) rootParent).attachChild(collisionChecker);
                }
            }
        }
        
        // 1.让collisionChecker与相机位置保持一致.
        Vector3f loc = collisionChecker.getLocalTranslation();
        Vector3f camLoc = cam.getLocation();
        if (FastMath.abs(loc.x - camLoc.x) > 0.0001f 
                || FastMath.abs(loc.y - camLoc.y) > 0.0001f 
                || FastMath.abs(loc.z - camLoc.z) > 0.0001f) {
            collisionChecker.setLocalTranslation(cam.getLocation());
        }
        
        // 2.防止相机穿墙，当collisionTarget不为null时说明相机已经与某些物体发生碰撞，这时需要偿试调整相机的位置。
        if (collisionTarget != null) {
//            LOG.log(Level.INFO, "CollisionChaseCamera, Need to fix collision with={0}", new Object[] {collisionTarget});

            // fixingCameraDistance方法用于拉近相机，以避免穿墙，这是一个持续的过程，如果该方法返回true,则说明正在持续
            // 修正（拉近）相机距离，这时不能释放collisionTarget, 因为该方法的修正会在下一帧被ChaseCamera的默认行为重置，
            // 所以这个方法必须持续进行，直到相机不产生碰撞才能释放。
            if (fixingCameraDistance(collisionTarget)) {
                collisionChecker.setLocalTranslation(cam.getLocation());
            } else {
                // 当该方法返回false时，说明相机已经不会碰撞到其它物体，则可
                // 释放collisionTarget,即不再需要修正距离。
                collisionTarget = null;
            }
        }
    }
    
    private boolean isChaseTarget(Spatial collisionObject) {
        if (collisionObject == target) {
            return true;
        }
        return collisionObject != null && isChaseTarget(collisionObject.getParent());
    }
    
    // fixingCameraDistance方法用于拉近相机，以避免穿墙，这是一个持续的过程，如果该方法返回true,则说明正在持续
    // 修正（拉近）相机距离，这时不能释放collisionTarget, 因为该方法的修正会在下一帧被ChaseCamera的默认行为重置，
    // 所以这个方法必须持续进行，直到相机不产生碰撞才能释放。
    private boolean fixingCameraDistance(Spatial collisionObject) {
        // 如果相机碰撞的是被跟随的对象本身，则不要做任何处理。因为当相机拉近目标时可能会产生持续的碰撞，这个时候可能
        // 相机与被跟随目标的距离比nearDistanceLimit还近，如果把相机反向往远处（nearDistanceLimit）拉，则可能返而导致
        // 相机穿过其它障碍物，比如当被跟随目标离墙壁很近时，如果相机与被跟随目标这时产生碰撞，
        // 当相机往回拉到nearDistanceLimit时可能会穿墙。
        if (isChaseTarget(collisionObject)) {
            return false;
        }
        
        TempVars tv = TempVars.get();
        Vector3f origin = tv.vect1;
        Vector3f dir = tv.vect2;
        origin.set(target.getWorldTranslation()).addLocal(collisionRayOriginOffset);
        dir.set(cam.getDirection()).negateLocal().normalizeLocal();
        boolean fixing = false;
        collisionResults.clear();
        collideWith(origin, dir, collisionObject, collisionResults);
        if (collisionResults.size() > 0) {
            Vector3f closest = collisionResults.getClosestCollision().getContactPoint();
            //目标与相机距离
            float distanceToCamera = origin.distanceSquared(cam.getLocation());
            float distanceToCollisionObject = origin.distanceSquared(closest);
            // 挡在相机中间的物体
            if (distanceToCollisionObject <= distanceToCamera) {
                if (distanceToCollisionObject < collisionNearDistanceLimitSquared) {
                    closest.set(origin).addLocal(dir.mult(collisionNearDistanceLimit, tv.vect6));
                }
                closest.addLocal(lookAtOffset).subtractLocal(collisionRayOriginOffset);
                cam.setLocation(closest);
                fixing = true;
            }
        }
        tv.release();
        
        return fixing;
    }
    
    private CollisionResults collideWith(Vector3f origin, Vector3f direction, Spatial root, CollisionResults store) {
        ray.setOrigin(origin);
        ray.setDirection(direction);
        root.collideWith(ray, store);
        return store;
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
        if (event.getNodeA()  == collisionChecker) {
            collisionTarget = event.getNodeB();
//            LOG.log(Level.INFO, "CollisionChaseCamera,Collision target(nodeB)={0}", event.getNodeB());
        } else if (event.getNodeB() == collisionChecker) {
            collisionTarget = event.getNodeA();
//            LOG.log(Level.INFO, "CollisionChaseCamera,Collision target(nodeA)={0}", event.getNodeB());
        }
        
    }
    
    private Spatial getRoot(Spatial spatial) {
        if (spatial.getParent() != null) {
            return getRoot(spatial.getParent());
        }
        return spatial;
    }
    
    /**
     * 让相机跟随指定物体
     * @param spatial 
     */
    public void setChase(Spatial spatial) {
        // 先移除原来的相机跟随
        if (this.target != null) {
            CollisionChaseCamera gcc = target.getControl(CollisionChaseCamera.class);
            if (gcc != null) {
                this.target.removeControl(gcc);
            }
        }
        spatial.addControl(this);
        // 因为变换了chase对象，所以collisionChecker需要重新加载到场景中
        collisionChecker.removeFromParent();
    }
    
    /**
     * 清理相机数据，清理后相机将不再可用。
     */
    public void cleanup() {
        // 1.移除控制器
        if (target != null) {
            target.removeControl(this);
        }
        
        // 2.移除用于防穿墙的碰撞检测特性
        if (collisionChecker != null) {
            GhostControl gc = collisionChecker.getControl(GhostControl.class);
            if (gc != null) {
                collisionChecker.removeControl(gc);
            }
            collisionChecker.removeFromParent();
            collisionChecker = null;
        }
        if (physicsSpace != null) {
            physicsSpace.removeCollisionListener(this);
            physicsSpace = null;
        }
        
        // 3.移除父类ChaseCamera中添加的监听
        String[] inputs = {CameraInput.CHASECAM_TOGGLEROTATE,
            CameraInput.CHASECAM_DOWN,
            CameraInput.CHASECAM_UP,
            CameraInput.CHASECAM_MOVELEFT,
            CameraInput.CHASECAM_MOVERIGHT,
            CameraInput.CHASECAM_ZOOMIN,
            CameraInput.CHASECAM_ZOOMOUT};
        for (String s : inputs) {
            inputManager.deleteMapping(s);
        }
        
        // 4.ChaseCamera中自定义的监听
        inputManager.deleteMapping(TOUCH_SCALE_EVENT);
        inputManager.deleteMapping(TOUCH_SCALE_EVENT);
    }
    
    private void registerTouchListener(InputManager im) {
        inputManager.addMapping(TOUCH_SCALE_EVENT, new com.jme3.input.controls.TouchTrigger(TouchInput.ALL));
        inputManager.addListener(this, TOUCH_SCALE_EVENT); // TouchSE => TouchScaleEvent
    }

    @Override
    public void onTouch(String name, TouchEvent event, float tpf) {
        if (event.getType() == TouchEvent.Type.SCALE_MOVE && TOUCH_SCALE_EVENT.equals(name)) {
            
            float dss = event.getDeltaScaleSpan();
            // 缩放太小就不处理了，否则有可能两个手指放上去时画面一直在抖动。
            if (FastMath.abs(dss) < 0.01f) {
                return;
            }

            // 缩放镜头: * 0.25 降低一点缩放强度
            zoomCamera(-dss * 0.1f);
            if (dss > 0) {
                // 拉近，放大 (参考：ChaseCamera中onAnalog方法)
                if (zoomin == false) {
                    distanceLerpFactor = 0;
                }
                zoomin = true;
            } else {
                // 拉远，缩小
                if (zoomin == true) {
                    distanceLerpFactor = 0;
                }
                zoomin = false;
            }
        }
    }
    
    /**
     * 获取相机
     * @return 
     */
    public Camera getCamera() {
        return this.cam;
    }
    
    /**
     * 获取被跟随的物体，如果目标不存在，则返回null.
     * @return 
     */
    public Spatial getChase() {
        return target;
    }

}
