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
import name.huliqing.core.object.actor.ItemListener;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class ActorItemControl <T extends ControlData> extends ActorControl<T> {
    
    // 监听角色物品的增删
    private List<ItemListener> itemListeners;

    @Override
    public void actorUpdate(float tpf) {}
    
    @Override
    public void actorRender(RenderManager rm, ViewPort vp) {}
    
    @Override
    public void cleanup() {
        super.cleanup();
    }
    
    /**
     * 添加物品侦听器
     * @param itemListener 
     */
    public void addItemListener(ItemListener itemListener) {
        if (itemListeners == null) {
            itemListeners = new ArrayList<ItemListener>();
        }
        if (!itemListeners.contains(itemListener)) {
            itemListeners.add(itemListener);
        }
    }
    
    /**
     * 删除物品侦听器
     * @param itemListener
     * @return 
     */
    public boolean removeItemListener(ItemListener itemListener) {
        return itemListeners != null && itemListeners.remove(itemListener);
    }
    
    /**
     * 获取角色的物品帧听器
     * @return 
     */
    public List<ItemListener> getItemListeners() {
        return itemListeners;
    }
    
}
