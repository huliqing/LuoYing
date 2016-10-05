/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.resist;

import java.util.List;
import name.huliqing.ly.data.ResistData;
import name.huliqing.ly.xml.DataProcessor;

/**
 * 角色的抗性控制，抗性决定了角色是否会接受各种各样的状态的影响．
 * 抗性可分为：抵抗和免疫，抵抗表示机会性的；而免疫表示绝对的。
 * @author huliqing
 * @param <T>
 */
public interface Resist<T extends ResistData> extends DataProcessor<T>{

    @Override
    public void setData(T data);

    @Override
    public T getData();
    
    /**
     * 获取指定状态的抗性值,如果不存在指定状态的抗性设置，则返回0.
     * @param stateId
     * @return 抗性值[0.0~1.0]
     */
    float getResist(String stateId);
    
    /**
     * 增加或降低指定的抗性值，如果指定的状态抗性不存在，则不作任何处理,
     * 并返回0.
     * @param stateId
     * @param amount 增加(正值）或减少(负值)
     * @return 返回实际增加或减少的数量
     */
    float applyResist(String stateId, float amount);
    
    /**
     * 获取所有的抵抗设置，重要：返回的列表只允许读取，不允许直接设置其中内容．
     * @return 
     */
    List<StateResist> getAll();
    
}
