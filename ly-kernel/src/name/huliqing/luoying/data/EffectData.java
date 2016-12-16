/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author huliqing
 */
@Serializable
public class EffectData extends ModelEntityData {
    
    private List<DelayAnimData> delayAnimDatas;
    
    public List<DelayAnimData> getDelayAnimDatas() {
        return delayAnimDatas;
    }

    public void setDelayAnimDatas(List<DelayAnimData> anims) {
        this.delayAnimDatas = anims;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        if (delayAnimDatas != null) {
            oc.writeSavableArrayList(new ArrayList<DelayAnimData>(delayAnimDatas), "anims", null);
        }
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        delayAnimDatas = ic.readSavableArrayList("anims", null);
    }
}
