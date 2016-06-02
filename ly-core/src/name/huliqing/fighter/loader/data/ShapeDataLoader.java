/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader.data;

import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.data.ShapeData;

/**
 *
 * @author huliqing
 */
public class ShapeDataLoader implements DataLoader<ShapeData> {

    @Override
    public ShapeData loadData(Proto proto) {
        ShapeData data = new ShapeData(proto.getId());
        return data;
    }
    
}
