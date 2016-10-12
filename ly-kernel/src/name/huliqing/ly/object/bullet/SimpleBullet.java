/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.bullet;

import com.jme3.math.Vector3f;

/**
 * 直接击中类型的子弹，不需要经过中间路径。
 * @author huliqing
 */
public class SimpleBullet extends AbstractBullet {
    
    @Override
    protected void doUpdatePosition(float tpf, Vector3f endPos) {
        bulletNode.setLocalTranslation(endPos);
    }

    
}
