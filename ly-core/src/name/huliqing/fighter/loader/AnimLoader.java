/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import name.huliqing.fighter.data.AnimData;
import name.huliqing.fighter.object.DataFactory;
import name.huliqing.fighter.object.anim.Anim;
import name.huliqing.fighter.object.anim.ColorAnim;
import name.huliqing.fighter.object.anim.CurveMoveAnim;
import name.huliqing.fighter.object.anim.MoveAnim;
import name.huliqing.fighter.object.anim.RandomRotationAnim;
import name.huliqing.fighter.object.anim.RotationAnim;
import name.huliqing.fighter.object.anim.ScaleAnim;

/**
 *
 * @author huliqing
 */
class AnimLoader {
    
    public static Anim load(AnimData data) {
        String tagName = data.getTagName();
        Anim anim = null;
        if (tagName.equals("animMove")) {
            anim = new MoveAnim(data);
        } else if (tagName.equals("animCurveMove")) {
            anim = new CurveMoveAnim(data);
        } else if (tagName.equals("animRotation")) {
            anim = new RotationAnim(data);
        } else if (tagName.equals("animScale")) {
            anim = new ScaleAnim(data);
        } else if (tagName.equals("animColor")) {
            anim = new ColorAnim(data);
        } else if (tagName.equals("animRandomRotation")) {
            anim = new RandomRotationAnim(data);
        } else {
            throw new UnsupportedOperationException("Unknow supported tagName=" + tagName);
        }
        return anim;
    }    
    
    public static Anim load(String id) {
        return load((AnimData) DataFactory.createData(id));
    }
}
