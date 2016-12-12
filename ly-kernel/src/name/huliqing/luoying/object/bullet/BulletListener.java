/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.bullet;

/**
 * @author huliqing
 */
public interface BulletListener {
    
    /**
     * 在子弹的飞行过程中，该方法会持续被调用,可以在这个方法中来检测子弹是否击中一个目标，
     * 如果击中了一个目标，则返回true,否则返回false. 如果击中了一个目标，并希望让子弹结束，
     * 可以调用 {@link #consume() }方法来销毁子弹。
     * @param bullet 
     * @return  
     */
    boolean onBulletFlying(Bullet bullet);
    
}
