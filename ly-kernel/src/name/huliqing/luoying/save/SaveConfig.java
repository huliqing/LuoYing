/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.save;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import java.io.IOException;
import name.huliqing.luoying.data.ConfigData;

/**
 *
 * @author huliqing
 */
public class SaveConfig implements Savable {
    
    // 全局配置
    private ConfigData config;
    
    public ConfigData getConfig() {
        return config;
    }
    
    public void setConfig(ConfigData config) {
        this.config = config;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(config, "config", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        config = (ConfigData) ic.readSavable("config", null);
    }
}
