/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.skill;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.animation.Bone;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import java.util.List;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.ModelEntity;
import name.huliqing.luoying.object.module.SkinModule;
import name.huliqing.luoying.object.skin.Skin;
import name.huliqing.luoying.object.skin.WeaponSkin; 
import name.huliqing.luoying.utils.GeometryUtils;

/**
 * 弓箭的射击技能
 * @author huliqing
 */
public class ShotBowSkill extends ShotSkill {
//    private final PlayService playService = Factory.get(PlayService.class);
//    private ActorModule actorModule;
    private SkinModule skinModule;
    
    private String weaponAnim;
    private float timeBulletTake = 0.2f;
    private float timeBulletPull = 0.4f;
    
    // ---- 内部参数
    private int shotState; // 0:取箭; 1:上弦; 2: other
    // 取箭的时候，用于绑定“箭”的角色的某一块骨头
    private String arrowBindBone;
    
    // 向上射和向下射击的动画名称。
    private String animationShotDown;
    private String animationShotUp;
    // 向上和向下射击时bullet的初始位置偏移。
    private Vector3f shotDownOffset;
    private Vector3f shotUpOffset;
    private String arrow;
    
    // ---- inner
    // 0 : down; 1: horizontal; 2:up
    private int shotDir;
    // 取出的箭,可复用，不会射出
    private Spatial arrowNode;
    
    @Override
    public void setData(SkillData data) {
        super.setData(data); 
        this.weaponAnim = data.getAsString("weaponAnim");
        this.timeBulletTake = data.getAsFloat("timeBulletTake", timeBulletTake);
        this.timeBulletPull = data.getAsFloat("timeBulletPull", timeBulletPull);
        this.animationShotDown = data.getAsString("animationShotDown");
        this.animationShotUp = data.getAsString("animationShotUp");
        this.shotDownOffset = data.getAsVector3f("shotDownOffset");
        this.shotUpOffset = data.getAsVector3f("shotUpOffset");
        this.arrow = data.getAsString("arrow");
        // 取箭的时候，用于绑定“箭”模型的角色骨架上的某一块骨头,如果没有指定，
        // 则默认以角色的右手武器的骨头作为该骨头.
        this.arrowBindBone = data.getAsString("arrowBindBone", "weapon.R");
    }

    @Override
    public void setActor(Entity actor) {
        super.setActor(actor);
//        actorModule = actor.getModuleManager().getModule(ActorModule.class);
        skinModule = actor.getModuleManager().getModule(SkinModule.class);        
    }

    @Override
    public void initialize() {
        super.initialize();
        
        // -- 重置state
        shotState = 0;
        
        // 载入箭模型, 该箭模型(arrow)主要用于“取箭”及“拉弓上弦”动画。
        if (arrowNode == null && arrow != null) {
            arrowNode = Loader.loadModel(arrow);
            if (actor instanceof ModelEntity && ((ModelEntity) actor).getData().isPreferUnshaded()) {
                GeometryUtils.makeUnshaded(arrowNode);
            }
        }
        
        // 偿试查找出弓模型，用于执行拉弓动画
        Spatial weapon = null;
        List<Skin> usingSkins = skinModule.getUsingSkins();
        if (usingSkins != null && !usingSkins.isEmpty()) {
            for (Skin skin : usingSkins) {
                if (skin instanceof WeaponSkin) {
                    weapon = skin.getSpatial();
                    break;
                }
            }
        }
  
        // -- 开始“弓”动画
        if (weapon != null && weaponAnim != null && trueUseTime > 0) {
            AnimControl ac = weapon.getControl(AnimControl.class);
            Animation anim = ac.getAnim(weaponAnim);
            if (anim == null) {
                return;
            }
            AnimChannel channel;
            if (ac.getNumChannels() > 0) {
                channel = ac.getChannel(0);
            } else {
                channel = ac.createChannel();
            }
            channel.setAnim(weaponAnim);
            channel.setLoopMode(LoopMode.DontLoop);
            channel.setSpeed(anim.getLength() / trueUseTime);
        }
    }

    @Override
    protected void doUpdateAnimation(String animation, boolean loop
            , float animFullTime, float animStartTime) {
        shotDir = 1; // horizontal
        // 目标角色
        Entity target = getTarget();
        if ((this.animationShotDown != null || this.animationShotUp != null) && target != null) {
            TempVars tv = TempVars.get();
            Vector3f viewPos = tv.vect1;
            Vector3f targetPos = target.getSpatial().getWorldBound().getCenter();
            
            Vector3f selfPos = actor.getSpatial().getWorldBound().getCenter();
            viewPos.set(targetPos).setY(selfPos.getY());
            Vector3f vec1 = tv.vect2;
            Vector3f vec2 = tv.vect3;
            targetPos.subtract(selfPos, vec1).normalizeLocal();
            viewPos.subtract(selfPos, vec2).normalizeLocal();
            float angle = vec1.angleBetween(vec2) * FastMath.RAD_TO_DEG;
            boolean up = targetPos.y > viewPos.y;
            tv.release();
            if (angle > 15) {
                if (up && animationShotUp != null) {
                    animation = animationShotUp;
                    shotDir = 2;
                } else if (animationShotDown != null) {
                    animation = animationShotDown;
                    shotDir = 0;
                }
            }
        }
        super.doUpdateAnimation(animation, loop, animFullTime, animStartTime);
    }

    @Override
    public Vector3f getShotOffset() {
        if (shotDir == 0 && shotDownOffset != null) {
            return shotDownOffset;
        } else if (shotDir == 2 && shotUpOffset != null) {
            return shotUpOffset;
        }
        return super.getShotOffset();
    }

    @Override
    protected void doSkillUpdate(float tpf) {
        super.doSkillUpdate(tpf);
        if (shotState == 0 && arrowNode != null && time >= timeBulletTake * trueUseTime) {
            takeArrow(); // 取箭
            shotState = 1;
        }
        
        if (shotState == 1 
                
                && shotChecker.getIndex() >= 0 // 说明已经发射过
                
                ) {
            hideArrow(); // 藏起手上的箭
            shotState = 2;
        }
        
    }
    
    // 取箭
    private void takeArrow() {
        SkeletonControl sc = actor.getSpatial().getControl(SkeletonControl.class);
        if (sc != null && arrowNode != null) {
            Bone abb = sc.getSkeleton().getBone(arrowBindBone);
            if (abb != null) {
                Node abbNode = sc.getAttachmentsNode(arrowBindBone);
                abbNode.attachChild(arrowNode);
            }
        }
    }
    
    // 隐藏“取箭”时的“箭”，实际上取箭动作中的箭与射出的“箭”是不同的对象。
    private void hideArrow() {
        if (arrowNode != null) {
            arrowNode.removeFromParent();
        }
    }
    
    @Override
    public void cleanup() {
        if (arrowNode != null) {
            arrowNode.removeFromParent();
        }
        super.cleanup();
    }
    
}
