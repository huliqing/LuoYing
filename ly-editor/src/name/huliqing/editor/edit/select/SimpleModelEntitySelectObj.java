/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.select;

import com.jme3.math.Ray;
import name.huliqing.luoying.manager.PickManager;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 普通模型的操作物体，直接操作实体本身的模型节点, 使用这类操作物体时必须确保实体存在网格模型，
 * 否则物体将无法通过鼠标来选择。
 * @author huliqing
 */
public class SimpleModelEntitySelectObj extends EntitySelectObj {

    public SimpleModelEntitySelectObj() {}
    
    public SimpleModelEntitySelectObj(Entity entity) {
        super(entity);
    }
    
    @Override
    public Float distanceOfPick(Ray ray) {
        return PickManager.distanceOfPick(ray, entity.getSpatial());
    }
}
