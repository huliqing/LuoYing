/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.actor;

import name.huliqing.ly.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public interface ActorPanel {
    
    /**
     * 设置是否显示面板
     * @param visible 
     */
    public void setPanelVisible(boolean visible);
    
    /**
     * 更新面板信息
     */
    public void setPanelUpdate(Actor actor);
    
}
