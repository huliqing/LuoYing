/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.ControlData;
import name.huliqing.core.object.actor.ActorListener;

/**
 * 角色的基本控制器
 * @author huliqing
 * @param <T>
 */
public class ActorBaseControl<T extends ControlData> extends ActorControl<T>{

    // 监听角色被目标锁定/释放,被击中,被杀死或杀死目标的侦听器
    private List<ActorListener> actorListeners;
    
    @Override
    public void actorUpdate(float tpf) {}

    @Override
    public void actorRender(RenderManager rm, ViewPort vp) {}
    
     /**
     * 添加物品侦听器
     * @param actorListener 
     */
    public void addActorListener(ActorListener actorListener) {
        if (actorListeners == null) {
            actorListeners = new ArrayList<ActorListener>();
        }
        if (!actorListeners.contains(actorListener)) {
            actorListeners.add(actorListener);
        }
    }
    
    /**
     * 删除物品侦听器
     * @param actorListener
     * @return 
     */
    public boolean removeActorListener(ActorListener actorListener) {
        return actorListeners != null && actorListeners.remove(actorListener);
    }
    
    /**
     * 获取角色的物品帧听器
     * @return 
     */
    public List<ActorListener> getActorListeners() {
        return actorListeners;
    }
}
