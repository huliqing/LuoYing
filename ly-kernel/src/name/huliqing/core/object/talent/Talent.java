/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.talent;

import name.huliqing.core.data.TalentData;
import name.huliqing.core.object.DataProcessor;
import name.huliqing.core.object.actor.Actor;

/**
 * 天赋，注：天赋是不会结束的，一旦获得就一直存在，除非手动移除。
 * 所以没有isEnd()方法。
 * @author huliqing
 * @param <T>
 */
public interface Talent<T extends TalentData> extends DataProcessor<T> {
    
    /**
     * 初始化天赋,当给指定角色添加天赋时该方法应该被调用一次，以进行初始化。
     */
    void init();
    
    /**
     * 天赋逻辑
     * @param tpf 
     */
    void update(float tpf);
    
    /**
     * 清理并释放资源，当天赋从目标身上移除时应该执行一次该方法。
     */
    void cleanup();
    
    void setActor(Actor actor);
    
    /**
     * 更新天赋到指定的级别
     * @param level 
     */
    void updateLevel(int level);
}
