/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.game.view.transfer;

import java.util.List;
import name.huliqing.core.data.ProtoData;

/**
 * 数据传输交换接口
 * @author huliqing
 */
public interface Transfer {
    
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
    List<ProtoData> getDatas();
    
    /**
     * 设置数据列表
     * @param datas 
     */
    void setDatas(List<ProtoData> datas);
    
    /**
     * 添加数据到列表
     * @param count 要添加的数量
     */
    void addData(ProtoData data, int count);
    
    /**
     * 从列表中移除数据
     * @param itemId
     * @param count 
     */
    void removeData(ProtoData data, int count);
    
    /**
     * 通过id查找data,如果不存在则返回null.
     * @param id
     * @return 
     */
    ProtoData findData(String id);
    
    /**
     * 传输数据到目标transfer(target)
     * @param data 传输的目标数据
     * @param count 传输的数量
     */
    void transfer(ProtoData data, int count);
    
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
