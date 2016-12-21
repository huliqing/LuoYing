/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
        testSkill("skillShotReborn");
    }
    
    private static void testSkill(String skillId) {
        Proto proto = DataFactory.getProto(skillId);
//        proto.setAttribute("cooldown", 1);
//        proto.setAttribute("useAttributes", null);
//        proto.setAttribute("hitDistance", 3000);
//        proto.putAttribute("shotSpeed", "2");
//        proto.setAttribute("useTime", 2);
    }
}
