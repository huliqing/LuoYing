/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.bullet;

import com.jme3.math.Vector3f;
import name.huliqing.core.data.BulletData;

/**
 * 直线型子弹
 * @author huliqing
 * @param <T>
 */
public class StraightBullet<T extends BulletData, S> extends AbstractBullet<T, S> {

    private final Vector3f dir = new Vector3f();
    private final Vector3f temp = new Vector3f();
    
    @Override
    public void initialize() {
        super.initialize();
        updateDir(getCurrentEndPos());
    }

    @Override
    protected void doUpdatePosition(float tpf, Vector3f endPos) {
        if (trace) {
            updateDir(endPos);
        }
        temp.set(dir).multLocal(baseSpeed * data.getSpeed() * tpf);
        move(temp);
    }
    
    private void updateDir(Vector3f endPos) {
        endPos.subtract(getWorldTranslation(), dir).normalizeLocal();
        if (facing) {
            lookAt(endPos, Vector3f.UNIT_Y);
        }
    }
}
