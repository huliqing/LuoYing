/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.Inject;
import name.huliqing.fighter.data.ViewData;
import name.huliqing.fighter.object.view.View;

/**
 *
 * @author huliqing
 */
public interface ViewService extends Inject{
    
    /**
     * 载入View
     * @param viewId
     * @return 
     */
    View loadView(String viewId);
    
    /**
     * 载入一个View
     * @param data
     * @return 
     */
    View loadView(ViewData data);
}
