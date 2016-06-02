/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.actoranim;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.data.ActorAnimData;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.anim.AbstractAnim;

/**
 * ActorAnim是专门用于角色动画的控制器.使用update方法来手动控制更新，或者
 * 直接添加到control列表中进行自动更新，二选一，不要两个都进行。该包依赖于
 * Animation包。
 * e.g.:
 * 1.ActorAnim anim = new ActorAnimXXX(data);
 * 2.anim.setActor(actor);
 * 3.anim.update(tpf) or actor.getModel().addControl(anim);
 * @author huliqing
 */
public abstract class ActorAnim extends AbstractAnim<Actor> implements Control {
    private final static Logger logger = Logger.getLogger(ActorAnim.class.getName());
    // 这个spatial最多只负责执行control,不开放给子类。
    // 当ActorAnim被添加到control列表时，这个refSpatial即为依附的对象。一般为
    // actor所在的model，在动画结束后可以将当前actorAnim从该model中释放出来,避免资源浪费
    private Spatial refSpatial;
    
    public ActorAnim(ActorAnimData data) {
        super(data);
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        logger.log(Level.WARNING, "Unsupported yet");
        return null;
    }

    /**
     * 将当前control添加到目标spatial时，这个方法会被调用，但是这个spatial
     * 只是用来负责执行当前control而已。
     * @param spatial 
     */
    @Override
    public void setSpatial(Spatial spatial) {
        refSpatial = spatial;
    }
    
    /**
     * 设置角色
     * @param actor 
     */
    public void setActor(Actor actor) {
        this.target = actor;
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {}

    @Override
    public void write(JmeExporter ex) throws IOException {
        logger.log(Level.WARNING, "Unsupported yet!");
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        logger.log(Level.WARNING, "Unsupported yet!");
    }

    @Override
    public void cleanup() {
        if (refSpatial != null) {
            refSpatial.removeControl(this);
            refSpatial = null;
        }
        super.cleanup();
    }

}
