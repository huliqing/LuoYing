/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.xml;


/**
 * XML数据载入器
 * @author huliqing
 * @param <T>
 */
public interface DataLoader<T extends ObjectData> { 
    
    /**
     * 载入数据，从proto中载入指定数据到store中。
     * @param proto
     * @param data 
     */
    void load(Proto proto, T data);
    
}
