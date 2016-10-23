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

    private Anim anim;

    public AnimNode() {}
    
    public AnimNode(Anim anim) {
        this.anim = anim;
    }
    
    public Anim getAnim() {
        return anim;
    }

    public void setAnim(Anim anim) {
        this.anim = anim;
    }

    @Override
    public final void updateLogicalState(float tpf) {
//        super.updateLogicalState(tpf);
        anim.update(tpf);
    }
    
}
