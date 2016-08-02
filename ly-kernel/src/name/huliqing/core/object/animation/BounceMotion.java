/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.animation;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.logging.Logger;

/**
 * 弹跳的动画效果
 * @author huliqing
 */
public class BounceMotion extends AbstractAnimation {
    private final static Logger logger = Logger.getLogger(BounceMotion.class.getName());

    private float height = 50;
    // UI的原始位置
    private Vector3f origin = new Vector3f();
    
    public void setHeight(float height) {
        this.height = height;
    }
    
    @Override
    protected void doInit() {
        origin.set(target.getLocalTranslation());
//        logger.log(Level.INFO, "ui end position: local={0}, world={1}"
//                , new Object[] {ui.getLocalTranslation(), ui.getWorldTranslation()});
    }

    @Override
    protected void doAnimation(float tpf) {
        float sineFactor = FastMath.sin(time / useTime * FastMath.PI);
        if (sineFactor < 0) {
            return;
        }
        TempVars tv = TempVars.get();
        float yOffset = height * sineFactor;
        target.setLocalTranslation(tv.vect1.set(origin).addLocal(0, yOffset, 0));
        tv.release();
    }

    @Override
    public void cleanup() {
        // 1.do self cleanup...
        target.setLocalTranslation(origin);
        // 2.do super cleanup
        super.cleanup(); 
    }
    
    
}
