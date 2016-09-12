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
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.constants.SkinConstants;
import name.huliqing.core.data.define.CostObject;
import name.huliqing.core.data.define.HandlerObject;
import name.huliqing.core.data.define.MatObject;
import name.huliqing.core.enums.Mat;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.utils.ConvertUtils;

/**
 * 皮肤，装甲，武器等.
 * @author huliqing
 */
@Serializable
public class SkinData extends ObjectData implements MatObject, CostObject, HandlerObject {
    
    //注：一件skin可属于多个type,如上下连身的套装，如法袍可属于 "7,8".
    //同时一件skin也可与多个其它skin进行排斥。这里的type和conflictType使用二
    //进制位来表示各个类型，例如一件上下连身的套装（类型属于7,8）在二进制表示为
    //"11000000"
    private int type;
    // 定义与其它skin的排斥,当一件skin穿上身时，角色身上受排斥的skin将会脱下来。
    private int conflictType;
    // 是否已在使用中
    private boolean used;
    
    // 这个值如果为0则说明是普通装备，如果该值大于0则说明为某种类型的武器
    private int weaponType;
    // 标记当前武器所有可支持的槽位
    private List<String> slots;
    // 标记当前武器所在的槽位
    private String slot;
    // 标记着这件装备是否为基本皮肤
    private boolean baseSkin;
    // 装备应用到目标身上时对目标属性的影响
    private List<AttributeApply> applyAttributes;
    
    // 标记着当前属性是否已经应用到角色身上,在角色穿上当前装备之后应该把这个参数设置为true， 
    // 在脱下当前装备后应该设置为false.
    private boolean attributeApplied;
    
    // 属性限制
    private List<AttributeMatch> matchAttributes;
    
    public SkinData() {
        this.total = 1;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(type, "type", 0);
        oc.write(conflictType, "conflictType", 0);
        oc.write(used, "used", false);
        if (applyAttributes != null) {
            oc.writeSavableArrayList(new ArrayList<AttributeApply>(applyAttributes), "applyAttributes", null);
        }
        oc.write(weaponType, "weaponType", 0);
        if (slots != null) 
            oc.write(slots.toArray(new String[]{}), "slots", null);
        oc.write(slot, "slot", null);
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
        type = ic.readInt("type", 0);
        conflictType = ic.readInt("conflictType", 0);
        used = ic.readBoolean("used", false);
        applyAttributes = ic.readSavableArrayList("applyAttributes", null);
        weaponType = ic.readInt("weaponType", 0);
        slots = ConvertUtils.toList(ic.readStringArray("slots", null));
        slot = ic.readString("slot", null);
        baseSkin = ic.readBoolean("baseSkin", false);
        attributeApplied = ic.readBoolean("attributeApplied", false);
        matchAttributes = ic.readSavableArrayList("matchAttributes", null);
    }
    
    /**
     * 获取skin的类型，注：这里返回的整数使用的是二进制位来表示skin的类型，
     * 每一个位表示一个skin类型。<br>
     *  注：一件skin可属于多个type,如上下连身的套装，如法袍可属于 "7,8".
     * 同时一件skin也可与多个其它skin进行排斥。这里的type和conflictType使用二进制位来表示各个类型，
     * 例如一件上下连身的套装（类型属于7,8）在二进制表示为"11000000"
     * @return 
     */
    public int getType() {
        return type;
    }

    /**
     * 设置skin的类型
     * @param type 二进制位表示，每个位表示一个skin类型。
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 获取skin的排斥类型，二进制表示，参考type
     * @return 
     */
    public int getConflictType() {
        return conflictType;
    }

    /**
     * 设置skin的排斥类型
     * @param conflictType 二进制位表示，每个位表示一个skin类型。
     */
    public void setConflictType(int conflictType) {
        this.conflictType = conflictType;
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

    /**
     * 获取武器类型值，如果该值为0，则表示该装备不是武器。
     * 武器类型参考：SkinConstants.WEAPON_SWORD,...
     * @return 
     */
    public int getWeaponType() {
        return weaponType;
    }

    /**
     * 设置一个武器类型，如果设置为0，则表示非武器
     * 武器类型参考：SkinConstants.WEAPON_SWORD,...
     * @param weaponType 
     */
    public void setWeaponType(int weaponType) {
        this.weaponType = weaponType;
    }

    /**
     * 获取武器所有可支持的槽位
     * @return 
     */
    public List<String> getSlots() {
        return slots;
    }

    /**
     * 设置武器所有可支持的槽位 
     * @param slots
     */
    public void setSlots(List<String> slots) {
        this.slots = slots;
    }

    /**
     * 获得武器所在的槽位
     * @return 
     */
    public String getSlot() {
        return slot;
    }

    /**
     * 设置武器所在的槽位 
     * @param slot
     */
    public void setSlot(String slot) {
        this.slot = slot;
    }
    
    /**
     * 判断是不是武器
     * @return 
     */
    public boolean isWeapon() {
        return getWeaponType() > 0;
    }
    
    /**
     * 判断是不是一把左手武器
     * @return 
     */
    public boolean isLeftHandWeapon() {
        return (type & (1 << SkinConstants.TYPE_WEAPON_LEFT)) != 0;
    }
    
    /**
     * 判断是不是一把右手武器
     * @return 
     */
    public boolean isRightHandWeapon() {
        return (type & (1 << SkinConstants.TYPE_WEAPON_RIGHT)) != 0;
    }

    public List<AttributeApply> getApplyAttributes() {
        return applyAttributes;
    }

    public void setApplyAttributes(ArrayList<AttributeApply> applyAttributes) {
        this.applyAttributes = applyAttributes;
    }
    
    /**
     * 获取模型文件路径如："Models/xxx.j3o";
     * @return 
     */
    public String getFile() {
        return getAsString("file");
    }
    
    /**
     * 获取物品的材质（mat)，如果没有设置则返回Mat.none.该材质信息目前主要
     * 用于计算物体碰撞声音。
     * @return 
     */
    @Override
    public Mat getMat() {
        int matInt = getAsInteger("mat", Mat.none.getValue());
        return Mat.identify(matInt);
    }

    @Override
    public float getCost() {
        return getAsFloat("cost", 0);
    }

    @Override
    public String getHandler() {
        return getAsString("handler");
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
