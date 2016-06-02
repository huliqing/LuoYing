/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.view;

import name.huliqing.fighter.data.ViewData;
import name.huliqing.fighter.object.ProtoObject;

/**
 *
 * @author huliqing
 */
public interface View extends ProtoObject<ViewData> {

    /**
     * 设置View的运行时间
     * @param useTime 
     */
    void setUseTime(float useTime);
    
}
