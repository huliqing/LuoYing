/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.skill;

import com.jme3.animation.LoopMode;
import com.jme3.math.Vector3f;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.game.service.ConfigService;
import name.huliqing.fighter.game.state.lan.Network;
import name.huliqing.fighter.object.skill.AbstractSkill;
import name.huliqing.fighter.object.skill.Walk;

/**
 * 行路的技能
 * @see RunSkill
 * @author huliqing
 * @param <T>
 */
public class WalkSkill<T extends SkillData> extends AbstractSkill<T> implements Walk{
    private final ConfigService configService = Factory.get(ConfigService.class);
    
    // 步行或跑步循环动画的播放速度。
    protected float animSpeed = 1.0f;
    // 基本移动速度
    protected float baseSpeed;
    
    protected final Vector3f walkDirection = new Vector3f();
    protected final Vector3f viewDirection = new Vector3f();

    @Override
    public void setData(T data) {
        super.setData(data);
        animSpeed = data.getAsFloat("animSpeed", animSpeed);
        baseSpeed = data.getAsFloat("baseSpeed", configService.getBaseWalkSpeed());
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
    public void init() {
        super.init();
        
        // remove20160502
//        Vector3f dir = actor.getWalkDirection();
//        dir.setX(walkDirection.x).setZ(walkDirection.z);
//        actor.setWalkDirection(dir.normalizeLocal().mult(baseSpeed * data.getSpeed()));
//        if (viewDirection != null) {
//            actor.setViewDirection(viewDirection);
//        }
        
//        actor.setWalkDirection(walkDirection.setY(0).normalizeLocal().mult(baseSpeed * data.getSpeed()));
        actor.setWalkDirection(walkDirection.setY(0).normalizeLocal().mult(baseSpeed * getSpeed()));
        if (viewDirection != null) {
            actor.setViewDirection(viewDirection);
        }
        
        // 添加角色自动位置同步
        Network.getInstance().addAutoSyncTransform(actor);
    }
    
    @Override
    protected void doUpdateLogic(float tpf) {}

    @Override
    protected void doUpdateAnimation(String animation, LoopMode loopMode
            , float animFullTime, float animStartTime) {
        super.doUpdateAnimation(animation, loopMode, animFullTime / animSpeed, animStartTime);
    }

    @Override
    public void restoreAnimation() {
        String animation = data.getAnimation();
        if (animation != null && channelProcessor != null) {
            channelProcessor.restoreAnimation(animation
                    , data.getLoopMode()
                    , getAnimFullTime() / animSpeed
                    , getAnimStartTime()
                    , data.getChannels());
        }
    }
    
    @Override
    public void cleanup() {
        actor.setWalkDirection(Vector3f.ZERO.clone());
        // 移除角色位置自动同步(平滑同步)
        Network.getInstance().removeAutoSyncTransform(actor);
        
        // remove20160503,这个方法会直接导致客户端被严重拉扯，发生很糟糕的抖动！！！
        // remove20160503,这个方法会直接导致客户端被严重拉扯，发生很糟糕的抖动！！！
//        // 这是绝对位置同步,确保停下来时位置绝对同步正确，因为平滑同步无法确保位置绝对正确.
//        // 这种同步在FPS太低或延迟太高的情况下容易产生跳跃现象.
//        Network.getInstance().syncTransformDirect(actor);
        
        
        super.cleanup();
    }
    
}
