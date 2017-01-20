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
import name.huliqing.editor.converter.property.TextConverter;
import name.huliqing.editor.converter.data.EntityDataConverter;
import name.huliqing.editor.converter.property.EntitiesPropertyConverter;
import name.huliqing.editor.converter.property.QuaternionConverter;
import name.huliqing.editor.converter.property.ColorConverter;
import name.huliqing.editor.converter.property.FileConverter;
import name.huliqing.editor.converter.property.Vector3fConverter;
import name.huliqing.editor.edit.JfxAbstractEdit;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.editor.converter.PropertyConverter;
import name.huliqing.editor.converter.property.CheckBoxConverter;
import name.huliqing.editor.converter.property.ChoiceConverter;
import static name.huliqing.editor.converter.property.FileConverter.FEATURE_FILTERS;
import name.huliqing.editor.converter.property.Vector2fConverter;

/**
 * @author huliqing
 */
public class ConverterManager {

    private static final Logger LOG = Logger.getLogger(ConverterManager.class.getName());
    
    private final static Map<String, ConverterDefine> MAPPING = new LinkedHashMap();
    
    public static void initialize() {

        // ==== Base converter
        ConverterDefine base = new ConverterDefine("base", null);
        ConverterDefine scene = new ConverterDefine("scene", SimpleDataConverter.class);
        ConverterDefine entityBase = new ConverterDefine("entityBase", null);
        ConverterDefine entityModelBase = new ConverterDefine("entityModelBase", null);
        ConverterDefine entityPlantBase = new ConverterDefine("entityPlantBase", null);
        ConverterDefine entityAdvanceWater = new ConverterDefine("entityAdvanceWater", EntityDataConverter.class);
        ConverterDefine entityAmbientLight = new ConverterDefine("entityAmbientLight", EntityDataConverter.class);
        ConverterDefine entityAudio = new ConverterDefine("entityAudio", EntityDataConverter.class);
        ConverterDefine entityChaseCamera = new ConverterDefine("entityChaseCamera", EntityDataConverter.class);
        ConverterDefine entityDirectionalLight = new ConverterDefine("entityDirectionalLight", EntityDataConverter.class);
        ConverterDefine entityDirectionalLightFilterShadow = new ConverterDefine("entityDirectionalLightFilterShadow", EntityDataConverter.class);
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
        addConverter(entityPlantBase);
        addConverter(entityAdvanceWater);
        addConverter(entityAmbientLight);
        addConverter(entityAudio);
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
        entityPlantBase.extendsFrom(entityModelBase.getTagName());
        entityAdvanceWater.extendsFrom(entityBase.getTagName());
        entityAmbientLight.extendsFrom(entityBase.getTagName());
        entityAudio.extendsFrom(entityBase.getTagName());
        entityChaseCamera.extendsFrom(entityBase.getTagName());
        entityDirectionalLight.extendsFrom(entityBase.getTagName());
        entityDirectionalLightFilterShadow.extendsFrom(entityBase.getTagName());
        entityGrass.extendsFrom(entityPlantBase.getTagName());
        entityPhysics.extendsFrom(entityBase.getTagName());
        entitySimpleModel.extendsFrom(entityModelBase.getTagName());
        entitySimpleTerrain.extendsFrom(entityModelBase.getTagName());
        entitySimpleWater.extendsFrom(entityModelBase.getTagName());
        entitySkyBox.extendsFrom(entityModelBase.getTagName());
        entityTree.extendsFrom(entityPlantBase.getTagName());
        
        // ==== Property Converter
        
        // -- base
        base.addPropertyConverter("id", TextConverter.class)
                .addFeature(PropertyConverter.FEATURE_DISABLED, "true")
                .addFeature(PropertyConverter.FEATURE_COLLAPSED, "true");
        base.addPropertyConverter("extends", TextConverter.class)
                .addFeature(PropertyConverter.FEATURE_DISABLED, "true")
                .addFeature(PropertyConverter.FEATURE_COLLAPSED, "true");
//        base.addPropertyConverter("dataClass", TextFieldConverter.class);
//        base.addPropertyConverter("dataLoaderClass", TextFieldConverter.class);
//        base.addPropertyConverter("dataProcessorClass", TextFieldConverter.class);
        
        // ---- Scene converter 
        scene.addPropertyConverter("entities", EntitiesPropertyConverter.class);
        scene.addPropertyConverter("progress", TextConverter.class);
        
        // ---- Entity converter
        entityBase.addPropertyConverter("name", TextConverter.class).addFeature(PropertyConverter.FEATURE_COLLAPSED, "true");
        entityBase.addPropertyConverter("location", Vector3fConverter.class);
        entityBase.addPropertyConverter("rotation", QuaternionConverter.class);
        entityBase.addPropertyConverter("scale", Vector3fConverter.class);
        entityBase.addPropertyConverter("mat", TextConverter.class).addFeature(PropertyConverter.FEATURE_COLLAPSED, "true");;
        entityBase.addPropertyConverter("modules", TextConverter.class).addFeature(PropertyConverter.FEATURE_COLLAPSED, "true");;
        entityBase.addPropertyConverter("objectDatas", TextConverter.class).addFeature(PropertyConverter.FEATURE_COLLAPSED, "true");
        
        entityModelBase.addPropertyConverter("shadowMode", ChoiceConverter.class)
                .addFeature(ChoiceConverter.FEATURE_ITEMS, "Off,Cast,Receive,CastAndReceive,Inherit");
        entityModelBase.addPropertyConverter("cullHint", ChoiceConverter.class)
                .addFeature(ChoiceConverter.FEATURE_ITEMS, "Inherit,Dynamic,Always,Never");
        entityModelBase.addPropertyConverter("queueBucket", ChoiceConverter.class)
                .addFeature(ChoiceConverter.FEATURE_ITEMS, "Opaque,Transparent,Sky,Translucent,Gui,Inherit");
        entityModelBase.addPropertyConverter("preferUnshaded", TextConverter.class);
        
        entityAdvanceWater.addFeature(DataConverter.FEATURE_HIDE_FIELDS, "rotation");
        entityAdvanceWater.addPropertyConverter("center", Vector3fConverter.class);
        entityAdvanceWater.addPropertyConverter("waterHeight", TextConverter.class);
        entityAdvanceWater.addPropertyConverter("radius", TextConverter.class);
        entityAdvanceWater.addPropertyConverter("colorExtinction", Vector3fConverter.class);
        entityAdvanceWater.addPropertyConverter("deepWaterColor", ColorConverter.class);
        entityAdvanceWater.addPropertyConverter("foamExistence", Vector3fConverter.class);
        entityAdvanceWater.addPropertyConverter("foamHardness", TextConverter.class);
        entityAdvanceWater.addPropertyConverter("foamIntensity", TextConverter.class);
        entityAdvanceWater.addPropertyConverter("lightColor", ColorConverter.class);
        entityAdvanceWater.addPropertyConverter("lightDirection", Vector3fConverter.class);
        entityAdvanceWater.addPropertyConverter("maxAmplitude", TextConverter.class);
        entityAdvanceWater.addPropertyConverter("normalScale", TextConverter.class);
        entityAdvanceWater.addPropertyConverter("reflectionDisplace", TextConverter.class);
        entityAdvanceWater.addPropertyConverter("reflectionMapSize", TextConverter.class);
        entityAdvanceWater.addPropertyConverter("refractionConstant", TextConverter.class);
        entityAdvanceWater.addPropertyConverter("refractionStrength", TextConverter.class);
        entityAdvanceWater.addPropertyConverter("shapeType", ChoiceConverter.class).addFeature(ChoiceConverter.FEATURE_ITEMS, "Square,Circular");
        entityAdvanceWater.addPropertyConverter("shininess", TextConverter.class);
        entityAdvanceWater.addPropertyConverter("shoreHardness", TextConverter.class);
        entityAdvanceWater.addPropertyConverter("speed", TextConverter.class);
        entityAdvanceWater.addPropertyConverter("sunScale", TextConverter.class);
        entityAdvanceWater.addPropertyConverter("underWaterFogDistance", TextConverter.class);
        entityAdvanceWater.addPropertyConverter("useCaustics", CheckBoxConverter.class);
        entityAdvanceWater.addPropertyConverter("useFoam", CheckBoxConverter.class);
        entityAdvanceWater.addPropertyConverter("useHQShoreline", CheckBoxConverter.class);
        entityAdvanceWater.addPropertyConverter("useRefraction", CheckBoxConverter.class);
        entityAdvanceWater.addPropertyConverter("useRipples", CheckBoxConverter.class);
        entityAdvanceWater.addPropertyConverter("useSpecular", CheckBoxConverter.class);
        entityAdvanceWater.addPropertyConverter("waterColor", ColorConverter.class);
        entityAdvanceWater.addPropertyConverter("waterTransparency", TextConverter.class);
        entityAdvanceWater.addPropertyConverter("waveScale", TextConverter.class);
        entityAdvanceWater.addPropertyConverter("windDirection", Vector2fConverter.class);
        entityAdvanceWater.addPropertyConverter("useSceneLight", TextConverter.class);
        entityAdvanceWater.addPropertyConverter("causticsTexture", FileConverter.class);
        entityAdvanceWater.addPropertyConverter("foamTexture", FileConverter.class);
        entityAdvanceWater.addPropertyConverter("heightTexture", FileConverter.class);
        entityAdvanceWater.addPropertyConverter("normalTexture", FileConverter.class);
        
        entityAmbientLight.addFeature(DataConverter.FEATURE_HIDE_FIELDS, "location,rotation,scale");
        entityAmbientLight.addPropertyConverter("color", ColorConverter.class);
        
        entityAudio.addFeature(DataConverter.FEATURE_HIDE_FIELDS, "rotation,scale,mat");
        entityAudio.addPropertyConverter("debug", CheckBoxConverter.class);
        entityAudio.addPropertyConverter("sound", TextConverter.class);
        
        entityChaseCamera.addFeature(DataConverter.FEATURE_HIDE_FIELDS, "scale,mat");
        entityChaseCamera.addPropertyConverter("debug", CheckBoxConverter.class);
        entityChaseCamera.addPropertyConverter("smoothMotion", CheckBoxConverter.class);
        entityChaseCamera.addPropertyConverter("trailingEnabled", CheckBoxConverter.class);
        entityChaseCamera.addPropertyConverter("invertVerticalAxis", CheckBoxConverter.class);
        entityChaseCamera.addPropertyConverter("lookAtOffset", Vector3fConverter.class);
        entityChaseCamera.addPropertyConverter("zoomSensitivity", TextConverter.class);
        entityChaseCamera.addPropertyConverter("rotationSpeed", TextConverter.class);
        entityChaseCamera.addPropertyConverter("rotationSensitivity", TextConverter.class);
        entityChaseCamera.addPropertyConverter("maxDistance", TextConverter.class);
        entityChaseCamera.addPropertyConverter("minDistance", TextConverter.class);
        entityChaseCamera.addPropertyConverter("defaultDistance", TextConverter.class);
        entityChaseCamera.addPropertyConverter("chasingSensitivity", TextConverter.class);
        entityChaseCamera.addPropertyConverter("downRotateOnCloseViewOnly", CheckBoxConverter.class);
        entityChaseCamera.addPropertyConverter("upVector", Vector3fConverter.class);
        entityChaseCamera.addPropertyConverter("hideCursorOnRotate", CheckBoxConverter.class);
        
        entityDirectionalLight.addFeature(DataConverter.FEATURE_HIDE_FIELDS, "location,rotation,scale,mat");
        entityDirectionalLight.addPropertyConverter("direction", Vector3fConverter.class);
        entityDirectionalLight.addPropertyConverter("color", ColorConverter.class);
        
        entityDirectionalLightFilterShadow.addFeature(DataConverter.FEATURE_HIDE_FIELDS, "location,rotation,scale,mat");
        entityDirectionalLightFilterShadow.addPropertyConverter("disabledOnPlatforms", TextConverter.class);
        entityDirectionalLightFilterShadow.addPropertyConverter("shadowIntensity", TextConverter.class);
        entityDirectionalLightFilterShadow.addPropertyConverter("shadowMapSize", TextConverter.class);
        entityDirectionalLightFilterShadow.addPropertyConverter("shadowMaps", TextConverter.class);
        
        entityPlantBase.addPropertyConverter("file", FileConverter.class).addFeature(FEATURE_FILTERS, "Model Files|*.j3o|*.obj|*.mesh.xml,All Files|*.*");
        entityPlantBase.addPropertyConverter("randomScale", CheckBoxConverter.class);
        entityPlantBase.addPropertyConverter("minScale", TextConverter.class);
        entityPlantBase.addPropertyConverter("maxScale", TextConverter.class);
        
        entityPhysics.addFeature(DataConverter.FEATURE_HIDE_FIELDS, "location,rotation,scale,mat,modules,objectDatas");
        entityPhysics.addPropertyConverter("debug", CheckBoxConverter.class);
        entityPhysics.addPropertyConverter("gravity", Vector3fConverter.class);
        entityPhysics.addPropertyConverter("broadphaseType", ChoiceConverter.class).addFeature(ChoiceConverter.FEATURE_ITEMS, "SIMPLE,AXIS_SWEEP_3,AXIS_SWEEP_3_32,DBVT");
        entityPhysics.addPropertyConverter("threadingType", ChoiceConverter.class).addFeature(ChoiceConverter.FEATURE_ITEMS, "SEQUENTIAL,PARALLEL");
        entityPhysics.addPropertyConverter("speed", TextConverter.class);
        entityPhysics.addPropertyConverter("worldMax", Vector3fConverter.class);
        entityPhysics.addPropertyConverter("worldMin", Vector3fConverter.class);
        entityPhysics.addPropertyConverter("accuracy", TextConverter.class);
        entityPhysics.addPropertyConverter("maxSubSteps", TextConverter.class);
        entityPhysics.addPropertyConverter("solverNumIterations", TextConverter.class);
        
        entitySimpleModel.addPropertyConverter("file", FileConverter.class);
        
        entitySimpleTerrain.addPropertyConverter("file", FileConverter.class);
        
        entitySimpleWater.addFeature(DataConverter.FEATURE_HIDE_FIELDS, "mat,modules,objectDatas,shadowMode,cullHint,queueBucket,preferUnshaded");
        entitySimpleWater.addPropertyConverter("waterModel", FileConverter.class);
        entitySimpleWater.addPropertyConverter("waterColor", ColorConverter.class);
        entitySimpleWater.addPropertyConverter("texScale", TextConverter.class);
        entitySimpleWater.addPropertyConverter("waveSpeed", TextConverter.class);
        entitySimpleWater.addPropertyConverter("distortionMix", TextConverter.class);
        entitySimpleWater.addPropertyConverter("distortionScale", TextConverter.class);
        entitySimpleWater.addPropertyConverter("foamMap", FileConverter.class);
        entitySimpleWater.addPropertyConverter("foamScale", Vector2fConverter.class);
        entitySimpleWater.addPropertyConverter("foamMaskMap", FileConverter.class);
        entitySimpleWater.addPropertyConverter("foamMaskScale", TextConverter.class);

        entitySkyBox.addFeature(DataConverter.FEATURE_HIDE_FIELDS, "location,rotation,scale"
                + ",mat,modules,objectDatas,shadowMode,cullHint,queueBucket,preferUnshaded");
        entitySkyBox.addPropertyConverter("west", FileConverter.class);
        entitySkyBox.addPropertyConverter("east", FileConverter.class);
        entitySkyBox.addPropertyConverter("north", FileConverter.class);
        entitySkyBox.addPropertyConverter("south", FileConverter.class);
        entitySkyBox.addPropertyConverter("up", FileConverter.class);
        entitySkyBox.addPropertyConverter("down", FileConverter.class);
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
