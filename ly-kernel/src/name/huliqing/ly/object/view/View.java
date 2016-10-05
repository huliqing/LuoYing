/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.view;

import name.huliqing.ly.data.ViewData;
import name.huliqing.ly.xml.DataProcessor;

/**
 *
 * @author huliqing
 * @param <T>
 */
public interface View<T extends ViewData> extends DataProcessor<T> {

    /**
     * 设置View的运行时间
     * @param useTime 
     */
    void setUseTime(float useTime);
    
    /**
     * 获得实时更新的数据
     * @return 
     */
    T getUpdateData();
}
