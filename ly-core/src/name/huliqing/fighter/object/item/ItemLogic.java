/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.item;

import name.huliqing.fighter.data.ActorLogicData;
import name.huliqing.fighter.object.IntervalLogic;

/**
 * @deprecated 20160310不再使用
 * 简单物体的逻辑
 * @author huliqing
 */
public abstract class ItemLogic extends IntervalLogic {
    
    protected Item self;
    
    public ItemLogic() {}
    
    public ItemLogic(ActorLogicData logicData) {}

    public Item getSelf() {
        return self;
    }
        
    public void setSelf(Item self) {
        this.self = self;
    }
    
}
