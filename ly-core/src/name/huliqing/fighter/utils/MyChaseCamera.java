/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.utils;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.CameraInput;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.TouchInput;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.util.TempVars;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.enums.DataType;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.state.PlayState;

/**
 * 可防止相机穿墙的相机。
 * @author huliqing
 */
public class MyChaseCamera extends ChaseCamera implements PhysicsCollisionListener, com.jme3.input.controls.TouchListener{
    private final static Logger logger = Logger.getLogger(MyChaseCamera.class.getName());
    
    private final PlayService playService = Factory.get(PlayService.class);
    
    // ============================ 以下处理相机旋转开关;
    private float tempRotationSpeed = rotationSpeed;

    // 相机与目标的特别碰撞组
    private final static int CAM_COLLISION_GROUP = 0x00000010;
    // ==== 为提高性能,特殊使用camCollisionChecker来跟踪相机的位置，当检测到该
    // 物体与地面发生碰撞时，才启动相机与地面的碰撞检测。避免相机频繁使用光线
    // 检测碰撞的大量性能开销。
    private Spatial camCollisionChecker;
    // 防止相机与目标相碰撞的目标object
    private PhysicsCollisionObject collisionObject;
    private PhysicsSpace physicsSpace;
    
    // 是否需要修正相机距离，当发现相机与地面发生碰撞时需要拉近相机，以避免相机
    // 穿越到地面下。
    private boolean needFixDistance;
    
    // ============================ 以下为相机穿墙问题处理，使用光线检测
    private CollisionResults collisionResults = new CollisionResults();
    // 射线的起始位置的偏移,在Y轴上加大一点可防立光线一开始就与地面发生碰撞交叉。
    // 避免误差
    private Vector3f rayOriginOffset = new Vector3f(0, 0.5f, 0);
    // 允许自动调整相机与目标的最近距离
    // 如果距离太近的话，角色会出现“空洞”的现象
    private float nearDistanceLimit = 2;
    
    // ---- 用于处理在JME3.1后不能在Android环境下自动缩放镜头的问题 
    private final static String TOUCH_SCALE_EVENT = "TouchSE";
    
    public MyChaseCamera(Camera cam
            , InputManager inputManager
            , PhysicsSpace physicsSpace
            ) {
        super(cam, inputManager);
            
        // 使用一个扁平的box来优化相机的碰撞检测,加大xz上的值可以避免相机在快速旋转的时候仍能看到地表背面的可能。
        // y向的值不能太大，以避免与地面的无限碰撞
        camCollisionChecker = new Geometry("camCollisionChecker", new Box(2.5f, 0.5f, 2.5f));
        camCollisionChecker.setMaterial(MatUtils.createWireFrame());
        camCollisionChecker.setCullHint(Spatial.CullHint.Always);
        GhostControl gc = new GhostControl(CollisionShapeFactory.createBoxShape(camCollisionChecker));
        camCollisionChecker.addControl(gc);

        playService.addObject(camCollisionChecker, false);

        // 给摄像机碰撞检测器和地面设置一个不同的碰撞组,这会优化碰撞检测性能.
        // 因为摄像机只需要检测是否与地面发生碰撞就可以,不需要与其它角色或物体进行
        // 碰撞检测,以提高性能.
        gc.setCollisionGroup(CAM_COLLISION_GROUP);
        gc.setCollideWithGroups(CAM_COLLISION_GROUP);

        resetCollision(physicsSpace, playService.getTerrain());
        
        // 用于提供在移动平台下双手指缩放调整镜头远近的事件监听
        registerTouchListener(inputManager);
    }
    
    /**
     * 设置一个让相机不会和它相碰撞的目标对象，一般可设置为地面或墙壁，这可
     * 避免相机穿墙。
     * @param physicsSpace
     * @param collisionTarget 
     */
    public final void resetCollision(PhysicsSpace physicsSpace, Spatial collisionTarget) {
        // remove old
        if (this.physicsSpace != null) {
            this.physicsSpace.removeCollisionListener(this);
            this.physicsSpace = null;
        }
        if (physicsSpace == null) {
            return;
        }
        // add new
        this.physicsSpace = physicsSpace;
        this.physicsSpace.addCollisionListener(this);
        
        // 清除掉的碰撞组
        if (collisionObject != null) {
            collisionObject.removeCollideWithGroup(CAM_COLLISION_GROUP);
        }
        // 重设新的碰撞组
        if (collisionTarget != null) {
            RigidBodyControl rbc = collisionTarget.getControl(RigidBodyControl.class);
            if (rbc != null) {
                rbc.addCollideWithGroup(CAM_COLLISION_GROUP);
                collisionObject = rbc; // 记住它，以便下次重设时清除
            }
        }
    }

    // ==============================
  
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
        
        // 1.让camCollisionChecker与相机位置保持一致
        camCollisionChecker.setLocalTranslation(cam.getLocation());
        
        // 2.防止相机穿墙
        if (needFixDistance) {
//            logger.log(Level.INFO, "needFixDistance=true");
            fixCameraDistance();
        } else {
//            logger.log(Level.INFO, "needFixDistance=false");
        }
    }
    
    protected void fixCameraDistance() {
        TempVars tv = TempVars.get();
        Vector3f tempRayOrigin = tv.vect1;
        Vector3f tempRayDirection = tv.vect2;
        tempRayOrigin.set(target.getWorldTranslation()).addLocal(rayOriginOffset);
        tempRayDirection.set(cam.getDirection()).negateLocal().normalizeLocal();
        collisionResults.clear();
        collisionResults = RayUtils.collideWith(tempRayOrigin, tempRayDirection, playService.getTerrain(), collisionResults);
        // 目标与相机距离
        float camDistance = cam.getLocation().subtract(tempRayOrigin, tv.vect3).length();
        
        boolean camDistanceFixed = false; // 表示是否处理过像机的距离
        for (int i = 0; i < collisionResults.size(); i++) {
            CollisionResult result = collisionResults.getCollision(i);

            // 挡在相机中间的物体
            if (result.getDistance() <= camDistance) {
                if (isObstacle(result.getGeometry())) {
                    Vector3f cp = result.getContactPoint();
                    
                    if (cp.distance(tempRayOrigin) < nearDistanceLimit) {
                        cp.set(tempRayOrigin).addLocal(tempRayDirection.mult(nearDistanceLimit, tv.vect6));
                    }
                    cp.addLocal(lookAtOffset).subtractLocal(rayOriginOffset);
                    cam.setLocation(cp);
                    camDistanceFixed = true;
                    break;
                } else {
                    // continue
                }
            } else {
                // 大于camDistance的后续都不需要检测
                break;
            }
        }
        
        // 如果经过检测，已经不需要拉近相机距离，则要释放needFixDistance,下次不再需要执行
        if (!camDistanceFixed) {
            needFixDistance = false;
        }
        
        tv.release();
    }
    
    // 查找相机与target中间的障碍物，需要排除target自身，及sky
    private boolean isObstacle(Spatial spatial) {
        if (spatial == target) {
            return false;
        }
        ProtoData pd = spatial.getUserData(ProtoData.USER_DATA);
        if (pd != null) {
            DataType pt = pd.getProto().getDataType();
            if (pt == DataType.sky || pt == DataType.actor) {
                return false;
            }
        }
        // 向父级查找
        if (spatial.getParent() != null) {
            return isObstacle(spatial.getParent());
        }
        return true;
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
        
        // 这里只要负责检测到needFixDistance=true就可以，解除needFixDistance将由fixCameraDistance来完成
        Object objA = event.getObjectA().getUserObject();
        Object objB = event.getObjectB().getUserObject();
        if ((objA == camCollisionChecker    && objB == playService.getTerrain())
         || (objA == playService.getTerrain() && objB == camCollisionChecker)) {
            needFixDistance = true;
        }
        
//        // test:测试碰撞组是否有效果
//        if (event.getNodeA() == camCollisionChecker || event.getNodeB() == camCollisionChecker) {
//            logger.log(Level.INFO, "CollideA={0} collisionGroup={1}, CollideB={2} collisionGroup={3}"
//                    , new Object[] {event.getNodeA().getName(), event.getObjectA().getCollisionGroup()
//                            , event.getNodeB().getName(), event.getObjectB().getCollisionGroup()});
//        }
        
    }
    
    public void setChase(Spatial spatial) {
        // 先移除原来的相机跟随
        if (this.target != null) {
            MyChaseCamera gcc = target.getControl(MyChaseCamera.class);
            if (gcc != null) {
                this.target.removeControl(gcc);
            }
        }
        spatial.addControl(this);
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
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
        if (camCollisionChecker != null) {
            GhostControl gc = camCollisionChecker.getControl(GhostControl.class);
            if (gc != null) {
                camCollisionChecker.removeControl(gc);
            }
        }
        if (physicsSpace != null) {
            physicsSpace.removeCollisionListener(this);
        }
        physicsSpace = null;
        camCollisionChecker = null;
        collisionObject = null;
        
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
        
        // 4.MyChaseCamera中自定义的监听
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
}
