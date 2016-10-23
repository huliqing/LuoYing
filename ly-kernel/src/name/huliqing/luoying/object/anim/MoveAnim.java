/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.anim;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import name.huliqing.luoying.data.AnimData;

/**
 * 直线移动的动画
 * @author huliqing
 */
public final class MoveAnim extends AbstractAnim<Spatial> {
//    private final static Logger LOG = Logger.getLogger(MoveAnim.class.getName());

    // 开始位置及结束位置
    private Vector3f startPos;
    private Vector3f endPos;
    private Vector3f startPosOffset;
    private Vector3f endPosOffset;
    // 是否朝向目标
    private boolean facing;
    
    // ---- inner
    private final Vector3f trueStartPos = new Vector3f();
    private final Vector3f trueEndPos = new Vector3f();
    
    @Override
    public void setData(AnimData data) {
        super.setData(data);
        this.startPos = data.getAsVector3f("startPos");
        this.endPos = data.getAsVector3f("endPos");
        this.startPosOffset = data.getAsVector3f("startPosOffset");
        this.endPosOffset = data.getAsVector3f("endPosOffset");
        this.facing = data.getAsBoolean("facing", facing);
    }
    
    @Override
    protected void doInit() {
        trueStartPos.set(startPos != null ? startPos : target.getWorldTranslation());
        if (startPosOffset != null) {
            trueStartPos.addLocal(startPosOffset);
        }
        
        trueEndPos.set(endPos != null ? endPos : target.getWorldTranslation());
        if (endPosOffset != null) {
            trueEndPos.addLocal(endPosOffset);
        }
        
        target.setLocalTranslation(trueStartPos);
        
        if (facing) {
            target.lookAt(trueEndPos, Vector3f.UNIT_Y);
        }
    }

    @Override
    protected void doAnimation(float interpolation) {
        TempVars tv = TempVars.get();
        FastMath.extrapolateLinear(interpolation, trueStartPos, trueEndPos, tv.vect1);
        target.setLocalTranslation(tv.vect1);
        tv.release();
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }

//    public Vector3f getStartPos() {
//        return startPos;
//    }

    public void setStartPos(Vector3f startPos) {
        if (this.startPos == null) {
            this.startPos = new Vector3f();
        }
        this.startPos.set(startPos);
    }

//    public Vector3f getEndPos() {
//        return endPos;
//    }

    public void setEndPos(Vector3f endPos) {
        if (this.endPos == null) {
            this.endPos = new Vector3f();
        }
        this.endPos.set(endPos);
    }

//    public boolean isFacing() {
//        return facing;
//    }
//
//    public void setFacing(boolean facing) {
//        this.facing = facing;
//    }

    
}
