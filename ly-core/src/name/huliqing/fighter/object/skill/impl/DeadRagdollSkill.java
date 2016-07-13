/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.skill.impl;

import com.jme3.animation.LoopMode;
import com.jme3.bullet.control.KinematicRagdollControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.skill.AbstractSkill;
import name.huliqing.fighter.object.skill.Dead;

/**
 * 执行死亡技能,该技能支持使用ragdoll模式.
 * @author huliqing
 * @param <T>
 */
public class DeadRagdollSkill<T extends SkillData> extends AbstractSkill<T> implements Dead{
    private final StateService stateService = Factory.get(StateService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    
    // 默认情况下开启布娃娃控制,如果不使用,则只是直接播放死亡动画
    private boolean useRagdoll = true;
    // 只有在受力的情况下才开启ragdoll模式
    private boolean forceOnly;
    // 使用要作为ragdoll的骨骼,如果没有指定该参数则默认使用全部,但是对于有IK约束
    // 的模型会显示异常，所以如果有IK骨骼则应该指定该参数，并排除IK骨头
    // 注意：使用bones参数时，必须包含骨骼的根节点，否则在嵌接死亡动画时位置会被还原到ragdoll之前的位置
    private String[] bones;
    // 使用要作为接受hit力的骨骼
    private String[] bonesHit;
    // 死亡动画的执行时间点,取值为0.0~1.0
    // 是技能总使用时间的插值点.
    private float deadAnimPoint = 0.5f;
    // 角色的临终遗言,这是指向resource中key的数组,角色在死后会从这个数组中
    // 随机取一个作为遗言吐槽
    private String[] lastWords;
    
    // ---- 内部参数 ----
    // Ragdoll控制
    private KinematicRagdollControl ragdoll;
    // 死亡时的受力
    private Vector3f force;
    // 是否已经播放了死亡动画
    private boolean deadAnimPlayed;
    private boolean ragdollEnabled;
    // 角色的原始物理特性开关状态
    private boolean oldEnableState;
    
    @Override
    public void initData(T data) {
        super.initData(data); 
        this.useRagdoll = data.getAsBoolean("useRagdoll", useRagdoll);
        this.forceOnly = data.getAsBoolean("forceOnly", forceOnly);
        this.bones = data.getAsArray("bones");
        this.bonesHit = data.getAsArray("bonesHit");
        this.deadAnimPoint = data.getAsFloat("deadAnimPoint", deadAnimPoint);
        // 如果技能没有指定useTime,则默认3秒，2秒太少，可能在开启ragdoll时，角色还没有倒下就停止了
        Float tempUseTime = data.getAsFloat("useTime");
        if (tempUseTime == null) {
            this.data.setUseTime(3.0f);
        }
        this.lastWords = data.getAsArray("lastWords");
    }

    @Override
    public void applyForce(Vector3f force) {
        if (force == null) {
            return;
        }
        if (this.force == null) {
            this.force = new Vector3f();
        }
        this.force.set(force);
    }

    @Override
    public void init() {
        super.init(); 
        oldEnableState = actor.isEnabled();
        
        ragdollEnabled = useRagdoll;
        if (useRagdoll) {
            if (forceOnly) {
                if (force == null || force.lengthSquared() < 0.001f) {
                    ragdollEnabled = false;
                }
            }
        }
        
        if (ragdollEnabled) {
            if (ragdoll == null) {
                ragdoll = new KinematicRagdollControl(0.5f);
                ragdoll.setRootMass(actor.getMass());
                if (bones != null) {
                    for (String bone : bones) {
                        ragdoll.addBoneName(bone);
                    }
                }
                
            }
            // 需要临时关闭角色的原始的物理特性,否则会和ragdoll冲突
            actor.setEnabled(false);
            actor.getModel().addControl(ragdoll);
            actor.getPhysicsSpace().add(ragdoll);
            ragdoll.setEnabled(true);
            ragdoll.setRagdollMode();
            
            if (bonesHit != null && force != null) {
                for (String boneHit : bonesHit) {
                    PhysicsRigidBody prb = ragdoll.getBoneRigidBody(boneHit);
                    prb.setLinearVelocity(force);
                }
            }
        } else {
            // 如果不需要ragdoll模式,则立即执行deadAnimation.
            playDeadAnim();
            deadAnimPlayed = true;
        }
    }
    
    @Override
    protected void doUpdateLogic(float tpf) {
        if (!deadAnimPlayed && time >= trueUseTime * deadAnimPoint) {
            playDeadAnim();
            deadAnimPlayed = true;
        }
    }

    @Override
    protected void doUpdateAnimation(String animation, LoopMode loopMode
            , float animFullTime, float animStartTime) {
        // ignore ,不直接使用父类的playAnimation,因为它是在一开始时就执行.
        // 而DeadRagdoll需要在deadAnimPoint指定的时间点上才播放动画
        // use playDeadAnim instead
    }
    
    protected void playDeadAnim() {
        String animation = data.getAnimation();
        
        if (animation == null && channelProcessor != null) {
            // Reset，对于没有“死亡”动画的角色，在死亡时必须让它“静止”
            // 让角色的其它动画停止播放，以防止角色在死亡之后仍然在做其它动作的奇怪现象。
            channelProcessor.reset();
            return;
        }
        
        if (animation != null && channelProcessor != null) {
            
            Spatial model = actor.getModel();
            
            Vector3f world = model.getWorldTranslation();
            Vector3f local = model.getLocalTranslation();
            float height = playService.getTerrainHeight(world.x, world.z);
            local.set(world).setY(height);
            model.setLocalTranslation(local);
            
            Quaternion q = new Quaternion();
            float[] angles = new float[3];
            model.getLocalRotation().toAngles(angles);
            q.fromAngleAxis(angles[1], Vector3f.UNIT_Y);
            model.setLocalRotation(q);
            
            float fullAnimTime = getAnimFullTime() * (1 - deadAnimPoint);
            float animStartTime = fullAnimTime * data.getCutTimeStart();
            super.doUpdateAnimation(animation, data.getLoopMode(), fullAnimTime, animStartTime);
            
            if (ragdoll != null) {
                // 这句必须放在playAnimation之后
                ragdoll.blendToKinematicMode(0.5f);
            }
        }
    }

    @Override
    protected void end() {
        // 播放临终吐槽
        if (lastWords != null && lastWords.length > 0) {
//            actor.say(ResourceManager.get(lastWords[FastMath.nextRandomInt(0, lastWords.length - 1)])); // remove
            actorNetwork.speak(actor, ResourceManager.get(lastWords[FastMath.nextRandomInt(0, lastWords.length - 1)]), -1);
        }
        
        // remove20160408不再立即清理状态，这可能会导致一些状态特效被立即移除。
        // 角色死亡后要清理身上的所有状态。
//        stateService.clearStates(actor);
        
        super.end();
    }
    
    @Override
    public void cleanup() {
        if (ragdoll != null) {
            ragdoll.setEnabled(false);
            actor.getModel().removeControl(ragdoll);
            actor.getPhysicsSpace().remove(ragdoll);
        }
        // 作用力要清0,因为只用一次
        if (force != null) {
            force.zero();
        }
        deadAnimPlayed = false;
        ragdollEnabled = false;
        // 恢复enabled状态
        actor.setEnabled(oldEnableState);
        super.cleanup();
    }
    
}
