/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.converter.ConverterDefine;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.converter.PropertyConverter;
import name.huliqing.editor.converter.tiles.TextFieldConverter;
import name.huliqing.editor.converter.entity.EntityDataConverter;
import name.huliqing.editor.converter.scene.EntitiesPropertyConverter;
import name.huliqing.editor.converter.scene.SceneConverter;
import name.huliqing.editor.converter.tiles.QuaternionConverter;
import name.huliqing.editor.converter.tiles.Vector3fConverter;
import name.huliqing.editor.edit.JfxAbstractEdit;
import name.huliqing.luoying.xml.ObjectData;

/**
 *
 * @author huliqing
 */
public class ConverterManager {
    
    private final static Map<String, ConverterDefine> MAPPING = new HashMap();
    
    public static void initialize() {

        // ---- Base converter
        ConverterDefine base = new ConverterDefine("base", null);
        base.addPropertyConverter("id", TextFieldConverter.class);
        base.addPropertyConverter("extends", TextFieldConverter.class);
//        base.addPropertyConverter("dataClass", TextFieldConverter.class);
//        base.addPropertyConverter("dataLoaderClass", TextFieldConverter.class);
//        base.addPropertyConverter("dataProcessorClass", TextFieldConverter.class);
        
        // ---- Scene converter 
        ConverterDefine scene = new ConverterDefine("scene", SceneConverter.class);
        scene.extendsFrom(base); 
        scene.addPropertyConverter("entities", EntitiesPropertyConverter.class);
        scene.addPropertyConverter("progress", TextFieldConverter.class);
        
        // ---- Entity converter
        ConverterDefine entityBase = new ConverterDefine("entityBase", null);
        entityBase.extendsFrom(base);
        entityBase.addPropertyConverter("name", TextFieldConverter.class);
        entityBase.addPropertyConverter("location", Vector3fConverter.class);
        entityBase.addPropertyConverter("rotation", QuaternionConverter.class);
        entityBase.addPropertyConverter("scale", Vector3fConverter.class);
        entityBase.addPropertyConverter("mat", TextFieldConverter.class);
        entityBase.addPropertyConverter("modules", TextFieldConverter.class);
        entityBase.addPropertyConverter("objectDatas", TextFieldConverter.class);
        
        ConverterDefine entitySkyBox = new ConverterDefine("entitySkyBox", EntityDataConverter.class);
        entitySkyBox.extendsFrom(entityBase);
        
        ConverterDefine entitySimpleTerrain = new ConverterDefine("entitySimpleTerrain", EntityDataConverter.class);
        entitySimpleTerrain.extendsFrom(entityBase);
        
        ConverterDefine entityTree = new ConverterDefine("entityTree", EntityDataConverter.class);
        entityTree.extendsFrom(entityBase);
        
        ConverterDefine entityGrass = new ConverterDefine("entityGrass", EntityDataConverter.class);
        entityGrass.extendsFrom(entityBase);
        
        ConverterDefine entitySimpleWater = new ConverterDefine("entitySimpleWater", EntityDataConverter.class);
        entitySimpleWater.extendsFrom(entityBase);
        
        ConverterDefine entityAdvanceWater = new ConverterDefine("entityAdvanceWater", EntityDataConverter.class);
        entityAdvanceWater.extendsFrom(entityBase);
        
        ConverterDefine entitySimpleModel = new ConverterDefine("entitySimpleModel", EntityDataConverter.class);
        entitySimpleModel.extendsFrom(entityBase);
        
        ConverterDefine entityPhysics = new ConverterDefine("entityPhysics", EntityDataConverter.class);
        entityPhysics.extendsFrom(entityBase);
        
        ConverterDefine entityDirectionalLight = new ConverterDefine("entityDirectionalLight", EntityDataConverter.class);
        entityDirectionalLight.extendsFrom(entityBase);
        
        ConverterDefine entityAmbientLight = new ConverterDefine("entityAmbientLight", EntityDataConverter.class);
        entityAmbientLight.extendsFrom(entityBase);
        
        ConverterDefine entityDirectionalLightFilterShadow = new ConverterDefine("entityDirectionalLightFilterShadow", EntityDataConverter.class);
        entityDirectionalLightFilterShadow.extendsFrom(entityBase);
        
        ConverterDefine entityChaseCamera = new ConverterDefine("entityChaseCamera", EntityDataConverter.class);
        entityChaseCamera.extendsFrom(entityBase);
        
        addConverter(scene);
        addConverter(entitySkyBox);
        addConverter(entitySimpleTerrain);
        addConverter(entityTree);
        addConverter(entityGrass);
        addConverter(entitySimpleWater);
        addConverter(entityAdvanceWater);
        addConverter(entitySimpleModel);
        addConverter(entityPhysics);
        addConverter(entityDirectionalLight);
        addConverter(entityAmbientLight);
        addConverter(entityDirectionalLightFilterShadow);
        addConverter(entityChaseCamera);
    }
    
    private static void addConverter(ConverterDefine cd) {
        MAPPING.put(cd.tagName, cd);
    }
    
    public final static DataConverter createConverter(JfxAbstractEdit editView, ObjectData data, PropertyConverter parent)  {
        ConverterDefine cd = MAPPING.get(data.getTagName());
        if (cd == null)
            throw new NullPointerException("ConverterDefine not found, tagName=" + data.getTagName());
        DataConverter c;
        try {
            c = cd.converter.newInstance();
            c.initialize(editView, data, cd.propertyConverters, parent);
            return c;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ConverterManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
