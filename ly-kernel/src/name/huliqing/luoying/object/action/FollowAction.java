/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.action;

import com.jme3.scene.Spatial;

/**
 *
 * @author huliqing
 */
public interface FollowAction extends Action {
    
    /**
     * 设置要跟随的目标
     * @param target 
     */
    public void setFollow(Spatial target);
    
    /**
     * 设置跟随的最近距离，当跟随到该目标以内时，即停止跟随，该值不需要太小。
     * 也不能小于0。
     * @param nearest 
     */
    public void setNearest(float nearest);
}
