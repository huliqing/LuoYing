/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import name.huliqing.core.data.ControlData;
import name.huliqing.core.data.SkinData;

/**
 * 角色的换装控制器
 * @author huliqing
 * @param <T>
 */
public class ActorSkinControl<T extends ControlData> extends ActorControl<T> {

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
}
