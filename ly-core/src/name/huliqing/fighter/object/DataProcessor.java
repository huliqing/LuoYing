/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object;

import name.huliqing.fighter.data.ProtoData;

/**
 *
 * @author huliqing
 */
public interface DataProcessor<T extends ProtoData> {

    /**
     * 获取Data
     * @return 
     */
    T getData();
    
    /**
     * 初始化Data.该方法只允许DataFactory调用,而且一般应该只设置一次，运行
     * 时不应该再调用。
     * @param data 
     */
    void initData(T data);


}
