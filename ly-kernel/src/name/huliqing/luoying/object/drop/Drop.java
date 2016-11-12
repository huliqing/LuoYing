/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.drop;

import name.huliqing.luoying.data.DropData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * Drop用于处理角色互相伤害后物品、经验等的掉落。当角色A攻击了角色B,并致B死亡时，
 * B可能会掉落各种各样的物品或经验给角色A.
 * @author huliqing
 */
public interface Drop extends DataProcessor<DropData> {
    
    /**
     * 处理掉落物品，物品从source掉落, 如果有任何物品成功掉落则返回true,否则返回false.
     * 注意：目标target标记着是谁对source造成了伤害致死，并造成source掉落物品的角色，
     * 这个角色有可能为null的，当source是被一个不实际存在的角色打倒时，如GM、一些未知释放源的魔法、状态致死时，
     * target传入的有可能会是null,当出现这种情况时，根据实现类的情况而定，可以直接将物品掉落到地上，
     * 或者不掉落。
     * @param source 掉落源
     * @param target 接受掉落物品的角色,注意：这个目标有可能为null.
     * @return 如果有任何物品成功掉落则返回true,否则返回false.
     */
    boolean doDrop(Entity source, Entity target);
}
