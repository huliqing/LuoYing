/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.Inject;
import name.huliqing.luoying.data.ViewData;
import name.huliqing.luoying.object.view.View;

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
