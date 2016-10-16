/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.bullet.Bullet;

/**
 *
 * @author huliqing
 */
public interface BulletService extends Inject {
    
    /**
     * 载入一个子弹
     * @param bulletId
     * @return 
     */
    Bullet loadBullet(String bulletId);
}
