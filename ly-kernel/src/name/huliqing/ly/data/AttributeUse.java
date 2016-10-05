/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;

/**
 * 用于SkillData，定义技能消耗的属性量
 * @author huliqing
 */
@Serializable
public class AttributeUse implements Savable {
    
    /** 使用的属性ID */
    private String attribute;

    /** 使用的属性量 */
    private float amount;
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(attribute, "attribute", null);
        oc.write(amount, "amount", 0);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        attribute = ic.readString("attribute", null);
        amount = ic.readFloat("amount", 0);
    }
    
    public AttributeUse() {}

    public AttributeUse(String attribute, float amount) {
        this.attribute = attribute;
        this.amount = amount;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
    
}
