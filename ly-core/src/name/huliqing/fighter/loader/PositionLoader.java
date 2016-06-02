/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import name.huliqing.fighter.data.PositionData;
import name.huliqing.fighter.object.position.FixedPosition;
import name.huliqing.fighter.object.position.Position;
import name.huliqing.fighter.object.position.RandomBoxPosition;
import name.huliqing.fighter.object.position.RandomCirclePosition;
import name.huliqing.fighter.object.position.RandomSpherePosition;
import name.huliqing.fighter.object.position.ViewPosition;

/**
 *
 * @author huliqing
 */
class PositionLoader {
    
    public static Position load(PositionData data) {
        String tagName = data.getTagName();
        if ("point".equals(tagName)) {
            return new FixedPosition(data);
        } else if ("box".equals(tagName)) {
            return new RandomBoxPosition(data);
        } else if ("sphere".equals(tagName)) {
            return new RandomSpherePosition(data);
        } else if ("circle".equals(tagName)) {
            return new RandomCirclePosition(data);
        } else if ("positionView".equals(tagName)) {
            return new ViewPosition(data);
        }
        
        throw new UnsupportedOperationException("tagName=" + tagName);
        
    }
    
    // remove
//    private static EmitterShape createPointShape(PositionData esd) {
//        EmitterPointShape eps = new EmitterPointShape(esd.getAsVector3f("point"));
//        return eps;
//    }
//    
//    private static EmitterShape createBoxShape(PositionData esd) {
//        EmitterBoxShape ebs = new EmitterBoxShape(esd.getAsVector3f("min")
//                , esd.getAsVector3f("max"));
//        return ebs;
//    }
//    
//    private static EmitterShape createSphereShape(PositionData esd) {
//        EmitterSphereShape ebs = new EmitterSphereShape(esd.getAsVector3f("center")
//                , esd.getAsFloat("radius", 1));
//        return ebs;
//    }
    
 
}
