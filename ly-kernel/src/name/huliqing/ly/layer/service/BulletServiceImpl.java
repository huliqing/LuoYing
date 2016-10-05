/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.bullet.Bullet;

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
