/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.item;

import name.huliqing.ly.object.entity.Entity;

/**
 * 没有任何属性的物品。
 * @author huliqing
 */
public class SimpleItem extends AbstractItem {

    @Override
    public void use(Entity actor) {
        super.use(actor);
        
        // do nothing.
    }
    
}
