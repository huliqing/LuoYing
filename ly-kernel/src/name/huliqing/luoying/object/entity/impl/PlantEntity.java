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
package name.huliqing.luoying.object.entity.impl;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shader.VarType;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.constants.AssetConstants;
import name.huliqing.luoying.data.ModelEntityData;
import name.huliqing.luoying.object.entity.ModelEntity;
import name.huliqing.luoying.object.entity.TerrainEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.scene.SceneListener;
import name.huliqing.luoying.object.scene.SceneListenerAdapter;

/**
 * 植被环境物体，如：花草等物体
 * @author huliqing
 */
public abstract class PlantEntity extends ModelEntity {
    
    private SceneListener sceneListener;
    
    // 树叶、草叶摇摆
    private boolean sway;
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
    public void setData(ModelEntityData data) {
        super.setData(data);
        sway = data.getAsBoolean("sway", sway);
        swayFrequency = data.getAsFloat("swayFrequency", swayFrequency);
        swayVariation = data.getAsFloat("swayVariation", swayVariation);
        swayDistance = data.getAsFloat("swayDistance", swayDistance);
        swayWind = data.getAsVector2f("swayWind", swayWind);
        swayGeometries = data.getAsStringList("swayGeometries");
    }

    @Override
    protected Spatial loadModel() {
        Spatial temp = LuoYing.getAssetManager().loadModel(data.getAsString("file"));
        return temp;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        
        if (sway) {
            converterToGrass(spatial, swayGeometries);
        }
    }
    
    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        if (scene.isInitialized()) {
            // 把植皮移到地形上面
            makePlantOnTerrain(scene);
        } else {
            sceneListener = new SceneListenerAdapter() {
                @Override
                public void onSceneLoaded(Scene scene) {
                    // 把植皮移到地形上面
                    makePlantOnTerrain(scene);
                    // 在处理完位置点之后就可以不再需要侦听了
                    scene.removeSceneListener(this);
                }
            };
            scene.addSceneListener(sceneListener);
        }
    }
    
    @Override
    public void cleanup() {
        if (sceneListener != null) {
            scene.removeSceneListener(sceneListener);
        }
        super.cleanup();
    }

    private void makePlantOnTerrain(Scene scene) {
        // 在场景载入完毕之后将植皮位置移到terrain节点的上面。
        List<TerrainEntity> sos = scene.getEntities(TerrainEntity.class, new ArrayList<TerrainEntity>());
        Vector3f location = null;
        for (TerrainEntity terrain : sos) {
            Vector3f terrainPoint = terrain.getHeight(spatial.getWorldTranslation().x, spatial.getWorldTranslation().z);
            if (terrainPoint != null) {
                if (location == null || terrainPoint.y > location.y) {
                    location = terrainPoint;
                }
            }
        }
        
        if (location != null) {
            location.addLocal(0, -0.1f, 0); // 下移一点
            RigidBodyControl rbc = spatial.getControl(RigidBodyControl.class);
            if (rbc != null) {
                rbc.setPhysicsLocation(location);
            } else {
                spatial.setLocalTranslation(location);
            }
        } 
    }

    private void converterToGrass(Spatial model, List<String> geometries) {
        if (model instanceof Geometry) {
            if (geometries == null || geometries.contains(model.getName())) {
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
}
