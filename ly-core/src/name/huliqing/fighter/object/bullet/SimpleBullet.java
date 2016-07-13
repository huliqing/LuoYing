/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.bullet;

import com.jme3.math.Vector3f;
import name.huliqing.fighter.data.BulletData;

/**
 * 直接击中类型的子弹，不需要经过中间路径。
 * @param <T>
 * @author huliqing
 */
public class SimpleBullet<T extends BulletData> extends AbstractBullet<T>{
    
    @Override
    protected void doUpdatePosition(float tpf, Vector3f endPos) {
        setLocalTranslation(endPos);
    }
    
}
