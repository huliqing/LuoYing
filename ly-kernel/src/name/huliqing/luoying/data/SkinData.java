/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
import name.huliqing.luoying.data.define.MatObject;
import name.huliqing.luoying.data.define.TradeInfo;
import name.huliqing.luoying.data.define.TradeObject;
import name.huliqing.luoying.xml.SimpleCloner;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 皮肤，装甲，武器等.
 * @author huliqing
 */
@Serializable
public class SkinData extends ObjectData implements TradeObject, MatObject {
    
    // 物品的价值信息
    private List<TradeInfo> tradeInfos;
    
    // 装备应用到目标身上时对目标属性的影响
    private List<AttributeApply> applyAttributes;
    
    @Override
    public int getTotal() {
        return getAsInteger("total", 0);
    }

    @Override
    public void setTotal(int total) {
        setAttribute("total", total);
    }

    /**
     * 获取图标，如果没有设置则返回null.
     * @return 
     */
    public String getIcon() {
        return getAsString("icon");
    }
    
    /**
     * 设置图标，如果没有设置则返回null.
     * @param icon 
     */
    public void setIcon(String icon) {
        setAttribute("icon", icon);
    }
    
    /**
     * 获取武器类型，如果该方法返回不为null,则说明这是一把武器。
     * @return 
     * @see DefineFactory#getWeaponTypeDefine() 
     */
    public String getWeaponType() {
        return getAsString("weaponType");
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
        return getAsInteger("mat");
    }
    
    @Override
    public void setMat(int mat) {
        setAttribute("mat", mat);
    }

    /**
     * 判断当前装备是否正在使用中
     * @return 
     */
    public boolean isUsed() {
        return getAsBoolean("used", false);
    }

    /**
     * 标记这件装备正在使用中
     * @param used 
     */
    public void setUsed(boolean used) {
        setAttribute("used", used);
    }
    
    /**
     * 判断这件skin是否为基本皮肤
     * @return 
     */
    public boolean isBaseSkin() {
        return getAsBoolean("baseSkin", false);
    }

    /**
     * 设置这件skin是否为基本皮肤，对于基本皮肤来说，不可以删除和出售。
     * @param baseSkin 
     */
    public void setBaseSkin(boolean baseSkin) {
        setAttribute("baseSkin", baseSkin);
    }
    
    /**
     * 判断是否为武器
     * @return 
     */
    public boolean isWeapon() {
        return getWeaponType() != null;
    }
    
    /**
     * 获取装备的属性是否已经应用到了角色身上，如果该参数返回true,则说明属性已经应用到角色身上，
     * 在这种情况下，当角色再穿上这件装备的时候就不再需要处理applyAttributes的问题,
     * 以避免重复给角色添加属性值。
     * @return 
     */
    public boolean isAttributeApplied() {
        return getAsBoolean("attributeApplied", false);
    }

    /**
     * 标记着当前属性是否已经应用到角色身上,在角色穿上装备之后应该把这个参数设置为true， 
     * 在脱下装备后应该设置为false.
     * @param attributeApplied 
     */
    public void setAttributeApplied(boolean attributeApplied) {
        setAttribute("attributeApplied", attributeApplied);
    }
    
    @Override
    public List<TradeInfo> getTradeInfos() {
        return tradeInfos;
    }

    @Override
    public void setTradeInfos(List<TradeInfo> tradeInfos) {
        this.tradeInfos = tradeInfos;
    }
    
    public List<AttributeApply> getApplyAttributes() {
        return applyAttributes;
    }
    
    public void setApplyAttributes(ArrayList<AttributeApply> applyAttributes) {
        this.applyAttributes = applyAttributes;
    }

    @Override
    public SkinData clone() {
        SkinData clone = (SkinData) super.clone(); 
        SimpleCloner cloner = new SimpleCloner();
        clone.tradeInfos = cloner.clone(tradeInfos);
        clone.applyAttributes  = cloner.clone(applyAttributes);
        return clone;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        if (tradeInfos != null) {
            oc.writeSavableArrayList(new ArrayList<TradeInfo>(tradeInfos), "tradeInfos", null);
        }
        if (applyAttributes != null) {
            oc.writeSavableArrayList(new ArrayList<AttributeApply>(applyAttributes), "applyAttributes", null);
        }
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        tradeInfos = ic.readSavableArrayList("tradeInfos", null);
        applyAttributes = ic.readSavableArrayList("applyAttributes", null);
    }
}
