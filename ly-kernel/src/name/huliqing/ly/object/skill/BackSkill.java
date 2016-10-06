/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.skill;

import com.jme3.math.Vector3f;
import name.huliqing.ly.Factory;
import name.huliqing.ly.data.SkillData;
import name.huliqing.ly.layer.network.ActorNetwork;
import name.huliqing.ly.layer.network.PlayNetwork;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.network.Network;

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
            // 注意：因为涉及到随机传送，所以必须统一由服务端处理。use Network
            Vector3f loc = actor.getSpatial().getLocalTranslation();
            playService.getRandomTerrainPoint(loc);
            actorNetwork.setLocation(actor, loc);
            backed = true;
        }
    }

    @Override
    public void cleanup() {
        backed = false;
        
        // 同步位置,必须的，否则服务端与客户端位置可能错位
        Network.getInstance().syncTransformDirect(actor);
        
        super.cleanup();
    }
    
}
