/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 * AbstractControl 适配器, 只保留update方法，其它任何方法都不要覆盖。
 * @author huliqing
 */
public class ControlAdapter implements Control {

    @Override
    public final Control cloneForSpatial(Spatial spatial) {
        // ignore
        return null;
    }

    @Override
    public final void setSpatial(Spatial spatial) {
        // ignore
    }

    @Override
    public void update(float tpf) {
        // for children override
    }

    @Override
    public final void render(RenderManager rm, ViewPort vp) {
        // ignore
    }

    @Override
    public final void write(JmeExporter ex) throws IOException {
        // ignore
    }

    @Override
    public final void read(JmeImporter im) throws IOException {
        // ignore
    }

  
    
}
