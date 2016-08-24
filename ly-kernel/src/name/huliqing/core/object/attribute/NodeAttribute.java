/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.attribute;

import java.util.List;

/**
 * 节点类型的属性，这种属性可以拥有多个子属性，一般来说，
 * 只有那种需要关联其它属性的情况下才需要实现这种类型的属性.
 * @author huliqing
 */
public interface NodeAttribute {
    
    /**
     * 获取子属性列表
     * @return 
     */
    List<Attribute> getChildren();
}
