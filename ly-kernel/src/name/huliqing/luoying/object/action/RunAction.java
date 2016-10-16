/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.action;

import com.jme3.math.Vector3f;

/**
 * 走路的行为,能够让角色走达到目标位置。
 * @author huliqing
 */
public interface RunAction extends Action {
    
    /**
     * 设置要行走的目标位置
     * @param positon 
     */
    public void setPosition(Vector3f positon);
    
    /**
     * 设置允许走到的最近位置,在到达该位置之内时即停止行走。
     * @param nearest 
     */
    public void setNearest(float nearest);
    
    /**
     * 判断当前是否已经行走到目标位置。只要当前角色与pos的距离小于或等于
     * nearest时就视为已经到达。
     * @param pos 
     * @return  
     */
    public boolean isInPosition(Vector3f pos);
    
    /**
     * 判断当前是否已经走到目标位置。
     * @return 
     */
    public boolean isEndPosition();
}
