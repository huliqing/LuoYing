/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.position;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.data.PositionData;
import name.huliqing.luoying.xml.DataProcessor;

/**
 *
 * @author huliqing
 * @param <T>
 */
public interface Position<T extends PositionData> extends DataProcessor<T> {
    
    Vector3f getPoint(Vector3f store);
    
}
