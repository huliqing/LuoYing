/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.xml;

/**
 *
 * @author huliqing
 * @param <T>
 */
public interface DataProcessor<T extends ProtoData> {

    /**
     * 设置data
     * @param data 
     */
    void setData(T data);
    
    /**
     * 获取data
     * @return 
     */
    T getData();

    /**
     * 更新实时状态数据到data中去,保持data的数据为当前的实时状态.
     */
    void updateDatas();
}
