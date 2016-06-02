/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import name.huliqing.fighter.data.ActorAnimData;
import name.huliqing.fighter.object.DataLoaderFactory;
import name.huliqing.fighter.object.actoranim.ActorAnim;
import name.huliqing.fighter.object.actoranim.ActorCurveMove;

/** 
 *
 * @author huliqing
 */
class ActorAnimLoader {
    
    public static ActorAnim load(ActorAnimData data) {
        String tagName = data.getProto().getTagName();
        ActorAnim anim = null;
        if (tagName.equals("curveMove")) {
            anim = new ActorCurveMove(data);
        } 
        return anim;
    }    
    
    public static ActorAnim load(String id) {
        return load(DataLoaderFactory.createActorAnimData(id));
    }
}
