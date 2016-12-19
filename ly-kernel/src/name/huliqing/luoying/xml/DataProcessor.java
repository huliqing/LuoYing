/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.xml;

/**
 * DataProcessor数据处理器
 * @author huliqing
 * @param <T>
 */
public interface DataProcessor<T extends ObjectData> {
 
    /**
     * 设置Data，这个data包含了DataProcessor要使用的所有可能的数据，在创建DataProcessor的时候这个方法会被自
     * 动调用，在运行时用户代码不应该再去改变这个data的引用。
     * @param data 
     */
    void setData(T data);
    
    /**
     * 获取data
     * @return 
     */
    T getData();

    /**
     * 更新实时状态数据到data中去,保持data的数据为当前的实时状态. <br>
     * 也就是说DataProcessor在运行时状态可能发生各种各样的变化，但是不需要实时更新到data中去，
     * 只在在调用这个方法时，才需要把当前状态更新到data中去，通常比如在存档之前都应该调用一次这个方法来将当前状
     * 态保存到data中去。
     */
    void updateDatas();
}
