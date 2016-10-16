/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data.define;

/**
 * 可以在游戏被选择的物体
 * @author huliqing
 */
public interface PickObject {
    
    /**
     * 物体是否可点击.
     * @return 
     */
    boolean isPickable();
}
