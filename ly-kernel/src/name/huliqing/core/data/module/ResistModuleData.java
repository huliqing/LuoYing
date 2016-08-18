/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data.module;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import name.huliqing.core.data.ResistData;

/**
 *
 * @author huliqing
 */
@Serializable
public class ResistModuleData extends ModuleData {
    
    // 状态抵抗设置
    private ResistData resist;

    public ResistData getResist() {
        return resist;
    }

    public void setResist(ResistData resist) {
        this.resist = resist;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        if (resist != null) {
            OutputCapsule oc = ex.getCapsule(this);
            oc.write(resist, "resist", null);
        }
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im); 
        InputCapsule ic = im.getCapsule(this);
        resist = (ResistData) ic.readSavable("resist", null);
    }
}
