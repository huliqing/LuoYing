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
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.StateData;

/**
 *
 * @author huliqing
 */
@Serializable
public class StateModuleData extends ModuleData {
    
    private List<StateData> stateDatas;

    public List<StateData> getStateDatas() {
        return stateDatas;
    }

    public void setStateDatas(List<StateData> stateDatas) {
        this.stateDatas = stateDatas;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        if (stateDatas != null) {
            OutputCapsule oc = ex.getCapsule(this);
            oc.writeSavableArrayList(new ArrayList<StateData>(stateDatas), "stateDatas", null);
        }
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        stateDatas = ic.readSavableArrayList("stateDatas", null);
    }
    
}
