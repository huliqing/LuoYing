/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.service;

import name.huliqing.core.object.bullet.Bullet;
import name.huliqing.core.object.bullet.BulletCache;

/**
 *
 * @author huliqing
 */
public class BulletServiceImpl implements BulletService {

    @Override
    public void inject() {
        // ...
    }

    @Override
    public Bullet loadBullet(String bulletId) {
        return BulletCache.getInstance().getBullet(bulletId);
    }
    
}
