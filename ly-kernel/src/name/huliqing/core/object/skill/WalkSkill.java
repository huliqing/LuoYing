/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skill;

import com.jme3.animation.LoopMode;
import com.jme3.math.Vector3f;
import name.huliqing.core.Factory;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.network.Network;

/**
 * 行路的技能
 * @see RunSkill
 * @author huliqing
 */
public class WalkSkill extends AbstractSkill implements Walk{
    private final ActorService actorService = Factory.get(ActorService.class);
    
    // 步行或跑步循环动画的播放速度。
    protected float animSpeed = 1.0f;
    // 基本移动速度
    protected float baseSpeed = 1.5f;
    
    protected final Vector3f walkDirection = new Vector3f();
    protected final Vector3f viewDirection = new Vector3f();

    @Override
    public void setData(SkillData data) {
        super.setData(data);
        animSpeed = data.getAsFloat("animSpeed", animSpeed);
        baseSpeed = data.getAsFloat("baseSpeed", baseSpeed);
    }

    public Vector3f getWalkDirection() {
        return walkDirection;
    }
    
    /**
     * 设置行动方向（非位置）
     * @param walkDirection 
     */
    @Override
    public void setWalkDirection(Vector3f walkDirection) {
        this.walkDirection.set(walkDirection);
    }
    
    /**
     * 设置视角方向(非位置)
     * @param viewDirection 
     */
    @Override
    public void setViewDirection(Vector3f viewDirection) {
        this.viewDirection.set(viewDirection);
    }
    
    @Override
    public void setBaseSpeed(float baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    @Override
    public void initialize() {
        super.initialize();
        
        actorService.setWalkDirection(actor, walkDirection.setY(0).normalizeLocal().mult(baseSpeed * getSpeed()));
        
        if (viewDirection != null) {
            actorService.setViewDirection(actor, viewDirection);
        }
        
        // 添加角色自动位置同步
        Network.getInstance().addAutoSyncTransform(actor);
    }
    
    @Override
    protected void doUpdateLogic(float tpf) {}

    @Override
    protected void doUpdateAnimation(String animation, boolean loop
            , float animFullTime, float animStartTime) {
        super.doUpdateAnimation(animation, loop, animFullTime / animSpeed, animStartTime);
    }

    @Override
    public void restoreAnimation() {
        if (animation != null) {
            channelModule.restoreAnimation(animation
                    , channels
                    , loop ? LoopMode.Loop : LoopMode.DontLoop
                    , getAnimFullTime() / animSpeed
                    , getAnimStartTime());
        }
    }
    
    @Override
    public void cleanup() {
        actorService.setWalkDirection(actor, Vector3f.ZERO.clone());
        // 移除角色位置自动同步(平滑同步)
        Network.getInstance().removeAutoSyncTransform(actor);
        
        super.cleanup();
    }
    
}
