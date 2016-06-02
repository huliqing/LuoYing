/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.bullet;


/**
 *
 * @author huliqing
 */
public interface BulletListener {
    
    /**
     * 检查子弹的碰撞,如果发生碰撞则返回true,否则返回false.
     * @param bullet
     * @return 
     */
    boolean hitCheck(Bullet bullet);
    
}
