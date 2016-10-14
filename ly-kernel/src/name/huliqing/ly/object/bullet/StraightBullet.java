/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.bullet;

import com.jme3.math.Vector3f;
import name.huliqing.ly.object.scene.Scene;

/**
 * 直线型子弹
 * @author huliqing
 */
public class StraightBullet extends AbstractBullet {

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
        temp.set(dir).multLocal(baseSpeed * speed * tpf);
        bulletNode.move(temp);
    }
    
    private void updateDir(Vector3f endPos) {
        endPos.subtract(bulletNode.getWorldTranslation(), dir).normalizeLocal();
        if (facing) {
            bulletNode.lookAt(endPos, Vector3f.UNIT_Y);
        }
    }

}
