/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.bullet;

import com.jme3.math.Vector3f;
import name.huliqing.core.data.BulletData;

/**
 * 直接击中类型的子弹，不需要经过中间路径。
 * @param <T>
 * @author huliqing
 */
public class SimpleBullet<T extends BulletData, S> extends AbstractBullet<T, S>{
    
    @Override
    protected void doUpdatePosition(float tpf, Vector3f endPos) {
        setLocalTranslation(endPos);
    }
    
}
