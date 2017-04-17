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
package name.huliqing.luoying.object.module;

import com.jme3.material.MatParamOverride;
import com.jme3.math.ColorRGBA;
import com.jme3.shader.VarType;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.attribute.ColorAttribute;
import name.huliqing.luoying.object.attribute.ValueChangeListener;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 颜色模块, 用于支持动态改变实体的颜色, 给实体添加这个模块，然后再给实体添加一个ColorAttribute的属性，
 * 并将这个属性绑定到ColorModule上，就可以在运行时通过改变ColorAttribute的属性值来改变实体的颜色。
 * @author huliqing
 */
public class ColorModule extends AbstractModule implements ValueChangeListener{
//    private static final Logger LOG = Logger.getLogger(ColorModule.class.getName());

    // 绑定一个Vector4Attribute类型的属性，这个属性用于控制角色的颜色
    private String bindColorAttribute;
    
    // ---- inner
    private ColorAttribute colorAttribute;
    // 用于Unshaded.jm3d
    private final MatParamOverride colorOverride = new MatParamOverride(VarType.Vector4, "Color", ColorRGBA.White.clone());
    // 用于Lighting.j3md
    private final MatParamOverride useMaterialColorOverride = new MatParamOverride(VarType.Boolean, "UseMaterialColors", true);
    private final MatParamOverride ambientOverride = new MatParamOverride(VarType.Vector4, "Ambient", ColorRGBA.White.clone());
    private final MatParamOverride diffuseOverride = new MatParamOverride(VarType.Vector4, "Diffuse", ColorRGBA.White.clone());
    
    private boolean colorInitialized;
    
    @Override
    public void setData(ModuleData data) {
        super.setData(data); 
        bindColorAttribute = data.getAsString("bindColorAttribute");
    }
    
    @Override
    public void initialize() {
        super.initialize();
        colorAttribute = getAttribute(bindColorAttribute, ColorAttribute.class);
        if (colorAttribute != null) {
            colorAttribute.addListener(this);
            changeColor(colorAttribute.getValue());
        }
    }
    @Override
    public void cleanup() {
        if (colorAttribute != null) {
            colorAttribute.removeListener(this);
        }
        if (colorInitialized) {
            entity.getSpatial().removeMatParamOverride(colorOverride);
            entity.getSpatial().removeMatParamOverride(useMaterialColorOverride);
            entity.getSpatial().removeMatParamOverride(ambientOverride);
            entity.getSpatial().removeMatParamOverride(diffuseOverride);
            colorInitialized = false;
        }
        super.cleanup(); 
    }

    @Override
    public void onValueChanged(Attribute attribute) {
        if (attribute == colorAttribute) {
            changeColor(colorAttribute.getValue());
        }
    }
    
    private void changeColor(ColorRGBA color) {
        if (!isEnabled())
            return;
        
        if (!colorInitialized) {
            entity.getSpatial().addMatParamOverride(colorOverride);
            entity.getSpatial().addMatParamOverride(useMaterialColorOverride);
            entity.getSpatial().addMatParamOverride(ambientOverride);
            entity.getSpatial().addMatParamOverride(diffuseOverride);
            colorInitialized = true;
        }
        ((ColorRGBA)colorOverride.getValue()).set(color.r, color.g, color.b, color.a);
        ((ColorRGBA)ambientOverride.getValue()).set(color.r, color.g, color.b, color.a);
        ((ColorRGBA)diffuseOverride.getValue()).set(color.r, color.g, color.b, color.a);
    }

    @Override
    public boolean handleDataAdd(ObjectData data, int amount) {
        return false;
    }

    @Override
    public boolean handleDataRemove(ObjectData data, int amount) {
        return false;
    }

    @Override
    public boolean handleDataUse(ObjectData data) {
        return false;
    }
}
