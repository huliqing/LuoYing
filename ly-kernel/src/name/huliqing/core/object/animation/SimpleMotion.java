/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.animation;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.logging.Logger;

/**
 *
 * @author huliqing
 */
public abstract class SimpleMotion extends AbstractAnimation {
    private final static Logger logger = Logger.getLogger(SimpleMotion.class.getName());
    
    protected Vector3f startPos = new Vector3f();
    protected Vector3f endPos = new Vector3f();
    protected boolean useAlpha = false;
    protected boolean useScale = false;
    protected float startScale = 0.0f;
    protected float endScale = 1.0f;
    
    public SimpleMotion() {}
    
    public void setStartPosition(Vector3f startPos) {
        this.startPos.set(startPos);
    }
    
    public void setStartPosition(float x, float y, float z) {
        this.startPos.set(x, y, z);
    }
    
    public void setEndPosition(Vector3f endPos) {
        this.endPos.set(endPos);
    }
    
    public void setEndPosition(float x, float y, float z) {
        this.endPos.set(x, y, z);
    }
    
    /**
     * 设置是否使用透明过渡
     * @param useAlpha 
     */
    public void setUseAlpha(boolean useAlpha) {
        this.useAlpha = useAlpha;
    }
    
    /**
     * 是否使用缩放
     * @param useScale 
     */
    public void setUseScale(boolean useScale) {
        this.useScale = useScale;
    }
    
    /**
     * 设置缩放区间
     * @param min
     * @param max 
     */
    public void setScale(float start, float end) {
        if (start < 0 || end < 0) {
            throw new IllegalArgumentException("start and end could not less than zero!");
        }
        this.startScale = start;
        this.endScale = end;
    }

    @Override
    protected void doInit() {
        target.setLocalTranslation(startPos);
    }

    @Override
    protected void doAnimation(float tpf) {
        float factor = time / useTime;
        doMotion(target, factor);
        if (useAlpha) {
            doAlpha(target, factor);
        }
        if (useScale) {
            doScale(target, factor);
        }
    }
    
    /**
     * 处理运行,factor值范围： 0.0~1.0
     * @param factor  
     */
    protected abstract void doMotion(Spatial ui, float factor);
    
    /**
     * 取理透明度,factor值范围:0.0~1.0
     * @param ui
     * @param factor 
     */
    protected abstract void doAlpha(Spatial ui, float factor);

    /**
     * 处理缩放
     * @param ui
     * @param factor 
     */
    protected abstract void doScale(Spatial ui, float factor);
    
    @Override
    public void cleanup() {
        target.setLocalTranslation(endPos);
//        logger.log(Level.INFO, "ui end position: local={0}, world={1}"
//                , new Object[] {ui.getLocalTranslation(), ui.getWorldTranslation()});
        super.cleanup();
    }
    
}
