/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.skill.impl;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.game.state.lan.Network;
import name.huliqing.fighter.object.skill.AbstractSkill;

/**
 * 回城、瞬移技能
 * @author huliqing
 * @param <T>
 */
public class BackSkill<T extends SkillData> extends AbstractSkill<T> {
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    
    // 人物消失的时间插值点
    private float backPoint = 0.75f;
    
    // ---- 内部参数
    // 需要一起消失的物品列表,这些物品会随着角色一起回城或瞬移
    // 这个功能允许角色在回城或瞬移的时候顺便把物品带走。
    private List<Spatial> backList;
    // 标记是否已经完成回传
    private boolean backed;
    
    @Override
    public void setData(T data) {
        super.setData(data); 
        this.backPoint = data.getAsFloat("backPoint", backPoint);
    }

    /**
     * 添加需要和角色一同“回城”“瞬移”的物品。
     * @param spatial 
     */
    public void addBackObject(Spatial spatial) {
        if (backList == null) {
            backList = new ArrayList<Spatial>(2);
        }
        if (!backList.contains(spatial)) {
            backList.add(spatial);
        }
    }

    @Override
    protected void doUpdateLogic(float tpf) {
        if (!backed && time >= trueUseTime * backPoint) {
            // 注意：因为涉及到随机传送，所以必须统一由服务端处理。use Network
            Vector3f loc = actor.getModel().getLocalTranslation();
            playNetwork.getRandomTerrainPoint(loc);
            playNetwork.moveObject(actor.getModel(), loc);
            if (backList != null) {
                for (Spatial backObject : backList) {
                    playNetwork.moveObject(backObject, loc);
                }
            }
            backed = true;
        }
    }

    @Override
    public void cleanup() {
        backed = false;
        
        // remove20160419不再需要打开或关闭物理特性
//        // 重新激活物理碰撞
//        actor.setEnabled(true);
        
        // 清理附带的物品列表
        if (backList != null) {
            backList.clear();
        }
        
        // 同步位置,必须的，否则服务端与客户端位置可能错位
        Network.getInstance().syncTransformDirect(actor);
        
        super.cleanup();
    }
    
}
