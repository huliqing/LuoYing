/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying;

import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.xml.Proto;

/**
 *
 * @author huliqing
 */
public class TestFactory {
    
    public static void preTest() {
        
        testSkill("skillDanceSakura");
        testSkill("skillLightningShot");
        testSkill("skillBack");
        testSkill("skillDualSwordWave");
        testSkill("skillShotCleanBuff");
        
        testSkill("skillIceShot");
        testSkill("skillShotLight");
    }
    
    private static void testSkill(String skillId) {
        Proto proto = DataFactory.getProto(skillId);
        proto.setAttribute("cooldown", 5);
        proto.setAttribute("useAttributes", null);
        proto.setAttribute("hitDistance", 3000);
//        proto.putAttribute("shotSpeed", "2");
//        proto.setAttribute("useTime", 2);
    }
}
