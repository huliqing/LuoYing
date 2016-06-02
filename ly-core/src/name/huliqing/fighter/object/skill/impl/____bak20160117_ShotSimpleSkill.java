///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.skill.impl;
//
//import com.jme3.bounding.BoundingVolume;
//import com.jme3.math.Vector3f;
//import com.jme3.util.TempVars;
//import java.util.List;
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.object.actor.Actor;
//import name.huliqing.fighter.data.SkillData;
//import name.huliqing.fighter.game.service.ActorService;
//import name.huliqing.fighter.game.service.BulletService;
//import name.huliqing.fighter.game.service.MagicService;
//import name.huliqing.fighter.game.service.PlayService;
//import name.huliqing.fighter.game.service.StateService;
//import name.huliqing.fighter.object.bullet.Bullet;
//import name.huliqing.fighter.object.bullet.BulletListener;
//import name.huliqing.fighter.object.magic.Magic;
//
///**
// * @author huliqing
// */
//public class ShotSimpleSkill extends FightSkill {
//    private final ActorService actorService = Factory.get(ActorService.class);
//    private final PlayService playService = Factory.get(PlayService.class);
//    private final StateService stateService = Factory.get(StateService.class);
//    private final BulletService bulletService = Factory.get(BulletService.class);
//    private final MagicService magicService = Factory.get(MagicService.class);
//    
//    // 发射的速度,默认1为标准速度，大于1则加大速度，小于1则减少速度
//    protected float shotSpeed = 1;
//    // 从哪一个位置开始发射，取值0~1， 0表示一开始发射，1表示技能执行完后发射
//    protected float shotTime = 1;
//    // 发射的起始位置，相对于角色的本地坐标
//    protected Vector3f shotOffset;
//    
//    // 子弹id,关联到一个子弹(bullet.xml)
//    protected String bullet;
//    
//    // 击中后的魔法
//    protected String endMagic;
//    
//    // ---- 内部
//    /**
//     * 0:未发射; 1已发射
//     */
//    private int state;
//    // 攻击目标
//    private Actor target;
//     
//    public ShotSimpleSkill(SkillData data) {
//        super(data);
//        shotSpeed = data.getProto().getAsFloat("shotSpeed", shotSpeed);
//        shotTime = data.getProto().getAsFloat("shotTime", shotTime);
//        shotOffset = data.getProto().getAsVector3f("shotOffset");
//        bullet = data.getProto().getAttribute("bullet");
//        endMagic = data.getProto().getAttribute("endMagic");
//    }
//    
//    protected Vector3f getShotOffset() {
//        return shotOffset;
//    }
//
//    @Override
//    protected void loopStart() {
//        super.loopStart(); 
//        state = 0;
//        // 应该在一开始时就确定目标，以避免在执行logic的过程被从服务端而来的消息
//        // 命令改变了目标或清除了目标
//        target = actorService.getTarget(actor);
//    }
//
//    @Override
//    protected void doUpdateLogic(float tpf) {
//        checkShot();
//    }
//    
//    /**
//     * 检查逻辑，确认是否进行shot
//     */
//    protected void checkShot() {
//        if (state == 0 && time >= data.getTrueUseTime() * shotTime) {
//            if (target != null) {
//                doShotTarget(target);
//                state = 1;
//            }
//        }
//    }
//    
//    protected void doShotTarget(Actor target) {
//        if (target == null) {
//            return;
//        }
//        
//        if (bullet != null) {
//            final Actor tempTarget = target;
//            Bullet bb = bulletService.loadBullet(bullet);
//            bb.setPath(getShotWorldPos(bb.getStartPoint()), target.getModel().getWorldBound().getCenter());
//            bb.setSpeed(shotSpeed);
//            bb.addListener(new BulletListener() {
//                @Override
//                public boolean hitCheck(Bullet bullet) {
//                    if (shotHitCheck(bullet, tempTarget)) {
//                        // 击中后结束子弹运行。
//                        bullet.doEnd();
//                        return true;
//                    }
//                    return false;
//                }
//            });
//            playService.addBullet(bb);
//        }
//    }
//        
//    /**
//     * 获取shot的世界坐标点位置
//     * @param store
//     * @return 
//     */
//    protected Vector3f getShotWorldPos(Vector3f store) {
//        store.set(getShotOffset());
//        TempVars tv = TempVars.get();
//        tv.quat1.lookAt(actor.getViewDirection(), Vector3f.UNIT_Y);
//        tv.quat1.mult(store, store);
//        store.addLocal(actor.getModel().getWorldTranslation());
//        tv.release();
//        return store;
//    }
//    
//    protected boolean shotHitCheck(Bullet bullet, Actor mainTarget) {
//        BoundingVolume collision = bullet.getCollisionBound();
//        // 注：这里是为了提高性能，只有在击中主目标之后才会进行伤害检测。
//        // 即使打开了multHit也必须在击中主目标之后才开始全部检测。否则在子弹
//        // 飞行过程中作全面检测会非常伤性能。
//        if (!collision.intersects(mainTarget.getModel().getWorldBound())) {
//            return false;
//        }
//        boolean result = false;
//        int sourceGroup = actorService.getGroup(actor);
//        if (hitChecker.canHit(sourceGroup, mainTarget)) {
//            applyHit(mainTarget);
//            result = true;
//        }
//        // 如果允许多重伤害时，则需要判断其他可能在攻击范围内的敌人
//        if (multHit) {
//            List<Actor> targets = playService.findAllActor();
//            for (Actor t : targets) {
//                // shot与普通的attackSkill不同，需要通过motion和target的Bound交叉碰撞来检测是否击中.
//                // 普通attack只需要检测是否在技能范围内（isInRange）及角度内即可。
//                if (t == mainTarget || !hitChecker.canHit(sourceGroup, t)) {
//                    continue;
//                }
//                if (collision.intersects(t.getModel().getWorldBound())) {
//                    applyHit(t);
//                    if (endMagic != null) {
//                        applyMagic(t, endMagic);
//                    }
//                    result = true;
//                }
//            }
//        }
//        return result;
//    }
//    
//    protected void applyMagic(Actor target, String magicId) {
//        Magic m = magicService.loadMagic(magicId);
//        m.setTarget(target);
//        m.setTraceObject(target.getModel());
//        playService.addMagic(m);
//    }
//
//    @Override
//    public void cleanup() {
//        state = 0;
//        target = null;
//        super.cleanup();
//    }
//    
//}
