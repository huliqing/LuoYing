/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.Inject;
import name.huliqing.fighter.object.bullet.Bullet;

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
