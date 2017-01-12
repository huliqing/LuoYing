/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.select;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author huliqing
 * @param <T>
 */
public interface SelectObj<T> {
   
    void setObject(T object);
    
    T getObject();
    
    void setLocalTranslation(Vector3f location);
    
    void setLocalRotation(Quaternion rotation);
    
    void setLocalScale(Vector3f scale);
    
    /**
     * 获取被选中的物体空间，该方法只作为只读使用,<b>不要</b>直接设置该物体的变换。
     * 
     * @return 
     */
    Spatial getReadOnlySelectedSpatial();
}
