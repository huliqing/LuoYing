/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.constants;

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
    
    /** 无法通过CheckEl检查 */
    public final static int STATE_CHECK_EL = 2; 
    
    
}
