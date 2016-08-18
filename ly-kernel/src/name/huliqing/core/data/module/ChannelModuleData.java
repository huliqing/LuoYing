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
import name.huliqing.core.data.ChannelData;

/**
 *
 * @author huliqing
 */
@Serializable
public class ChannelModuleData extends ModuleData {
    
    private List<ChannelData> channels;

    public List<ChannelData> getChannels() {
        return channels;
    }

    public void setChannels(List<ChannelData> channels) {
        this.channels = channels;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        if (channels != null) {
            OutputCapsule oc = ex.getCapsule(this);
            oc.writeSavableArrayList(new ArrayList<ChannelData>(channels), "channels", null);
        }
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        channels = ic.readSavableArrayList("channels", null);
    }
}
