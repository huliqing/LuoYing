///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.skill.impl;
//
//import com.jme3.math.FastMath;
//import java.util.List;
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.data.SkillData;
//import name.huliqing.fighter.game.network.ActorNetwork;
//import name.huliqing.fighter.object.actor.Actor;
//import name.huliqing.fighter.object.skill.PointChecker;
//import name.huliqing.fighter.utils.MathUtils;
//
///**
// * 多重射击技能
// * @author huliqing
// */
//public class ShotMultSkill extends ShotSkill {
//    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
//    
//    // 发射的使用时间，该参数取值在0~1之间，是整个trueUseTime的比率
//    // 比如trueUseTime=2秒，useShotTime=0.5, 则发射时间＝ 2 * 0.5 = 1秒，即所有子弹
//    // 将在最长1秒内发射完毕,实际的使用时间受shotTime影响，计算公式：
//    // (该时间不受cutTime影响)
//    private float useShotTime = 0.5f;
//    // 总共要发射的子弹数量
//    private int bulletTotal = 20;
//    // 是否射击多个目标，如果为false,则只射击当前角色的主要目标
//    // 否则射击在一定范围内的目标
//    private boolean multTarget = true;
//    
//    // ---- 内部参数 -----------
//    // 射击时间点，该数组中的时间点是trueUseTime的插值点
//    private float[] shotTimePoints;
//    private PointChecker shotChecker = new PointChecker();
//    // 在攻击技能范围内的目标，每次技能过后清空
//    private List<Actor> tempTargets;
//    
//    public ShotMultSkill(SkillData data) {
//        super(data);
//        this.useShotTime = data.getProto().getAsFloat("useShotTime", useShotTime);
//        this.bulletTotal = data.getProto().getAsInteger("bulletTotal", bulletTotal);
//        this.multTarget = data.getProto().getAsBoolean("multTarget", multTarget);
//        // 注：如果动态改变bulletTotal(set)，则需要更新shotTimePoints大小
//        this.shotTimePoints = new float[bulletTotal];
//    }
//
//    @Override
//    public void init() {
//        super.init();
//        
//        // shotTime是开始射击的时间插值点
//        // shotTimeEnd是结束射击的时间插值点。
//        float trueShotTimeStart = fixTimePointByCutTime(shotTime);
//        float trueShotTimeEnd = fixTimePointByCutTime(shotTime + useShotTime);
//        trueShotTimeEnd = MathUtils.clamp(trueShotTimeEnd, trueShotTimeStart, 1);
//        
//        // 生成实际的射击时间点(插值点)
//        float avg = (trueShotTimeEnd - trueShotTimeStart) / shotTimePoints.length;
//        for (int i = 0; i < shotTimePoints.length; i++) {
//            shotTimePoints[i] = avg * i + trueShotTimeStart;
//        }
//        shotChecker.setMaxTime(data.getTrueUseTime());
//        shotChecker.setCheckPoint(shotTimePoints);
//    }
//
//    @Override
//    protected void loopStart() {
//        super.loopStart();
//        shotChecker.rewind();
//    }
//    
//    @Override
//    protected void checkShot() {
//        while (shotChecker.nextPoint(time) != -1) {
//            doShot();
//        }
//    }
//    
//    private void doShot() {
//        Actor enemy = null;
//        if (multTarget) {
//            if (tempTargets == null || tempTargets.isEmpty()) {
//                tempTargets = actorNetwork.findNearestEnemies(actor, data.getRadius(), tempTargets);
//            }
//            if (tempTargets.size() > 0) {
//                enemy = tempTargets.get(FastMath.nextRandomInt(0, tempTargets.size() - 1));
//            }
//        } else {
//            enemy = actorNetwork.getTarget(actor);
//        }
//        
//        if (enemy != null) {
//            doShotTarget(enemy);
//        }
//    }
//    
//    @Override
//    public void cleanup() {
//        // 释放引用内存
//        if (tempTargets != null) {
//            tempTargets.clear();
//        }
//        super.cleanup();
//    }
//
//    
//}
