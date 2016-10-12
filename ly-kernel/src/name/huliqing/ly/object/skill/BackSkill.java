/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.skill;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.List;
import name.huliqing.ly.Factory;
import name.huliqing.ly.data.SkillData;
import name.huliqing.ly.layer.network.ActorNetwork;
import name.huliqing.ly.layer.network.PlayNetwork;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.network.Network;
import name.huliqing.ly.object.entity.TerrainEntity;
import name.huliqing.ly.object.scene.Scene;

/**
 * 回城、瞬移技能
 * @author huliqing
 */
public class BackSkill extends AbstractSkill {
    private final PlayService playService = Factory.get(PlayService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    
    // 人物消失的时间插值点
    private float backPoint = 0.75f;
    
    // ---- 内部参数
    
    // 标记是否已经完成回传
    private boolean backed;
    
    @Override
    public void setData(SkillData data) {
        super.setData(data); 
        this.backPoint = data.getAsFloat("backPoint", backPoint);
    }

    @Override
    protected void doUpdateLogic(float tpf) {
        if (!backed && time >= trueUseTime * backPoint) {
            Vector3f loc = actor.getSpatial().getLocalTranslation();
            
            // 从场景中获取一个随机点
            getRandomTerrainPoint(actor.getScene(), loc);
            
            // 注意：因为涉及到随机传送，所以必须统一由服务端处理。use Network
            actorNetwork.setLocation(actor, loc);
            backed = true;
        }
    }
    
    private Vector3f getRandomTerrainPoint(Scene scene, Vector3f store) {
        List<TerrainEntity> tes = scene.getEntities(TerrainEntity.class, null);
        if (tes == null || tes.isEmpty()) {
            return store.set(0, 0, 0);
        }
        TerrainEntity te = tes.get(FastMath.nextRandomInt(0, tes.size() - 1));
        if (te.getSpatial() == null) {
            store.set(0, 0, 0);
            return store;
        }
        BoundingBox bb = (BoundingBox) te.getSpatial().getWorldBound();
        float xe = bb.getXExtent() * 0.5f; // x 0.5防止掉出边界
        float ze = bb.getZExtent() * 0.5f;
        
        store.set(FastMath.nextRandomFloat() * xe * 2 - xe, 0, FastMath.nextRandomFloat() * ze * 2 - ze);
        store.set(te.getHeight(store.x, store.z));
        return store;
    }

    @Override
    public void cleanup() {
        backed = false;
        
        // 同步位置,必须的，否则服务端与客户端位置可能错位
        Network.getInstance().syncTransformDirect(actor);
        
        super.cleanup();
    }
    
}
