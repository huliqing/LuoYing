/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.manager;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.converter.ConverterDefine;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.converter.PropertyConverterDefine;
import name.huliqing.editor.converter.SimpleDataConverter;
import name.huliqing.editor.converter.tiles.TextFieldConverter;
import name.huliqing.editor.converter.entity.EntityDataConverter;
import name.huliqing.editor.converter.tiles.EntitiesPropertyConverter;
import name.huliqing.editor.converter.tiles.QuaternionConverter;
import name.huliqing.editor.converter.tiles.ColorFieldConverter;
import name.huliqing.editor.converter.tiles.FileFieldConverter;
import name.huliqing.editor.converter.tiles.Vector3fConverter;
import name.huliqing.editor.edit.JfxAbstractEdit;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.editor.converter.PropertyConverter;
import name.huliqing.editor.converter.tiles.ChoiceFieldConverter;

/**
 * @author huliqing
 */
public class ConverterManager {

    private static final Logger LOG = Logger.getLogger(ConverterManager.class.getName());
    
    private final static Map<String, ConverterDefine> MAPPING = new LinkedHashMap();
    
    /** 指定要隐藏的字段, 格式:"field1,field2,..." */
    public final static String FEATURE_HIDE_FIELDS = "hideFields";
    
    /** 指定要DISABLED的字段，格式:"field1,field2,..." */
    public final static String FEATURE_FIELD_DISABLED = "disabled";
    /** 让字段折叠 */
    public final static String FEATURE_FIELD_COLLAPSED = "collapsed";
    
    public static void initialize() {

        // ==== Base converter
        ConverterDefine base = new ConverterDefine("base", null);
        ConverterDefine scene = new ConverterDefine("scene", SimpleDataConverter.class);
        ConverterDefine entityBase = new ConverterDefine("entityBase", null);
        ConverterDefine entityModelBase = new ConverterDefine("entityModelBase", null);
        ConverterDefine entityAdvanceWater = new ConverterDefine("entityAdvanceWater", EntityDataConverter.class);
        ConverterDefine entityAmbientLight = new ConverterDefine("entityAmbientLight", EntityDataConverter.class);
        ConverterDefine entityChaseCamera = new ConverterDefine("entityChaseCamera", EntityDataConverter.class);
        ConverterDefine entityDirectionalLightFilterShadow = new ConverterDefine("entityDirectionalLightFilterShadow", EntityDataConverter.class);
        ConverterDefine entityDirectionalLight = new ConverterDefine("entityDirectionalLight", EntityDataConverter.class);
        ConverterDefine entityGrass = new ConverterDefine("entityGrass", EntityDataConverter.class);
        ConverterDefine entityPhysics = new ConverterDefine("entityPhysics", EntityDataConverter.class);
        ConverterDefine entitySimpleModel = new ConverterDefine("entitySimpleModel", EntityDataConverter.class);
        ConverterDefine entitySimpleTerrain = new ConverterDefine("entitySimpleTerrain", EntityDataConverter.class);
        ConverterDefine entitySimpleWater = new ConverterDefine("entitySimpleWater", EntityDataConverter.class);
        ConverterDefine entitySkyBox = new ConverterDefine("entitySkyBox", EntityDataConverter.class);
        ConverterDefine entityTree = new ConverterDefine("entityTree", EntityDataConverter.class);
        
        addConverter(base);
        addConverter(scene);
        addConverter(entityBase);
        addConverter(entityModelBase);
        addConverter(entityAdvanceWater);
        addConverter(entityAmbientLight);
        addConverter(entityChaseCamera);
        addConverter(entityDirectionalLight);
        addConverter(entityDirectionalLightFilterShadow);
        addConverter(entityGrass);
        addConverter(entityPhysics);
        addConverter(entitySimpleModel);
        addConverter(entitySimpleTerrain);
        addConverter(entitySimpleWater);
        addConverter(entitySkyBox);
        addConverter(entityTree);
                
        // ==== Extends
        entityBase.extendsFrom(base.getTagName());
        scene.extendsFrom(base.getTagName()); 
        entityModelBase.extendsFrom(entityBase.getTagName());
        entityAdvanceWater.extendsFrom(entityBase.getTagName());
        entityAmbientLight.extendsFrom(entityBase.getTagName());
        entityChaseCamera.extendsFrom(entityBase.getTagName());
        entityDirectionalLight.extendsFrom(entityBase.getTagName());
        entityDirectionalLightFilterShadow.extendsFrom(entityBase.getTagName());
        entityGrass.extendsFrom(entityModelBase.getTagName());
        entityPhysics.extendsFrom(entityBase.getTagName());
        entitySimpleModel.extendsFrom(entityModelBase.getTagName());
        entitySimpleTerrain.extendsFrom(entityModelBase.getTagName());
        entitySimpleWater.extendsFrom(entityModelBase.getTagName());
        entitySkyBox.extendsFrom(entityModelBase.getTagName());
        entityTree.extendsFrom(entityModelBase.getTagName());
        
        // ==== Features
        entityAdvanceWater.addFeature(FEATURE_HIDE_FIELDS, "rotation");
        
        // ==== Property Converter
        
        // -- base
        base.addPropertyConverter("id", TextFieldConverter.class)
                .addFeature(FEATURE_FIELD_DISABLED, "true")
                .addFeature(FEATURE_FIELD_COLLAPSED, "true");
        base.addPropertyConverter("extends", TextFieldConverter.class)
                .addFeature(FEATURE_FIELD_DISABLED, "true")
                .addFeature(FEATURE_FIELD_COLLAPSED, "true");
//        base.addPropertyConverter("dataClass", TextFieldConverter.class);
//        base.addPropertyConverter("dataLoaderClass", TextFieldConverter.class);
//        base.addPropertyConverter("dataProcessorClass", TextFieldConverter.class);
        
        // ---- Scene converter 
        scene.addPropertyConverter("entities", EntitiesPropertyConverter.class);
        scene.addPropertyConverter("progress", TextFieldConverter.class);
        
        // ---- Entity converter
        entityBase.addPropertyConverter("name", TextFieldConverter.class).addFeature(FEATURE_FIELD_COLLAPSED, "true");
        entityBase.addPropertyConverter("location", Vector3fConverter.class);
        entityBase.addPropertyConverter("rotation", QuaternionConverter.class);
        entityBase.addPropertyConverter("scale", Vector3fConverter.class);
        entityBase.addPropertyConverter("mat", TextFieldConverter.class).addFeature(FEATURE_FIELD_COLLAPSED, "true");;
        entityBase.addPropertyConverter("modules", TextFieldConverter.class).addFeature(FEATURE_FIELD_COLLAPSED, "true");;
        entityBase.addPropertyConverter("objectDatas", TextFieldConverter.class).addFeature(FEATURE_FIELD_COLLAPSED, "true");
        
        entityModelBase.addPropertyConverter("shadowMode", ChoiceFieldConverter.class)
                .addFeature(ChoiceFieldConverter.FEATURE_ITEMS, "Off,Cast,Receive,CastAndReceive,Inherit");
        entityModelBase.addPropertyConverter("cullHint", ChoiceFieldConverter.class)
                .addFeature(ChoiceFieldConverter.FEATURE_ITEMS, "Inherit,Dynamic,Always,Never");
        entityModelBase.addPropertyConverter("queueBucket", ChoiceFieldConverter.class)
                .addFeature(ChoiceFieldConverter.FEATURE_ITEMS, "Opaque,Transparent,Sky,Translucent,Gui,Inherit");
        entityModelBase.addPropertyConverter("preferUnshaded", TextFieldConverter.class);
        
        entityAdvanceWater.addPropertyConverter("causticsTexture", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("center", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("colorExtinction", TextFieldConverter.class);
        entityAdvanceWater.addPropertyConverter("deepWaterColor", ColorFieldConverter.class);
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
        
        entityTree.addPropertyConverter("file", FileFieldConverter.class);
        entityTree.addPropertyConverter("randomScale", TextFieldConverter.class);
        entityTree.addPropertyConverter("minScale", TextFieldConverter.class);
        entityTree.addPropertyConverter("maxScale", TextFieldConverter.class);
     

    }
    
    private static void addConverter(ConverterDefine cd) {
        MAPPING.put(cd.getTagName(), cd);
    }
    
    public final static ConverterDefine getConverterDefine(String tagName) {
        return MAPPING.get(tagName);
    }
    
    public final static DataConverter createConverter(JfxAbstractEdit edit, ObjectData data)  {
        ConverterDefine cd = MAPPING.get(data.getTagName());
        if (cd == null)
            throw new NullPointerException("ConverterDefine not found, tagName=" + data.getTagName());
        DataConverter dc;
        try {
            dc = cd.getConverter().newInstance();
            dc.setData(data);
            dc.setEdit(edit);
            dc.setPropertyConverterDefines(cd.getPropertyConverters());
            dc.setFeatures(cd.getFeatures());
            return dc;
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public final static PropertyConverter createPropertyConverter(JfxAbstractEdit edit, PropertyConverterDefine pcd) {
        try {
            PropertyConverter pc = pcd.getPropertyConverter().newInstance();
            pc.setProperty(pcd.getPropertyName());
            pc.setFeatures(pcd.getUnmodifiableFeatures());
            pc.setEdit(edit);
            return pc;
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, "Could not create PropertyConverter", ex);
        }
        return null;
    }
}
