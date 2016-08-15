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
import name.huliqing.core.data.SkinData;
import name.huliqing.core.object.actor.SkinListener;

/**
 * 角色的换装控制器
 * @author huliqing
 * @param <T>
 */
public class ActorSkinControl<T extends ControlData> extends ActorControl<T> {

    // 监听角色装备、武器等的穿脱
    private List<SkinListener> skinListeners;
    
    @Override
    public void actorUpdate(float tpf) {
        // ignore
    }
    
    @Override
    public void actorRender(RenderManager rm, ViewPort vp) {
        // ignore
    }
    
    /**
     * 给角色换上装备,注：换装备的时候需要考虑冲突的装备，并把冲突的装备换
     * 下来
     * @param skinData 
     */
    public void attachSkin(SkinData skinData) {
        
    }
    
    public void addSkinListener(SkinListener skinListener) {
        if (skinListeners == null) {
            skinListeners = new ArrayList<SkinListener>();
        }
        if (!skinListeners.contains(skinListener)) {
            skinListeners.add(skinListener);
        }
    }

    public boolean removeSkinListener(SkinListener skinListener) {
        return skinListeners != null && skinListeners.remove(skinListener);
    }

    public List<SkinListener> getSkinListeners() {
        return skinListeners;
    }
}
