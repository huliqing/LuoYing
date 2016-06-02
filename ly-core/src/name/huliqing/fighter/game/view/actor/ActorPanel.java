/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.view.actor;

import name.huliqing.fighter.object.actor.Actor;

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
