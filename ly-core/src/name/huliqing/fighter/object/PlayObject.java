/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object;

import com.jme3.app.Application;

/**
 * 带有逻辑行为的物体（不要去继承PlayObject,保持与PlayObject的平行关系。）
 * @author huliqing
 */
public interface PlayObject {

    /**
     * 初始化物体
     * @param app
     */
    public void initialize(Application app);

    /**
     * 判断是否已经初始化
     * @return 
     */
    public boolean isInitialized();

    /**
     * 更新Object的logic
     * @param tpf 
     */
    void update(float tpf);

    /**
     * 清理Logic产生的资源，当该方法被调用的时候Object已经被移出更新队列。
     */
    void cleanup();
}
