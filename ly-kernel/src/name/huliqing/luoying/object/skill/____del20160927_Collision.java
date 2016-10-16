package name.huliqing.luoying.object.skill;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.object.skill;
//
//import com.jme3.math.Vector3f;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import name.huliqing.core.Config;
//import name.huliqing.core.Factory;
//import name.huliqing.core.enums.Mat;
//import name.huliqing.core.mvc.service.ActorService;
//import name.huliqing.core.mvc.service.EffectService;
//import name.huliqing.core.mvc.service.PlayService;
//import name.huliqing.core.mvc.service.SkinService;
//import name.huliqing.core.object.actor.Actor;
//import name.huliqing.core.object.effect.AbstractEffect;
//import name.huliqing.core.object.skin.Skin;
//import name.huliqing.core.object.skin.Weapon;
//import name.huliqing.core.object.sound.SoundManager;
//
///**
// * 处理技能的碰撞效果
// * @author huliqing
// */
//public class Collision {
//    // 包含材质与效果列表
//    private static List<MatMatcher> matchers;
//    private final static SkinService skinService = Factory.get(SkinService.class);
//    private final static PlayService playService = Factory.get(PlayService.class);
//    private final static EffectService effectService = Factory.get(EffectService.class);
//    private final static ActorService actorService = Factory.get(ActorService.class);
//    
//    private static void init() {
//        matchers = new ArrayList<MatMatcher>();
//        matchers.add(new MatMatcher(Mat.metal, Mat.metal, "effectCollisionMM"));
//        // ...other
//    }
//    
//    /**
//     * 处理防守成功的碰撞效果,例如产生一些火花效果,碰撞声音
//     * @param collidePos 产生碰撞的世界位置
//     * @param attacker 攻击者
//     * @param defender 防守者
//     * @param attackMat 攻击材质，如果没有指定，则默认使用当前攻击者所使用武器的材质
//     * @param defendMat 防守材质,如果没有指定，则默认使用当前防守者所使用武器的材质
//     */
//    public static void playDefend(Vector3f collidePos, Actor attacker, Actor defender, Mat attackMat, Mat defendMat) {
//        
//        if (attackMat == null) {
//            Skin attackerWeaponSkin = getFirstWeaponSkin(attacker);
//            if (attackerWeaponSkin != null) {
//                attackMat = attackerWeaponSkin.getData().getMat();
//            }
//        }
//        if (defendMat == null) {
//            Skin defendWeaponSkin = getFirstWeaponSkin(defender);
//            if (defendWeaponSkin != null) {
//                defendMat = defendWeaponSkin.getData().getMat();
//            }
//        }
//        
//        SoundManager.getInstance().playCollision(attackMat, defendMat, collidePos);
//        
//        // playEffect
//        if (matchers == null) {
//            init();
//        }
//        for (MatMatcher matcher : matchers) {
//            if (matcher.match(attackMat, defendMat)) {
//                AbstractEffect effect = (AbstractEffect) effectService.loadEffect(matcher.effectId);
//                effect.setLocalTranslation(collidePos);
//                effect.getLocalRotation().lookAt(actorService.getViewDirection(attacker), Vector3f.UNIT_Y);
//                playService.addEffect(effect);
//                return;
//            }
//        }
//        if (Config.debug) {
//            Logger.getLogger(Collision.class.getName()).log(Level.WARNING, "No collision effect match for mat1={0}, mat2={1}"
//                    , new Object[] {attackMat, defendMat});
//        }
//    }
//    
//    private static Skin getFirstWeaponSkin(Actor actor) {
//         // 伤害声音
//        List<Skin> skins = skinService.getUsingSkins(actor);
//        if (skins != null) {
//            for (Skin s : skins) {
//                if (s instanceof Weapon) {
//                    return s;
//                }
//            }
//        }
//        return null;
//    }
//    
//    private static class MatMatcher {
//        Mat mat1;
//        Mat mat2;
//        final String effectId;
//        public MatMatcher(Mat mat1, Mat mat2, String effectId) {
//            this.mat1 = mat1;
//            this.mat2 = mat2;
//            this.effectId = effectId;
//        }
//        
//        boolean match(Mat mat1, Mat mat2) {
//            return (this.mat1 == mat1 && this.mat2 == mat2) 
//                    || (this.mat1 == mat2 && this.mat2 == mat1);
//        }
//    }
//}
