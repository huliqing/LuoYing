/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import name.huliqing.fighter.data.TalentData;
import name.huliqing.fighter.object.talent.AttributeTalent;
import name.huliqing.fighter.object.talent.Talent;

/**
 * 天赋
 * @author huliqing
 */
class TalentLoader {
    
    public static Talent load(TalentData data) {
        
        String tagName = data.getProto().getTagName();
        
        if (tagName.equals("talentAttribute")) {
            
            return new AttributeTalent(data);
            
        }
        
        throw new UnsupportedOperationException("TalentLoader Unknow type, tagName=" + tagName);
    }
}
