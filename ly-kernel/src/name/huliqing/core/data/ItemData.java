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
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.define.CostObject;

/**
 * 基本的原始物体数据
 * @author huliqing
 */
@Serializable
public class ItemData extends ObjectData implements CostObject{
    
    // 属性限制
    private List<AttributeMatch> matchAttributes;
    
    // 判断物品是否是可删除的
    private boolean deletable = true;
    
    @Override
    public float getCost() {
        return getAsFloat("cost", 0);
    }
    
    /**
     * 获取属性限制
     * @return 
     */
    public List<AttributeMatch> getMatchAttributes() {
        return matchAttributes;
    }

    /**
     * 设置使用物品时的属性限制。
     * @param matchAttributes 
     */
    public void setMatchAttributes(List<AttributeMatch> matchAttributes) {
        this.matchAttributes = matchAttributes;
    }
    
    /**
     * 判断物品是否是可删除的 
     * @return 
     */
    public boolean isDeletable() {
        return deletable;
    }

    /**
     * 设置物品是否是可删除的
     * @param deletable 
     */
    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(deletable, "deletable", true);
        if (matchAttributes != null) {
            oc.writeSavableArrayList(new ArrayList<AttributeMatch>(matchAttributes), "matchAttributes", null);
        }
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        deletable = ic.readBoolean("deletable", true);
        matchAttributes = ic.readSavableArrayList("matchAttributes", null);
    }
}
