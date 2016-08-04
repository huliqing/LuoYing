/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.handler;

import com.jme3.bullet.control.PhysicsControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import name.huliqing.core.Factory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.HandlerData;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.mvc.network.PlayNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.ConfigService;
import name.huliqing.core.mvc.service.ItemService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.StateService;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.utils.GeometryUtils;
import name.huliqing.core.utils.RayUtils;
import name.huliqing.core.utils.Temp;

/**
 * 召唤角色，召唤后目标角色的物品会减少
 * @author huliqing
 */
public class SummonHandler extends AbstractHandler {
    private final StateService stateService = Factory.get(StateService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final ConfigService configService = Factory.get(ConfigService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ItemService itemService = Factory.get(ItemService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    
    // 召换哪一个角色
    private String actorId;
    // 召换多少
    private int total = 1;
    
    private final Ray ray = new Ray();

    @Override
    public void setData(HandlerData data) {
        super.setData(data);
        this.actorId = data.getAttribute("actorId");
        this.total = data.getAsInteger("total", total);
    }

    @Override
    protected void useObject(Actor actor, ObjectData data) {
        int count = 0;
        for (int i = 0; i < total; i++) {
            count += summon(actor, actorId) ? 1 : 0;
        }
        // 物品数减少
        if (count > 0) {
//            remove(actor, data.getId(), count);
            itemService.removeItem(actor, data.getId(), count);
        }
    }
    
    private boolean summon(Actor actor, String actorId) {
        // -- 载入角色
        Actor bcc = actorService.loadActor(actorId);
        int level = (int) (actorService.getLevel(actor) * configService.getSummonLevelFactor());
        actorService.setName(bcc, actorService.getName(bcc) + "-" + actorService.getName(actor));
        actorService.setLevel(bcc, level > 0 ? level : 1); // 至少1级
        
        // -- 设置为同伴
        actorService.setPartner(actor, bcc);
        
        // --添加角色到场景
        Spatial root = GeometryUtils.findRootNode(actor.getModel());
        float zExtent = GeometryUtils.getBoundingVolumeZExtent(actor.getModel()) * 0.5f;
        float distance = GeometryUtils.getBoundingVolumeZExtent(bcc.getModel());
        
        TempVars tv = TempVars.get();
        Vector3f origin = tv.vect1;
        Vector3f direction = tv.vect2;
        origin.set(actor.getModel().getWorldBound().getCenter()).setY(actor.getModel().getWorldTranslation().y + 1);
        direction.set(actor.getViewDirection()).normalizeLocal();
        origin.addLocal(direction.mult(zExtent));
        
        // 通过障碍判断，找出可用于召唤的地点
        Vector3f summonPos = findSummonPosition(root, origin, direction, distance, 0);
        tv.release();
        
        if (summonPos == null) {
            playNetwork.addMessage(actor, ResourceManager.get("common.summonPosError"), MessageType.notice);
            return false;
        }
        
        // 检查计算出的召唤点是否在地面以下，如果在地面以下，则不应该允许召唤
        // 否则召唤出的角色可能重叠在一起(因为光线的跟踪计算可能已经在地面以下而导致
        // 碰撞检测已经不正确
        float y = summonPos.y;
        summonPos.setY(playService.getTerrainHeight(summonPos.x, summonPos.z));
        
        if (y < summonPos.y) {
            playNetwork.addMessage(actor, ResourceManager.get("common.summonPosError"), MessageType.notice);
            return false;
        }
        // 设置地点并召唤
        bcc.setLocation(summonPos);
        
        playNetwork.addActor(bcc);
        return true;
    }
    
    // 计算用于召奂物体的位置
    private Vector3f findSummonPosition(Spatial root, Vector3f origin, Vector3f direction, float distance, int count) {
        // 不允许太多次，如果光线检测延伸了太多次仍找不到合适的召唤点，则返回null
        if (count >= 7) {
//            Logger.getLogger(getClass().getName()).log(Level.INFO, "findSummonPosition limit times={0}", count);
            return null;
        }
//        DebugDynamicUtils.debugArrow(origin.length() + "", origin, direction, distance);
        if (hasObstacle(root, origin, direction, distance)) {
            // 如果遇到障碍物，则原点向前移动，直到找到没有障碍物的地方
            origin.addLocal(direction.mult(distance));
            count++;
            return findSummonPosition(root, origin, direction, distance, count);
        } else {
            // 如果没有障碍物，则在origin的dirction方向上前进distance的一半，作为召唤点
//            Logger.getLogger(getClass().getName()).log(Level.INFO, "find times={0}", count);
            return origin.add(direction.mult(distance * 0.5f));
        }
    }
    
    private boolean hasObstacle(Spatial root, Vector3f origin, Vector3f direction, float limit) {
        Temp tp = Temp.get();
        CollisionResults results = tp.results;
        results.clear();
        collideWith(origin, direction, root, results);
        boolean result = false;
        for (CollisionResult r : results) {
            if (r.getDistance() > limit) {
                break;
            }
            if (isObstacle(r.getGeometry())) {
                // 障碍物找到
//                Log.get(GeometryUtils.class).log(Level.INFO, "Found obstacle infront! distance={0}, name={1}"
//                        , new Object[] {r.getDistance(), r.getGeometry().getName()});
                result = true;
                break;
            }
 
        }
        tp.release();
        return result;
    }
    
    private CollisionResults collideWith(Vector3f origin, Vector3f direction, Spatial root, CollisionResults store) {
        ray.setOrigin(origin);
        ray.setDirection(direction);
        root.collideWith(ray, store);
        return store;
    }
    
    // 查找前方是否存在障碍物
    private static boolean isObstacle(Spatial spatial) {
        if (spatial.getControl(PhysicsControl.class) != null) {
            return true;
        } else if (spatial.getParent() != null) {
            return isObstacle(spatial.getParent());
        }
        return false;
    }

    
}
