/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.item;

import com.jme3.bullet.control.PhysicsControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import name.huliqing.ly.Factory;
import name.huliqing.ly.data.ItemData;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.ly.layer.network.PlayNetwork;
import name.huliqing.ly.layer.service.ActorService;
import name.huliqing.ly.layer.service.ConfigService;
import name.huliqing.ly.layer.service.ItemService;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.utils.GeometryUtils;
import name.huliqing.ly.utils.Temp;

/**
 * 可用于召唤角色的物品
 * @author huliqing
 */
public class SummonItem extends AbstractItem {
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
    public void setData(ItemData data) {
        super.setData(data);
        this.actorId = data.getAsString("actorId");
        this.total = data.getAsInteger("total", total);
    }

    @Override
    public void use(Entity actor) {
        int count = 0;
        for (int i = 0; i < total; i++) {
            count += summon(actor, actorId) ? 1 : 0;
        }
        // 物品数减少
        if (count > 0) {
            itemService.removeItem(actor, data.getId(), count);
        }
    }
    
    private boolean summon(Entity actor, String actorId) {
        // -- 载入角色
        Entity bcc = Loader.load(actorId);
        int level = (int) (actorService.getLevel(actor) * configService.getSummonLevelFactor());
        actorService.setName(bcc, actorService.getName(bcc) + "-" + actorService.getName(actor));
        actorService.setLevel(bcc, level > 0 ? level : 1); // 至少1级
        
        // -- 设置为同伴
        actorService.setPartner(actor, bcc);
        
        // --添加角色到场景
        Spatial root = GeometryUtils.findRootNode(actor.getSpatial());
        float zExtent = GeometryUtils.getBoundingVolumeZExtent(actor.getSpatial()) * 0.5f;
        float distance = GeometryUtils.getBoundingVolumeZExtent(bcc.getSpatial());
        
        TempVars tv = TempVars.get();
        Vector3f origin = tv.vect1;
        Vector3f direction = tv.vect2;
        origin.set(actor.getSpatial().getWorldBound().getCenter()).setY(actor.getSpatial().getWorldTranslation().y + 1);
        direction.set(actorService.getViewDirection(actor)).normalizeLocal();
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
        actorService.setLocation(bcc, summonPos);
        playNetwork.addEntity(actor.getScene(), bcc);
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
