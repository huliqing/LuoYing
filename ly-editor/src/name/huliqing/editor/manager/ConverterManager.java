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
 * 转换器管理及配置
 * @author huliqing
 */
public class ConverterManager {

    private static final Logger LOG = Logger.getLogger(ConverterManager.class.getName());
    
    private final static Map<String, ConverterDefine> CONVERTER_MAP = new HashMap();
    private final static Map<String, DataDefine> DATA_DEFINES = new HashMap();
    
    /**
     * 给定转换器定义文件的根目录，重新载入转换器定义.该根目录下的转换器定义文件必须是以".xml"为后缀结尾的组件定义文件。
     * @param convertersDir 组件定义的根目录
     */
    public final static void reloadConverters(File convertersDir) {
        for (File converterFile : convertersDir.listFiles()) {
            if (converterFile.exists() && converterFile.isFile() && converterFile.getAbsolutePath().endsWith(".xml")) {
                try {
                    loadConverterFile(converterFile);
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "Could not load converter define file, file={0}", converterFile.getAbsolutePath());
                }
            }
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
            fieldDefines.put(fieldDefine.getField(), fieldDefine);
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
        if (converter == null) {
            throw new RuntimeException("Could not found ConverterDefine, converter=" + fd.getConverter());
        }
        FieldConverter pc;
        try {
            pc = converter.createConverter();
            pc.setData(od);
            pc.setParent(parent);
            pc.setFeatures(fd.getFeatures());
            pc.setField(fd.getField());
            pc.setEdit(edit);
            return pc;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ConverterManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
  
}
