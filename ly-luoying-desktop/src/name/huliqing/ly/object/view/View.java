/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.view;

import name.huliqing.ly.data.ViewData;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 * @param <T>
 */
public interface View<T extends ViewData> extends Entity<T> {

    /**
     * 设置View的运行时间
     * @param useTime 
     */
    void setUseTime(float useTime);
    
}
