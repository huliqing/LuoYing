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
import name.huliqing.editor.converter.tiles.ColorFieldConverter;
import name.huliqing.editor.converter.tiles.CullHintChoiceFieldConverter;
import name.huliqing.editor.converter.tiles.FileFieldConverter;
import name.huliqing.editor.converter.tiles.QueueBucketChoiceFieldConverter;
import name.huliqing.editor.converter.tiles.ShadowModeChoiceFieldConverter;
import name.huliqing.editor.converter.tiles.Vector3fConverter;
import name.huliqing.editor.edit.JfxAbstractEdit;
import name.huliqing.luoying.xml.ObjectData;

/**
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
        
        ConverterDefine entityModelBase = new ConverterDefine("entityModelBase", null);
        entityModelBase.extendsFrom(entityBase);
        entityModelBase.addPropertyConverter("shadowMode", ShadowModeChoiceFieldConverter.class);
        entityModelBase.addPropertyConverter("cullHint", CullHintChoiceFieldConverter.class);
        entityModelBase.addPropertyConverter("queueBucket", QueueBucketChoiceFieldConverter.class);
        entityModelBase.addPropertyConverter("preferUnshaded", TextFieldConverter.class);
        
        ConverterDefine entitySkyBox = new ConverterDefine("entitySkyBox", EntityDataConverter.class);
        entitySkyBox.extendsFrom(entityModelBase);
        
        ConverterDefine entitySimpleTerrain = new ConverterDefine("entitySimpleTerrain", EntityDataConverter.class);
        entitySimpleTerrain.extendsFrom(entityModelBase);
        
        ConverterDefine entityTree = new ConverterDefine("entityTree", EntityDataConverter.class);
        entityTree.extendsFrom(entityModelBase);
        entityTree.addPropertyConverter("file", FileFieldConverter.class);
        entityTree.addPropertyConverter("randomScale", TextFieldConverter.class);
        entityTree.addPropertyConverter("minScale", TextFieldConverter.class);
        entityTree.addPropertyConverter("maxScale", TextFieldConverter.class);
        
        ConverterDefine entityGrass = new ConverterDefine("entityGrass", EntityDataConverter.class);
        entityGrass.extendsFrom(entityModelBase);
        
        ConverterDefine entitySimpleWater = new ConverterDefine("entitySimpleWater", EntityDataConverter.class);
        entitySimpleWater.extendsFrom(entityModelBase);
        
        ConverterDefine entityAdvanceWater = new ConverterDefine("entityAdvanceWater", EntityDataConverter.class);
        entityAdvanceWater.extendsFrom(entityBase);
        entityAdvanceWater.addPropertyConverter("causticsTexture", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("center", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("colorExtinction", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("deepWaterColor", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("foamExistence", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("foamHardness", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("foamIntensity", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("foamTexture", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("heightTexture", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("lightColor", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("lightDirection", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("maxAmplitude", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("normalScale", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("normalTexture", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("radius", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("reflectionDisplace", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("reflectionMapSize", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("refractionConstant", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("refractionStrength", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("shapeType", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("shininess", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("shoreHardness", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("speed", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("sunScale", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("underWaterFogDistance", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("useCaustics", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("useFoam", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("useHQShoreline", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("useRefraction", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("useRipples", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("useSpecular", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("waterColor", ColorFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("waterHeight", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("waterTransparency", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("waveScale", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("windDirection", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("useSceneLight", TextFieldConverter.class);
        
        ConverterDefine entitySimpleModel = new ConverterDefine("entitySimpleModel", EntityDataConverter.class);
        entitySimpleModel.extendsFrom(entityModelBase);
        
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
