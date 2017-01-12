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
import name.huliqing.editor.converter.SimpleDataConverter;
import name.huliqing.editor.converter.TextFieldConverter;
import name.huliqing.editor.converter.scene.EntitiesPropertyConverter;
import name.huliqing.editor.converter.scene.SceneConverter;
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
        entityBase.addPropertyConverter("location", TextFieldConverter.class);
        entityBase.addPropertyConverter("rotation", TextFieldConverter.class);
        entityBase.addPropertyConverter("scale", TextFieldConverter.class);
        entityBase.addPropertyConverter("mat", TextFieldConverter.class);
        entityBase.addPropertyConverter("modules", TextFieldConverter.class);
        entityBase.addPropertyConverter("objectDatas", TextFieldConverter.class);
        
        ConverterDefine entitySkyBox = new ConverterDefine("entitySkyBox", SimpleDataConverter.class);
        entitySkyBox.extendsFrom(entityBase);
        
        addConverter(scene);
        addConverter(entitySkyBox);
    }
    
    private static void addConverter(ConverterDefine cd) {
        MAPPING.put(cd.tagName, cd);
    }
    
    public final static DataConverter createConverter(ObjectData data, PropertyConverter parent)  {
        ConverterDefine cd = MAPPING.get(data.getTagName());
        if (cd == null)
            throw new NullPointerException("ConverterDefine not found, tagName=" + data.getTagName());
        DataConverter c;
        try {
            c = cd.converter.newInstance();
            c.initialize(data, cd.propertyConverters, parent);
            return c;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ConverterManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
