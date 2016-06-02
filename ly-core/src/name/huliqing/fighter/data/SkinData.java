/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.constants.ResConstants;
import name.huliqing.fighter.constants.SkinConstants;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.utils.ConvertUtils;

/**
 * 皮肤，装甲，武器等.
 * @author huliqing
 */
@Serializable
public class SkinData extends PkgItemData {
    
    // 注：一件skin可属于多个type,如上下连身的套装，如法袍可属于 "7,8".
    // 同时一件skin也可与多个其它skin进行排斥。这里的type和conflictType使用二
    // 进制位来表示各个类型，例如一件上下连身的套装（类型属于7,8）在二进制表示为
    // "11000000"
    private int type;
    // 定义与其它skin的排斥,当一件skin穿上身时，角色身上受排斥的skin将会脱下来。
    private int conflictType;
    // 是否正在使用
    private boolean using; 
    
    // 装备应用到目标身上时对目标属性的影响
    private ArrayList<AttributeApply> applyAttributes;
    
    // 武器类型，如果该值大于0，则说明为武器类型
    // 否则为普通装备
    private int weaponType;
    // 标记当前武器所有可支持的槽位
    private List<String> slots;
    // 标记当前武器所在的槽位
    private String slot;
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(type, "type", 0);
        oc.write(conflictType, "conflictType", 0);
        oc.write(using, "using", false);
        oc.writeSavableArrayList(applyAttributes, "applyAttributes", null);
        oc.write(weaponType, "weaponType", 0);
        if (slots != null) 
            oc.write(slots.toArray(new String[]{}), "slots", null);
        oc.write(slot, "slot", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        type = ic.readInt("type", 0);
        conflictType = ic.readInt("conflictType", 0);
        using = ic.readBoolean("using", false);
        applyAttributes = ic.readSavableArrayList("applyAttributes", null);
        weaponType = ic.readInt("weaponType", 0);
        slots = ConvertUtils.toList(ic.readStringArray("slots", null));
        slot = ic.readString("slot", null);
    }
    
    public SkinData() {}
    
    public SkinData(String id) {
        super(id);
    }

    /**
     * 获取skin的类型，注：这里返回的整数使用的是二进制位来表示skin的类型，
     * 每一个位表示一个skin类型。
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

    public boolean isUsing() {
        return using;
    }

    public void setUsing(boolean using) {
        this.using = using;
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
     * @param weaponSlots 
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
     * @param weaponSlot 
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

    public ArrayList<AttributeApply> getApplyAttributes() {
        return applyAttributes;
    }

    public void setApplyAttributes(ArrayList<AttributeApply> applyAttributes) {
        this.applyAttributes = applyAttributes;
    }
    
    /**
     * 获取描述说明
     * @return 
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
}
