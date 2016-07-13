/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter;

import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.loader.ObjectLoader;

/**
 *
 * @author huliqing
 */
public class TestFactory {
    
    public static void preTest() {
        
        testSkill("skillDanceSakura");
        testSkill("skillLightningShot");
        testSkill("skillIceShot");
        testSkill("skillBack");
        testSkill("skillDualSwordWave");
        testSkill("skillShotCleanBuff");
        
    }
    
    private static void testSkill(String skillId) {
        Proto proto = ObjectLoader.findObjectDef(skillId);
        proto.setAttribute("cooldown", 1);
        proto.setAttribute("useAttributes", null);
        proto.setAttribute("hitDistance", 3000);
//        proto.putAttribute("shotSpeed", "2");
    }
}
