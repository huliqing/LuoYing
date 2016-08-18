/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view.transfer;

import java.util.List;

/**
 * 数据传输交换接口
 * @author huliqing
 * @param <T>
 */
public interface Transfer<T> {
    
    /**
     * 设置要传输的目标对象
     * @param target 
     */
    void setTarget(Transfer target);
    
    /**
     * 获取要传输的目标对象
     * @return 
     */
    Transfer getTarget();
    
    /**
     * 获取数据列表
     * @return 
     */
    List<T> getDatas();
    
    /**
     * 设置数据列表
     * @param datas 
     */
    void setDatas(List<T> datas);
    
    /**
     * 添加数据到列表
     * @param data
     * @param count 要添加的数量
     */
    void addData(T data, int count);
    
    /**
     * 从列表中移除数据
     * @param data
     * @param count 
     */
    void removeData(T data, int count);
    
    /**
     * 通过id查找data,如果不存在则返回null.
     * @param id
     * @return 
     */
    T findData(String id);
    
    /**
     * 传输数据到目标transfer(target)
     * @param data 传输的目标数据
     * @param count 传输的数量
     */
    void transfer(T data, int count);
    
    /**
     * 添加数据传输侦听器
     * @param listener 
     */
    void addListener(TransferListener listener);
    
    /**
     * 移除数据传输侦听器
     * @param listener
     * @return 
     */
    boolean removeListener(TransferListener listener);
    
    
}
