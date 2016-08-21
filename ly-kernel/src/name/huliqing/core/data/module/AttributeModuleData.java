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
import name.huliqing.core.data.AttributeData;

/**
 *
 * @author huliqing
 */
@Serializable
public class AttributeModuleData extends ModuleData {
    
//    private List<AttributeData> attributes;
    
//    public List<AttributeData> getAttributes() {
//        return attributes;
//    }
//    
//    public void setAttributes(List<AttributeData> attributes) {
//        this.attributes = attributes;
//    }
//    
//    @Override
//    public void write(JmeExporter ex) throws IOException {
//        super.write(ex);
//        if (attributes != null) {
//            OutputCapsule oc = ex.getCapsule(this);
//            oc.writeSavableArrayList(new ArrayList<AttributeData>(attributes), "attributes", null);
//        }
//    }
//    
//    @Override
//    public void read(JmeImporter im) throws IOException {
//        super.read(im);
//        InputCapsule ic = im.getCapsule(this);
//        attributes = ic.readSavableArrayList("attributes", null);
//    }
}
