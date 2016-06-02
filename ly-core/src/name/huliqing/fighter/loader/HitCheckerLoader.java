/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import name.huliqing.fighter.data.HitCheckerData;
import name.huliqing.fighter.object.hitchecker.HitChecker;
import name.huliqing.fighter.object.hitchecker.SimpleHitChecker;

/**
 *
 * @author huliqing
 */
class HitCheckerLoader {
    
    public static HitChecker load(HitCheckerData data) {
        String tagName = data.getProto().getTagName();
        if (tagName.equals("hitChecker")) {
            return new SimpleHitChecker(data);
        }
        throw new UnsupportedOperationException("tagName=" + tagName);
    }
}
