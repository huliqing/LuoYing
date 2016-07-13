/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.anim;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.data.AnimData;

/**
 * SpatialAnim是为了支持Spatial的Control方式而增加了,该方式可以将动
 * 画控制器直接添加到Sptial的Control列表中直接执行。只需要调用start即可。
 * 不需要手动调用update方式更新。
 * @author huliqing
 * @param <T>
 */
public abstract class SpatialAnim<T extends AnimData> extends AbstractAnim<T, Spatial> implements Control {
    private final static Logger LOG = Logger.getLogger(SpatialAnim.class.getName());
    
    @Override
    public void setSpatial(Spatial spatial) {
        this.setTarget(spatial);
    }

    @Override
    public Spatial getTarget() {
        return target;
    }
    
    @Override
    public void render(RenderManager rm, ViewPort vp) {}
    
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        LOG.log(Level.WARNING, "Unsupported yet");
        return null;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        LOG.log(Level.WARNING, "Unsupported yet!");
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        LOG.log(Level.WARNING, "Unsupported yet!");
    }
    
}
