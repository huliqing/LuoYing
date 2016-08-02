/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skill;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Config;
import name.huliqing.core.Factory;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.enums.Mat;
import name.huliqing.core.game.service.EffectService;
import name.huliqing.core.game.service.PlayService;
import name.huliqing.core.game.service.SkinService;
import name.huliqing.core.manager.SoundManager;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.effect.AbstractEffect;

/**
 * 处理技能的碰撞效果
 * @author huliqing
 */
public class Collision {
    // 包含材质与效果列表
    private static List<MatMatcher> matchers;
    private final static SkinService skinService = Factory.get(SkinService.class);
    private final static PlayService playService = Factory.get(PlayService.class);
    private final static EffectService effectService = Factory.get(EffectService.class);
    
    private static void init() {
        matchers = new ArrayList<MatMatcher>();
        matchers.add(new MatMatcher(Mat.metal, Mat.metal, "effectCollisionMM"));
        // ...other
    }
    
    /**
     * 处理防守成功的碰撞效果,例如产生一些火花效果,碰撞声音
     * @param collidePos 产生碰撞的世界位置
     * @param attacker 攻击者
     * @param defender 防守者
     * @param attackMat 攻击材质，如果没有指定，则默认使用当前攻击者所使用武器的材质
     * @param defendMat 防守材质,如果没有指定，则默认使用当前防守者所使用武器的材质
     */
    public static void playDefend(Vector3f collidePos, Actor attacker, Actor defender, Mat attackMat, Mat defendMat) {
        
        List<SkinData> weaponSkinStore = skinService.getCurrentWeaponSkin(attacker);
        if (attackMat == null) {
            if (!weaponSkinStore.isEmpty()) {
                attackMat = weaponSkinStore.get(0).getProto().getMat();
            }
        }
        if (defendMat == null) {
            weaponSkinStore = skinService.getCurrentWeaponSkin(defender);
            if (!weaponSkinStore.isEmpty()) {
                defendMat = weaponSkinStore.get(0).getProto().getMat();
            }
        }
        
        SoundManager.getInstance().playCollision(attackMat, defendMat, collidePos);
        
        // playEffect
        if (matchers == null) {
            init();
        }
        for (MatMatcher matcher : matchers) {
            if (matcher.match(attackMat, defendMat)) {
                AbstractEffect effect = (AbstractEffect) effectService.loadEffect(matcher.effectId);
                
                // remove20160517
//                effect.setLocation(collidePos);
//                effect.getLocalRotation().lookAt(attacker.getViewDirection(), Vector3f.UNIT_Y);
                
                effect.setLocalTranslation(collidePos);
                effect.getLocalRotation().lookAt(attacker.getViewDirection(), Vector3f.UNIT_Y);
                playService.addEffect(effect);
                return;
            }
        }
        if (Config.debug) {
            Logger.getLogger(Collision.class.getName()).log(Level.WARNING, "No collision effect match for mat1={0}, mat2={1}"
                    , new Object[] {attackMat, defendMat});
        }
    }
    
    private static class MatMatcher {
        Mat mat1;
        Mat mat2;
        final String effectId;
        public MatMatcher(Mat mat1, Mat mat2, String effectId) {
            this.mat1 = mat1;
            this.mat2 = mat2;
            this.effectId = effectId;
        }
        
        boolean match(Mat mat1, Mat mat2) {
            return (this.mat1 == mat1 && this.mat2 == mat2) 
                    || (this.mat1 == mat2 && this.mat2 == mat1);
        }
    }
}
