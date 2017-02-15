/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
 * 转换器管理及配置
 * @author huliqing
 */
public class ConverterManager {

    private static final Logger LOG = Logger.getLogger(ConverterManager.class.getName());
    
    private final static Map<String, ConverterDefine> CONVERTER_MAP = new HashMap();
    private final static Map<String, DataDefine> DATA_DEFINES = new HashMap();
    
    public static void initialize() {
        CONVERTER_MAP.clear();
        DATA_DEFINES.clear();
        
        File rootDir = new File("data/converter");
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            LOG.log(Level.SEVERE, "data/converter direction not found!");
            return;
        }
        
        // 递归载入data/converter目录下的转换器配置，data/converter下允许多层次目录
        // 每个目录放一个config文件，config文件中每一行定义一个要载入的转换器配置
        loadConverterFromDir(rootDir);
    }
    
    /**
     * @param dir 
     */
    private static void loadConverterFromDir(File dir) {
        if (!dir.exists() || !dir.isDirectory())
            return;
        // 载入当前文件夹中的配置
        try {
            File configFile = new File(dir, "config");
            if (!configFile.exists() || !configFile.isFile()) {
                LOG.log(Level.WARNING, "Config file not found!");
            } else {
                loadConverterFromConfigFile(configFile);
            }
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, "Could not load converters, dir=" + dir.getAbsolutePath(), ex);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Could not load converters, dir=" + dir.getAbsolutePath(), ex);
        }
        // 载入子文件夹中的配置
        File[] children = dir.listFiles();
        for (File f : children) {
            loadConverterFromDir(f);
        }
    }
    
    private static void loadConverterFromConfigFile(File configFile) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        // 从config中读取 converter xml配置文件
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), "utf-8"));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().length() <= 0) 
                continue;
            if (line.trim().startsWith("#")) 
                continue;
            File converterFile = new File(configFile.getParent(), line);
            if (!converterFile.exists() || !converterFile.isFile()) 
                continue;
            try {
                loadConverterFile(converterFile);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Could not load converter file=" + line, e);
            }
        }
        try {
            br.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    private static void loadConverterFile(File converterFile) throws IOException, ParserConfigurationException, SAXException {
        String xmlStr = FileUtils.readFile(converterFile, "utf-8");
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
    
    public final static FieldConverter createPropertyConverter(JfxAbstractEdit edit, ObjectData od, FieldDefine fd, DataConverter parent) {
        ConverterDefine converter = CONVERTER_MAP.get(fd.getConverter());
        FieldConverter pc;
        try {
            pc = converter.createConverter();
            pc.setData(od);
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
