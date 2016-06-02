/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import name.huliqing.fighter.data.BulletData;
import name.huliqing.fighter.object.bullet.Bullet;
import name.huliqing.fighter.object.bullet.CurveBullet;
import name.huliqing.fighter.object.bullet.CurveTrailBullet;
import name.huliqing.fighter.object.bullet.SimpleBullet;
import name.huliqing.fighter.object.bullet.StraightBullet;

/**
 *
 * @author huliqing
 */
class BulletLoader {
    
    public static Bullet load(BulletData data) {
        String tagName = data.getTagName();
        
        // remove
//        AbstractBullet result;
//        if (tagName.equals("straight")) {
//            result = new StraightBullet(data);
//        } else if (tagName.equals("curve")) {
//            result = new CurveBullet(data);
//        } else {
//            throw new UnsupportedOperationException("Unknow supported tagName=" + tagName);
//        }
        
        if (tagName.equals("bulletSimple")) {
            return new SimpleBullet(data);
        }
        
        if (tagName.equals("bulletStraight")) {
            return new StraightBullet(data);
        } 
        
        if (tagName.equals("bulletCurve")) {
            return new CurveBullet(data);
        } 
        
        if (tagName.equals("bulletCurveTrail")) {
            return new CurveTrailBullet(data);
        }
        
        throw new UnsupportedOperationException("Unknow supported tagName=" + tagName);
        
    }
}
