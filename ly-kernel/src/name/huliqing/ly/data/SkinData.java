/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.ly.data.define.CostObject;
import name.huliqing.ly.data.define.MatObject;
import name.huliqing.ly.manager.ResourceManager;

/**
 * 皮肤，装甲，武器等.
 * @author huliqing
 */
@Serializable
public class SkinData extends ObjectData implements MatObject, CostObject {
    
    // 是否已在使用中
    private boolean used;
    
    // 标记着这件装备是否为基本皮肤
    private boolean baseSkin;
    
    // 装备应用到目标身上时对目标属性的影响
    private List<AttributeApply> applyAttributes;
    
    // 标记着当前属性是否已经应用到角色身上,在角色穿上当前装备之后应该把这个参数设置为true， 
    // 在脱下当前装备后应该设置为false.
    private boolean attributeApplied;
    
    // 属性限制
    private List<AttributeMatch> matchAttributes;
    
    private int mat;
    
    public SkinData() {}
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(used, "used", false);
        if (applyAttributes != null) {
            oc.writeSavableArrayList(new ArrayList<AttributeApply>(applyAttributes), "applyAttributes", null);
        }
        oc.write(baseSkin, "baseSkin", false);
        oc.write(attributeApplied, "attributeApplied", false);
        if (matchAttributes != null) {
            oc.writeSavableArrayList(new ArrayList<AttributeMatch>(matchAttributes), "matchAttributes", null);
        }
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        used = ic.readBoolean("used", false);
        applyAttributes = ic.readSavableArrayList("applyAttributes", null);
        baseSkin = ic.readBoolean("baseSkin", false);
        attributeApplied = ic.readBoolean("attributeApplied", false);
        matchAttributes = ic.readSavableArrayList("matchAttributes", null);
    }

    /**
     * 判断当前装备是否正在使用中
     * @return 
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * 标记这件装备正在使用中
     * @param used 
     */
    public void setUsed(boolean used) {
        this.used = used;
    }
    
    public List<AttributeApply> getApplyAttributes() {
        return applyAttributes;
    }

    public void setApplyAttributes(ArrayList<AttributeApply> applyAttributes) {
        this.applyAttributes = applyAttributes;
    }
    
    /**
     * 获取模型文件路径如："Models/xyz.j3o";
     * @return 
     */
    public String getFile() {
        return getAsString("file");
    }
    
    @Override
    public int getMat() {
        return mat;
    }
    
    @Override
    public void setMat(int mat) {
        this.mat = mat;
    }

    @Override
    public float getCost() {
        return getAsFloat("cost", 0);
    }

    /**
     * 获取描述说明
     * @return 
     * @deprecated 以后不要再使用.
     */
    @Override
    public String getDes() {
        List<AttributeApply> aas = getApplyAttributes();
        if (aas != null) {
            StringBuilder sb = new StringBuilder();
            for (AttributeApply aa : aas) {
                sb.append(ResourceManager.getObjectName(aa.getAttribute()))
                        .append(":")
                        .append(aa.getAmount())
                        .append("  ");
            }
            return sb.toString();
        }
        return ResourceManager.get(ResConstants.COMMON_UNKNOW);
    }

    /**
     * 判断这件skin是否为基本皮肤
     * @return 
     */
    public boolean isBaseSkin() {
        return baseSkin;
    }

    /**
     * 设置这件skin是否为基本皮肤，对于基本皮肤来说，不可以删除和出售。
     * @param baseSkin 
     */
    public void setBaseSkin(boolean baseSkin) {
        this.baseSkin = baseSkin;
    }

    /**
     * 获取装备的属性是否已经应用到了角色身上，如果该参数返回true,则说明属性已经应用到角色身上，
     * 在这种情况下，当角色再穿上这件装备的时候就不再需要处理applyAttributes的问题,
     * 以避免重复给角色添加属性值。
     * @return 
     */
    public boolean isAttributeApplied() {
        return attributeApplied;
    }

    /**
     * 标记着当前属性是否已经应用到角色身上,在角色穿上装备之后应该把这个参数设置为true， 
     * 在脱下装备后应该设置为false.
     * @param attributeApplied 
     */
    public void setAttributeApplied(boolean attributeApplied) {
        this.attributeApplied = attributeApplied;
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
}
