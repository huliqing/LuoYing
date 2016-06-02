/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.object.bullet.Bullet;
import name.huliqing.fighter.object.bullet.BulletCache;

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
