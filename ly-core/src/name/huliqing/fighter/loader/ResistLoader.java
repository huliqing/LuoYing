/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import name.huliqing.fighter.data.ResistData;
import name.huliqing.fighter.object.resist.AllResist;
import name.huliqing.fighter.object.resist.Resist;
import name.huliqing.fighter.object.resist.SimpleResist;

/**
 *
 * @author huliqing
 */
class ResistLoader {
    
    public static Resist load(ResistData data) {
        String tagName = data.getProto().getTagName();
        Resist resist = null;
        if (tagName.equals("resistSimple")) {
            resist = new SimpleResist(data);
        } else if (tagName.equals("resistAll")) {
            resist = new AllResist(data);
        } else {
            throw new UnsupportedOperationException("Unknow tagName:" + tagName);
        }
        return resist;
    }
   
}
