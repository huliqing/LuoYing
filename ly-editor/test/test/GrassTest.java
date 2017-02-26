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
package test;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shader.VarType;
import com.jme3.system.AppSettings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author huliqing
 */
public class GrassTest extends SimpleApplication {

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setFrameRate(60);
        GrassTest test = new GrassTest();
        test.setSettings(settings);
        test.start();
    }
    
    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(10);

        rootNode.addLight(new AmbientLight());
        rootNode.addLight(new DirectionalLight());
        
//        Spatial model = assetManager.loadModel("Models/plants/Flower-242.j3o");
        Spatial model = assetManager.loadModel("Models/plants/Bush-1086.j3o");
        Spatial clone = model.clone(true);
        converterMaterial(clone, Arrays.asList("Bush-1086-geom-0"));
        
        for (int i = 0; i < 5; i++) {
            Spatial m = clone.clone();
            m.setLocalTranslation(FastMath.nextRandomFloat() * 15, 0, FastMath.nextRandomFloat() * 15);
            rootNode.attachChild(m);
        }
    }
    
    private void converterMaterial(Spatial model, List<String> geometries) {
        if (model instanceof Geometry) {
            if (geometries == null || geometries.contains(model.getName())) {
                model.setMaterial(convertToGrassMat(((Geometry) model).getMaterial()));
            }
        } else if (model instanceof Node) {
            for (Spatial child : ((Node) model).getChildren()) {
                converterMaterial(child, geometries);
            }
        }
    }
    
    private Material convertToGrassMat(Material originMat) {
        Material grassMat = new Material(getAssetManager(), "LuoYingSys/Assets/MatDefs/Grass/Grass.j3md");
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
        grassMat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        
        grassMat.setBoolean("Swaying", true);
        grassMat.setVector3("SwayData", new Vector3f(1.5f, 0, 1));
        grassMat.setVector2("Wind", new Vector2f(1f, 1f));
        
        return grassMat;
    }
    
    private void transferMatParam(Material from, Material to, String matName, VarType varType) {
        MatParam matParam = from.getParam(matName);
        if (matParam != null && matParam.getVarType() == varType) {
            to.setParam(matName, varType, matParam.getValue());
        }
    }
}
