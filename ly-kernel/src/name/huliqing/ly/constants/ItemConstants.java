/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.constants;

/**
 * 
 * @author huliqing
 */
public class ItemConstants {
    
    /** 当前物品可以正常使用，只有物品报这个状态码时才可以让角色正常使用。 */
    public final static int STATE_OK = -1;
    
    /** 未定义的状态类型 */
    public final static int STATE_UNDEFINE = 0;
    
    /** 物品数量不足 */
    public final static int STATE_ITEM_NOT_ENOUGH = 1;
    
    /** 
     * 属性值不满足，这表示一些物品在使用的时候需要满足匹配某些属性，或消耗一定数量的属性值,
     * 如果角色不存在某个属性或者当前状态属性值不满足指定要求则会报这个状态。 
     */
    public final static int STATE_ATTRIBUTE_NOT_MATCH = 2; 
    
    
}
