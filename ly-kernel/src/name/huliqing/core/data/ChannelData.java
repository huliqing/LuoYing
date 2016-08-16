/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;

/**
 *
 * @author huliqing
 */
@Serializable
public class ChannelData extends ObjectData {
    
    private String[] fromRootBones;
    private String[] toRootBones;
    private String[] bones;

    public String[] getFromRootBones() {
        return fromRootBones;
    }

    public void setFromRootBones(String[] fromRootBones) {
        this.fromRootBones = fromRootBones;
    }

    public String[] getToRootBones() {
        return toRootBones;
    }

    public void setToRootBones(String[] toRootBones) {
        this.toRootBones = toRootBones;
    }

    public String[] getBones() {
        return bones;
    }

    public void setBones(String[] bones) {
        this.bones = bones;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(fromRootBones, "fromRootBones", null);
        oc.write(toRootBones, "toRootBones", null);
        oc.write(bones, "bones", null);
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        fromRootBones = ic.readStringArray("fromRootBones", null);
        toRootBones = ic.readStringArray("toRootBones", null);
        bones = ic.readStringArray("bones", null);
    }
}
