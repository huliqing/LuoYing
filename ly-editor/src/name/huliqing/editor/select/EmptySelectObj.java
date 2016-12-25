/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.select;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * 用于默认的没有选择任何物体时的情形, 避免NullPoint
 * @author huliqing
 */
public class EmptySelectObj implements SelectObj {

    private final Node emptNode = new Node();
    
    /**
     * 设置物体对方法不会有任何作用
     * @param object 
     */
    @Override
    public void setObject(Object object) {
        // ignore
    }

    /**
     * 物方法将始终返回null.
     * @return 
     */
    @Override
    public Object getObject() {
        return null;
    }

    @Override
    public Spatial getSelectedSpatial() {
        return emptNode;
    }

    
}
