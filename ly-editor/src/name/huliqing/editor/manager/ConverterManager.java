/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.converter.FieldConverter;
import name.huliqing.editor.converter.define.ConverterDefine;
import name.huliqing.editor.converter.define.DataDefine;
import name.huliqing.editor.converter.define.Feature;
import name.huliqing.editor.converter.define.FieldDefine;
import name.huliqing.editor.edit.JfxAbstractEdit;
import name.huliqing.luoying.utils.FileUtils;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.luoying.xml.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author huliqing
 */
public class ConverterManager {

    private static final Logger LOG = Logger.getLogger(ConverterManager.class.getName());
    
    private final static Map<String, ConverterDefine> CONVERTER_MAP = new HashMap();
    private final static Map<String, DataDefine> DATA_DEFINES = new HashMap();
    
    public static void initialize() {
        try {
            CONVERTER_MAP.clear();
            DATA_DEFINES.clear();
            initializeInner("data/converter/base.xml");
            initializeInner("data/converter/entity.xml");
            initializeInner("data/converter/scene.xml");
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(ConverterManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void initializeInner(String filePath) throws IOException, ParserConfigurationException, SAXException {
        File converterFiles = new File(filePath);
        String xmlStr = FileUtils.readFile(converterFiles, "utf-8");
        Element root = XmlUtils.newDocument(xmlStr).getDocumentElement();
        NodeList converters = root.getElementsByTagName("converter");
        int converterSize = converters.getLength();
        for (int i = 0; i < converterSize; i++) {
            Element converterEle = (Element) converters.item(i);
            Map<String, Feature> converterFeatures = loadFeatures(converterEle);
            ConverterDefine converter = new ConverterDefine(XmlUtils.getAttributes(converterEle), converterFeatures);
            CONVERTER_MAP.put(converter.getName(), converter);
        }
        
        NodeList datas = root.getElementsByTagName("data");
        int dataSize = datas.getLength();
        
        for (int i = 0; i < dataSize; i++) {
            Element dataElement = (Element) datas.item(i);
            Map<String, Feature> dataFeatures = loadFeatures(dataElement);
            Map<String, FieldDefine> fieldDefines = loadFieldDefines(dataElement);
            DataDefine df = new DataDefine(XmlUtils.getAttributes(dataElement), fieldDefines, dataFeatures);
            DATA_DEFINES.put(df.getName(), df);
        }
    }
    
    private static Map<String, FieldDefine> loadFieldDefines(Element ele) {
        NodeList fields = ele.getElementsByTagName("field");
        if (fields == null || fields.getLength() <= 0) 
            return null;
        int fieldSize = fields.getLength();
        Map<String, FieldDefine> fieldDefines = new LinkedHashMap(fieldSize);
        for (int i = 0; i < fieldSize; i++) {
            Element fieldEle = (Element) fields.item(i);
            Map<String, Feature> fieldFeatures = loadFeatures(fieldEle);
            FieldDefine fieldDefine = new FieldDefine(XmlUtils.getAttributes(fieldEle), fieldFeatures);
            fieldDefines.put(fieldDefine.getName(), fieldDefine);
        }
        return fieldDefines;
    }
    
    private static Map<String, Feature> loadFeatures(Element e) {
        NodeList featuresList = e.getElementsByTagName("feature");
        if (featuresList == null || featuresList.getLength() <= 0) {
            return null;
        }
        int featureSize = featuresList.getLength();
        Map<String, Feature> features = new LinkedHashMap(featureSize);
        for (int i = 0; i < featureSize; i++) {
            Element featureEle = (Element) featuresList.item(i);
            Feature feature = new Feature(XmlUtils.getAttributes(featureEle));
            features.put(feature.getName(), feature);
        }
        return features;
    }
    
    public final static DataDefine getDataDefine(String name) {
        return DATA_DEFINES.get(name);
    }
    
    public final static DataConverter createDataConverter(JfxAbstractEdit edit, ObjectData data, FieldConverter parent)  {
        DataDefine dd = DATA_DEFINES.get(data.getTagName());
        if (dd == null) {
           throw new NullPointerException("Could not find DataDefine by tagName=" + data.getTagName());
        }
        ConverterDefine converter = CONVERTER_MAP.get(dd.getConverter());
        try {
            DataConverter dc = converter.createConverter();
            dc.setParent(parent);
            dc.setFeatures(dd.getFeatures());
            dc.setFieldDefines(dd.getFieldDefines());
            dc.setData(data);
            dc.setEdit(edit);
            return dc;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ConverterManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public final static FieldConverter createPropertyConverter(JfxAbstractEdit edit, FieldDefine fd, DataConverter parent) {
        ConverterDefine converter = CONVERTER_MAP.get(fd.getConverter());
        FieldConverter pc;
        try {
            pc = converter.createConverter();
            pc.setParent(parent);
            pc.setFeatures(fd.getFeatures());
            pc.setField(fd.getName());
            pc.setEdit(edit);
            return pc;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ConverterManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
