/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import name.huliqing.fighter.data.ShapeData;
import name.huliqing.fighter.object.shape.BoxShape;
import name.huliqing.fighter.object.shape.Shape;

/**
 *
 * @author huliqing
 */
public class ShapeLoader {
    
    public static Shape load(ShapeData data) {
        String tagName = data.getProto().getTagName();
        if (tagName.equals("shapeBox")) {
            return new BoxShape(data);
        }
        throw new UnsupportedOperationException("Unknow tagname=" + tagName);
    }
}
