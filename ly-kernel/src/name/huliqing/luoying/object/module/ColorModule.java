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
import com.jme3.math.Vector4f;
import com.jme3.shader.VarType;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.attribute.ValueChangeListener;
import name.huliqing.luoying.object.attribute.Vector4fAttribute;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 颜色模块, 用于支持动态改变Entity的颜色, 需要给Entity添加一个Vector4Attribute的属性，并绑定到ColorModule上，
 * 然后通过运行时动态改变属性的值来改变Entity颜色。
 * @author huliqing
 */
public class ColorModule extends AbstractModule implements ValueChangeListener{
//    private static final Logger LOG = Logger.getLogger(ColorModule.class.getName());

    // 绑定一个Vector4Attribute类型的属性，这个属性用于控制角色的颜色
    private String bindColorAttribute;
    
    // ---- inner
    private Vector4fAttribute colorAttribute;
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
    public void updateDatas() {
        // ignore
    }
    
    @Override
    public void initialize(Entity entity) {
        super.initialize(entity);
        colorAttribute = getAttribute(bindColorAttribute, Vector4fAttribute.class);
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
        }
        super.cleanup(); 
    }

    @Override
    public void onValueChanged(Attribute attribute) {
        if (attribute == colorAttribute) {
            changeColor(colorAttribute.getValue());
        }
    }
    
    private void changeColor(Vector4f vec4) {
        if (!colorInitialized) {
            entity.getSpatial().addMatParamOverride(colorOverride);
            entity.getSpatial().addMatParamOverride(useMaterialColorOverride);
            entity.getSpatial().addMatParamOverride(ambientOverride);
            entity.getSpatial().addMatParamOverride(diffuseOverride);
            colorInitialized = true;
        }
        ((ColorRGBA)colorOverride.getValue()).set(vec4.x, vec4.y, vec4.z, vec4.w);
        ((ColorRGBA)ambientOverride.getValue()).set(vec4.x, vec4.y, vec4.z, vec4.w);
        ((ColorRGBA)diffuseOverride.getValue()).set(vec4.x, vec4.y, vec4.z, vec4.w);
    }
}
