/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import name.huliqing.core.object.Loader;
import name.huliqing.core.object.bullet.Bullet;

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
        return Loader.load(bulletId);
    }
    
}
