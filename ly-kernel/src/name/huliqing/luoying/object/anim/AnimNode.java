/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.anim;

import com.jme3.scene.Node;

/**
 * 用于包装运行Anim
 * @author huliqing
 */
public class AnimNode extends Node {

    private final Anim anim;
    private boolean autoDetach;

    public AnimNode(Anim anim, boolean autoDetach) {
        this.anim = anim;
        this.autoDetach = autoDetach;
    }

    /**
     * 设置是否在动画结束时自动从场景脱离。
     * @param autoDetach 
     */
    public void setAutoDetach(boolean autoDetach) {
        this.autoDetach = autoDetach;
    }
            
    @Override
    public final void updateLogicalState(float tpf) {
        if (autoDetach && anim.isEnd()) {
            removeFromParent();
        } else {
            anim.update(tpf);
        }
    }
    
}
