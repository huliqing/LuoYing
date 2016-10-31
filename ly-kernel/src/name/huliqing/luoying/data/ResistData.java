/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data;

import name.huliqing.luoying.xml.ObjectData;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;

/**
 *
 * @author huliqing
 */
@Serializable
public class ResistData extends ObjectData {
    
    /**
     * 抵抗率[0.0~1.0]
     */
    private float factor;
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(factor, "factor", 0);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        factor = ic.readFloat("factor", 0);
    }
    
    public ResistData() {}

    /**
     * 获取抵抗率，[0.0~1.0]
     * @return 
     */
    public float getFactor() {
        return factor;
    }

    /**
     * 设置抵抗率，该值会被限制在[0.0~1.0]之间
     * @param factor 
     */
    public void setFactor(float factor) {
        this.factor = FastMath.clamp(factor, .0f, 1.0f);
    }
    
}
