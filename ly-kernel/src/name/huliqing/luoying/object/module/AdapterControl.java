/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.module;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 * 简单的Control适配器,这个类不会做任何工作，只为调用update(tpf)和render(rm, vp)方法。
 * 主要由其它module在需要的情况下去继承这个类，并覆盖update或render方法来使用module支持"更新"逻辑.
 * @author huliqing
 */
public class AdapterControl implements Control {

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSpatial(Spatial spatial) {
    }

    @Override
    public void update(float tpf) {
        // ignore
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
        // ignore
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
    }

    @Override
    public void read(JmeImporter im) throws IOException {
    }
    
}
