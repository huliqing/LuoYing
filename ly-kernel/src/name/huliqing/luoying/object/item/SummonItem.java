/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.item;

import com.jme3.bullet.control.PhysicsControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.ConfigService;
import name.huliqing.luoying.layer.service.ItemService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.TerrainEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.utils.GeometryUtils;
import name.huliqing.luoying.utils.Temp;

/**
 * 可用于召唤角色的物品
 * @author huliqing
 */
public class SummonItem extends AbstractItem {
    private final ActorService actorService = Factory.get(ActorService.class);
    private final ItemService itemService = Factory.get(ItemService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    
    // 召换哪一个角色
    private String actorId;
    // 召换多少
    private int total = 1;
    
    // 设置要从source（召唤者）同步的属性值，这是个属性名称列表，当召唤后，被召唤的角色的这些属性的值将从召唤者身上获取。
    // 比如等级属性，分组属性，以便被召唤后的角色与召唤者有一样的等级和分组
    private String[] copyAttributesFromSource;
    // 设置要将被召唤者的属性的值连接到召唤者的属性名称。当召唤后，被召唤的角色的这些属性将指向召唤者的entityId.所以
    // 这些属性必须是NumberAttribute类型。
    // 比如attributeFollow,attributeOwner属性，指向跟随者和拥有者
    private String[] linkAttributesToSource;
    
    // ---- inner
    private final Ray ray = new Ray();
    
    @Override
    public void setData(ItemData data) {
        super.setData(data);
        this.actorId = data.getAsString("actorId");
        this.total = data.getAsInteger("total", total);
        copyAttributesFromSource = data.getAsArray("copyAttributesFromSource");
        linkAttributesToSource = data.getAsArray("linkAttributesToSource");
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
        
        // remove20161103
//        int level = actorService.getLevel(actor);
//        actorService.setLevel(bcc, level > 0 ? level : 1); // 至少1级
//        actorService.setPartner(actor, bcc);

        // 同步属性
        if (copyAttributesFromSource != null) {
            for (String attr : copyAttributesFromSource) {
                Attribute summonAttr = bcc.getAttributeManager().getAttribute(attr);
                Attribute sourceAttr = actor.getAttributeManager().getAttribute(attr);
                if (summonAttr != null && sourceAttr != null) {
                    summonAttr.setValue(sourceAttr.getValue());
                }
            }
        }
        if (linkAttributesToSource != null) {
            for (String attr : linkAttributesToSource) {
                NumberAttribute summonAttr = bcc.getAttributeManager().getAttribute(attr, NumberAttribute.class);
                if (summonAttr != null) {
                    summonAttr.setValue(actor.getEntityId());
                }
            }
        }
        
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
//            playNetwork.addMessage(actor, ResourceManager.get("common.summonPosError"), MessageType.notice);
            return false;
        }
        
        // 检查计算出的召唤点是否在地面以下，如果在地面以下，则不应该允许召唤
        // 否则召唤出的角色可能重叠在一起(因为光线的跟踪计算可能已经在地面以下而导致
        // 碰撞检测已经不正确
        float y = summonPos.y;
        Vector3f terrainHeight = getTerrainHeight(actor.getScene(), summonPos.x, summonPos.z);
        if (terrainHeight != null) {
            summonPos.set(terrainHeight);
        } else {
            summonPos.setY(actor.getSpatial().getWorldTranslation().y);
        }
        
        if (y < summonPos.y) {
//            playNetwork.addMessage(actor, ResourceManager.get("common.summonPosError"), MessageType.notice);
            return false;
        }
        // 设置地点并召唤
        actorService.setLocation(bcc, summonPos);
        playNetwork.addEntity(actor.getScene(), bcc);
        return true;
    }
    
    private Vector3f getTerrainHeight(Scene scene, float x, float z) {
        // 在场景载入完毕之后将植皮位置移到terrain节点的上面。
        List<TerrainEntity> sos = scene.getEntities(TerrainEntity.class, new ArrayList<TerrainEntity>());
        Vector3f heightPoint = null;
        for (TerrainEntity terrain : sos) {
            Vector3f terrainPoint = terrain.getHeight(x, z);
            if (terrainPoint != null) {
                if (heightPoint == null || terrainPoint.y > heightPoint.y) {
                    heightPoint = terrainPoint;
                }
            }
        }
        return heightPoint;
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
