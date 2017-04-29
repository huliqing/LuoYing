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

import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shader.VarType;
import java.util.List;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.constants.AssetConstants;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.xml.ObjectData;

/**
 * SwayModule用于让模形实体的网格有摇动的功能, 一般可用于花草树木等场景实体，
 * 可以让这些物体如树叶进行摆动。
 * 这个模块会替换掉物体的材质。
 * @author huliqing
 */
public class SwayModule extends AbstractModule {
    
    // 摇摆开关 
    private boolean sway = true;
    // 摇动的频率
    private float swayFrequency = 1.5f;
    // 摇动扭曲
    private float swayVariation = 1.0f;
    // 最大摇动距离
    private float swayDistance = 1.0f;
    // 风的风向(0,1)为正面（Z向）
    private Vector2f swayWind= new Vector2f(0,1);
    // 指定要摇动哪些网格(名称)，如果没有指定则全部摇动
    private List<String> swayGeometries;
    
    @Override
    public void setData(ModuleData data) {
        super.setData(data);
        sway = data.getAsBoolean("sway", sway);
        swayFrequency = data.getAsFloat("swayFrequency", swayFrequency);
        swayVariation = data.getAsFloat("swayVariation", swayVariation);
        swayDistance = data.getAsFloat("swayDistance", swayDistance);
        swayWind = data.getAsVector2f("swayWind", swayWind);
        swayGeometries = data.getAsStringList("swayGeometries");
    }

    @Override
    public void initialize() {
        super.initialize();
        
        if (sway) {
            converterToGrass(entity.getSpatial(), swayGeometries);
        }
    }
    
    private void converterToGrass(Spatial model, List<String> geometries) {
        if (model instanceof Geometry) {
            if (geometries == null || geometries.isEmpty() || geometries.contains(model.getName())) {
                model.setMaterial(
                        convertToGrassMaterial(((Geometry) model).getMaterial()
                                , sway,  swayFrequency, swayVariation, swayDistance, swayWind
                ));
            }
        } else if (model instanceof Node) {
            for (Spatial child : ((Node) model).getChildren()) {
                converterToGrass(child, geometries);
            }
        }
    }

    private Material convertToGrassMaterial(Material originMat
            , boolean swaying, float swayFrequency, float swayVariation, float swayDistance, Vector2f wind) {
        Material grassMat = new Material(LuoYing.getAssetManager(), AssetConstants.MATERIAL_GRASS);
        grassMat.setName("Grass Material");
        transferMatParam(originMat, grassMat, "AlphaDiscardThreshold", VarType.Float);
        transferMatParam(originMat, grassMat, "UseMaterialColors", VarType.Boolean);
        transferMatParam(originMat, grassMat, "UseVertexColor", VarType.Boolean);
        transferMatParam(originMat, grassMat, "Ambient", VarType.Vector4);
        transferMatParam(originMat, grassMat, "Diffuse", VarType.Vector4);
        transferMatParam(originMat, grassMat, "Specular", VarType.Vector4);
        transferMatParam(originMat, grassMat, "Shininess", VarType.Float);
        transferMatParam(originMat, grassMat, "DiffuseMap", VarType.Texture2D);
        transferMatParam(originMat, grassMat, "NormalMap", VarType.Texture2D);
        transferMatParam(originMat, grassMat, "SpecularMap", VarType.Texture2D);
        
        grassMat.setTransparent(true);
        grassMat.getAdditionalRenderState().setDepthWrite(true);
        grassMat.getAdditionalRenderState().setDepthTest(true);
        grassMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        
        grassMat.setBoolean("Swaying", swaying);
        grassMat.setVector3("SwayData", new Vector3f(swayFrequency, swayVariation, swayDistance));
        grassMat.setVector2("Wind", wind);
        
        return grassMat;
    }
    
    private void transferMatParam(Material from, Material to, String matName, VarType varType) {
        MatParam matParam = from.getParam(matName);
        if (matParam != null && matParam.getVarType() == varType) {
            to.setParam(matName, varType, matParam.getValue());
        }
    }
    
    @Override
    public boolean handleDataAdd(ObjectData hData, int amount) {return false;}
    @Override
    public boolean handleDataRemove(ObjectData hData, int amount) {return false;}
    @Override
    public boolean handleDataUse(ObjectData hData) {return false;}
    
}
