/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skill;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.animation.Bone;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.SkinConstants;
import name.huliqing.core.xml.ProtoData;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.SkinService;
import name.huliqing.core.loader.Loader;

/**
 * 弓箭的射击技能
 * @author huliqing
 * @param <T>
 */
public class ShotBowSkill<T extends SkillData> extends ShotSkill<T> {
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkinService skinService = Factory.get(SkinService.class);
    
    private String weaponAnim;
    private float timeBulletTake = 0.2f;
    private float timeBulletPull = 0.4f;
    
    // ---- 内部参数
    private int shotState; // 0:取箭; 1:上弦; 2: other
    private Spatial arrow; // 取出的箭,可复用，不会射出
    // 取箭的时候，用于绑定“箭”的角色的某一块骨头
    private String arrowBindBone;
    
    // 向上射和向下射击的动画名称。
    private String animationShotDown;
    private String animationShotUp;
    // 向上和向下射击时bullet的初始位置偏移。
    private Vector3f shotDownOffset;
    private Vector3f shotUpOffset;
    
    // ---- inner
    private int shotDir; // 0 : down; 1: horizontal; 2:up
    
    @Override
    public void setData(T data) {
        super.setData(data); 
        this.weaponAnim = data.getAttribute("weaponAnim");
        this.timeBulletTake = data.getAsFloat("timeBulletTake", timeBulletTake);
        this.timeBulletPull = data.getAsFloat("timeBulletPull", timeBulletPull);
        this.animationShotDown = data.getAttribute("animationShotDown");
        this.animationShotUp = data.getAttribute("animationShotUp");
        this.shotDownOffset = data.getAsVector3f("shotDownOffset");
        this.shotUpOffset = data.getAsVector3f("shotUpOffset");
        
        // 载入箭模型, 该箭模型(arrow)主要用于“取箭”及“拉弓上弦”动画。
        String tempArrow = data.getAttribute("arrow");
        if (tempArrow != null) {
            arrow = Loader.loadModel(tempArrow);
        }
        
        // 取箭的时候，用于绑定“箭”模型的角色骨架上的某一块骨头,如果没有指定，
        // 则默认以角色的右手武器的骨头作为该骨头.
        this.arrowBindBone = data.getAttribute("arrowBindBone", "weapon.R");
    }

    @Override
    protected void init() {
        super.init();
        // -- 重置state
        shotState = 0;
        
        // 偿试查找出弓模型，用于执行拉弓动画
        Spatial weapon = null;
        List<SkinData> weaponSkins = skinService.getCurrentWeaponSkin(actor);
        if (!weaponSkins.isEmpty()) {
            SkinData bowSkinData = null;
            for (SkinData sd : weaponSkins) {
                if (sd.getWeaponType() == SkinConstants.WEAPON_BOW) {
                    bowSkinData = sd;
                    break;
                }
            }
            if (bowSkinData != null) {
                WeaponBowFinder finder = new WeaponBowFinder(bowSkinData);
                actor.getModel().breadthFirstTraversal(finder);
                weapon = finder.bowNode;
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
    protected void doUpdateAnimation(String animation, LoopMode loopMode
            , float animFullTime, float animStartTime) {
        shotDir = 1; // horizontal
        if ((this.animationShotDown != null || this.animationShotUp != null) 
                && actorService.getTarget(actor) != null) {
            
            TempVars tv = TempVars.get();
            Vector3f viewPos = tv.vect1;
            Vector3f targetPos = actorService.getTarget(actor).getModel().getWorldBound().getCenter();
            
            Vector3f selfPos = actor.getModel().getWorldBound().getCenter();
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
        
        // animationShotDown和animationShotUp是额外指定的武器，这里必须检查一下是否存在。
        actorService.checkAndLoadAnim(actor, animation);
        
        super.doUpdateAnimation(animation, loopMode, animFullTime, animStartTime);
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
    protected void doUpdateLogic(float tpf) {
        super.doUpdateLogic(tpf);
//        float trueUseTime = data.getTrueUseTime();
        if (shotState == 0 && arrow != null && time >= timeBulletTake * trueUseTime) {
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
        SkeletonControl sc = actor.getModel().getControl(SkeletonControl.class);
        if (sc != null && arrow != null) {
            Bone abb = sc.getSkeleton().getBone(arrowBindBone);
            if (abb != null) {
                Node abbNode = sc.getAttachmentsNode(arrowBindBone);
                abbNode.attachChild(arrow);
            }
        }
    }
    
    // 隐藏“取箭”时的“箭”，实际上取箭动作中的箭与射出的“箭”是不同的对象。
    private void hideArrow() {
        if (arrow != null) {
            arrow.removeFromParent();
        }
    }
    
    @Override
    public void cleanup() {
        if (arrow != null) {
            arrow.removeFromParent();
        }
        super.cleanup();
    }
    
    // 用于查找弓模型
    private class WeaponBowFinder implements SceneGraphVisitor {
        
        private SkinData bowSkinData;
        private Spatial bowNode;
        
        public WeaponBowFinder(SkinData bowSkinData) {
            this.bowSkinData = bowSkinData;
        }
        
        @Override
        public void visit(Spatial spatial) {
            ProtoData pd = spatial.getUserData(ProtoData.USER_DATA);
            if (pd != null && pd == bowSkinData) {
                bowNode = spatial;
            }
        }
    }
    
}
