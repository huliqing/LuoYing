/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.bullet;

import com.jme3.math.Vector3f;
import name.huliqing.fighter.data.BulletData;

/**
 * 直线型子弹
 * @author huliqing
 */
public class StraightBullet extends AbstractBullet {

    private final Vector3f dir = new Vector3f();
    private final Vector3f temp = new Vector3f();
    
    public StraightBullet(BulletData data) {
        super(data);
    }

    @Override
    protected void doInit() {
        super.doInit();
        updateDir(getCurrentEndPos());
    }

    @Override
    protected void doUpdatePosition(float tpf, Vector3f endPos) {
        if (trace) {
            updateDir(endPos);
        }
        temp.set(dir).multLocal(baseSpeed * speed * tpf);
        move(temp);
    }
    
    private void updateDir(Vector3f endPos) {
        endPos.subtract(getWorldTranslation(), dir).normalizeLocal();
        if (facing) {
            lookAt(endPos, Vector3f.UNIT_Y);
        }
    }
}
