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
package name.huliqing.luoying.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import name.huliqing.luoying.LuoYingException;
import org.xml.sax.SAXException;

/**
 * 数据总管
 * @author huliqing
 */
public class DataFactory {
    private static final Logger LOG = Logger.getLogger(DataFactory.class.getName());
    
    private static IdGenerator idGenerator = new SimpleIdGenerator();
    
    // TagName -> ObjectData,用于注册自定义的数据容器
    private final static Map<String, Class<? extends ObjectData>> TAG_DATAS 
            = new HashMap<String, Class<? extends ObjectData>>();
    
    // TagName -> DataLoader, 自定义数据载入器
    private final static Map<String, Class<? extends DataLoader>> TAG_LOADERS 
            = new HashMap<String, Class<? extends DataLoader>>();
    
    // TagName -> DataProcessor, 自定义数据处理器
    private final static Map<String, Class<? extends DataProcessor>> TAG_PROCESSORS 
            = new HashMap<String, Class<? extends DataProcessor>>();
    
    private final static DataStore DATA_STORE = new DataStore();
    private final static DataStoreLoader DATA_STORE_LOADER = new DataStoreLoader();
    
    /**
     * 注册一个ID生成器，该ID生成器将用于为所有Data生成唯一id.
     * @param generator 
     */
    public static void registerIdGenerator(IdGenerator generator) {
        idGenerator = generator;
    }
    
    /**
     * 注册一个数据类型
     * @param tagName 
     * @param dataClass 
     */
    public static void registerData(String tagName, Class<? extends ObjectData> dataClass) {
        TAG_DATAS.put(tagName, dataClass);
//        LOG.log(Level.INFO, "registerData, {0} => {1}", new Object[] {tagName, dataClass});
    }
    
    /**
     * 注册一个数据载入器,如果dataLoaderClass为null,则清除已有的载入器.
     * @param tagName
     * @param dataLoaderClass 
     */
    public static void registerDataLoader(String tagName, Class<? extends DataLoader> dataLoaderClass) {
        if (dataLoaderClass == null) {
            TAG_LOADERS.remove(tagName);
            return;
        }
        TAG_LOADERS.put(tagName, dataLoaderClass);
//        LOG.log(Level.INFO, "registerDataLoader, {0} => {1}", new Object[] {tagName, dataLoaderClass});
    }
    
    /** 
     * 注册一个数据处理器
     * @param tagName 
     * @param dataProcessorClass 
     */
    public static void registerDataProcessor(String tagName, Class<? extends DataProcessor> dataProcessorClass) {
        if (dataProcessorClass == null) {
            TAG_PROCESSORS.remove(tagName);
            return;
        }
        TAG_PROCESSORS.put(tagName, dataProcessorClass);
//        LOG.log(Level.INFO, "registerDataProcessor, {0} => {1}", new Object[] {tagName, dataProcessorClass});
    }
    
    /**
     * 注册数据类型、数据载入器及数据处理器
     * @param tagName
     * @param dataClass
     * @param dataLoaderClass
     * @param dataProcessorClass 
     */
    public static void register(String tagName
            , Class<? extends ObjectData> dataClass
            , Class<? extends DataLoader> dataLoaderClass
            , Class<? extends DataProcessor> dataProcessorClass
            ) {
        registerData(tagName, dataClass);
        registerDataLoader(tagName, dataLoaderClass);
        registerDataProcessor(tagName, dataProcessorClass);
    }
    
    /**
     * 通过ID来创建并载入Data
     * @param <T>
     * @param id
     * @return 
     * @throws LuoYingException 如果无法为id创建Data
     */
    @SuppressWarnings("UseSpecificCatch")
    public static <T extends ObjectData> T createData(String id) {
        Proto proto = ProtoUtils.getProto(DATA_STORE, id);
        if (proto == null) {
            throw new LuoYingException("Could not find object, id=" + id);
        }
        
        try {
            // 这里如果指定了特殊的data类型，则使用指定的，否则默认使用ObjectData
            ObjectData data = (ObjectData) proto.getDataClass().newInstance();
            
            data.setProto(proto);
            data.setUniqueId(generateUniqueId());
            
            // 如果指定了Data载入器则使用这个载入器来载入数据，否则不理, 允许不注册指定的载入器及Data类型。
            if (NullLoader.class != proto.getDataLoaderClass()) {
                DataLoader dl = (DataLoader) proto.getDataLoaderClass().newInstance();
                dl.load(proto, data);
            }
            return (T) data;
            
        } catch (Exception ex) {
            throw new LuoYingException("Could not createData for dataId=" + id, ex);
        }
    }
    
    /**
     * 获取一个处理器
     * @param <T>
     * @param data
     * @return 
     * @throws LuoYingException 如果无法为data创建Processor
     */
    @SuppressWarnings("UseSpecificCatch")
    public static <T extends DataProcessor> T createProcessor(ObjectData data) {
        if (data == null) {
            LOG.log(Level.WARNING, "Data could not be null");
            return null;
        }
        
        Class<? extends DataProcessor> dpClass = data.getProto().getDataProcessorClass();
        if (NullProcessor.class == dpClass) {
            throw new LuoYingException("Could not find data processor to createProcessor"
                    + ", tagName=" + data.getProto().getTagName() 
                    + ", dataId=" + data.getId());
        }
        try {
            DataProcessor dp =dpClass.newInstance();
            dp.setData(data);
            return (T) dp;
        } catch (Exception e) {
            throw new LuoYingException("Could not createProcessor for dataId=" + data.getId(), e);
        }
    }
    
    /**
     * 通用objectId来获得Data类型
     * @param id
     * @return 
     */
    public static Class<? extends ObjectData> getDataClassById(String id) {
        Proto proto = getProto(id);
        return getDataClass(proto.getTagName());
    }
    
    /**
     * 通过xml标记(tagName)获取已经注册的数据容器。
     * @param tagName
     * @return 
     */
    public static Class<? extends ObjectData> getDataClass(String tagName) {
        return TAG_DATAS.get(tagName);
    }
    
    /**
     * 通过xml标记(tagName)获取已经注册的数据载入器
     * @param tagName
     * @return 
     */
    public static Class<? extends DataLoader> getDataLoaderClass(String tagName) {
        return TAG_LOADERS.get(tagName);
    }
    
    /**
     * 通过xml标记(tagName)获取已经注册的数据处理器
     * @param tagName
     * @return 
     */
    public static Class<? extends DataProcessor> getDataProcessorClass(String tagName) {
        return TAG_PROCESSORS.get(tagName);
    }
    
    /**
     * 从数据流中载入数据
     * @param inputStream
     * @param encoding 如果为null则默认使用"utf-8"
     * @throws name.huliqing.luoying.LuoYingException
     * @see #loadDataFile(java.lang.String) 
     */
    public static void loadData(InputStream inputStream, String encoding) throws LuoYingException {
        try {
            List<Proto> protos = DATA_STORE_LOADER.loadData(inputStream, encoding);
            if (protos != null && !protos.isEmpty()) {
                DATA_STORE.addProtos(protos);
            }
        } catch (ParserConfigurationException ex) {
            throw new LuoYingException(ex);
        } catch (SAXException ex) {
            throw new LuoYingException(ex);
        } catch (IOException ex) {
            throw new LuoYingException(ex);
        }
    }
    
    public static void loadData(String xmlStr) throws LuoYingException {
        try {
            List<Proto> protos = DATA_STORE_LOADER.loadData(xmlStr);
            if (protos != null && !protos.isEmpty()) {
                DATA_STORE.addProtos(protos);
            }
        } catch (ParserConfigurationException ex) {
            throw new LuoYingException(ex);
        } catch (SAXException ex) {
            throw new LuoYingException(ex);
        } catch (IOException ex) {
            throw new LuoYingException(ex);
        }
    }
    
    public static Proto createProtoByExtends(String newId, String extProtoId) {
        return DATA_STORE.createProtoByExtends(newId, extProtoId);
    }
    
    public static void addProto(Proto proto) {
        DATA_STORE.addProto(proto);
    }
    
    /**
     * 通过ID获取原形
     * @param id
     * @return 
     */
    public static Proto getProto(String id) {
        return ProtoUtils.getProto(DATA_STORE, id);
    }
    
    /**
     * 清理掉所有数据及配置.
     * 注：一般这个方法只有在需要重新初始化系统的时候才应该使用。
     */
    public static void cleanup() {
        TAG_DATAS.clear();
        TAG_LOADERS.clear();
        TAG_PROCESSORS.clear();
        DATA_STORE.clearData();
    }
    
    /**
     * 生成一个唯一id, 每次调用该方法都将产生一个唯一的id.
     * @return 
     */
    public static long generateUniqueId() {
        return idGenerator.generateUniqueId();
    }
    
    /**
     * 添加数据类型定义，这个方法允许在运行时向系统添加自定义的数据类型。 注：如果指定的ID已经存在，则数据将会被覆盖。
     * @param tagName
     * @param id 
     * @param dataClass
     * @param dataLoaderClass
     * @param dataProcessorClass
     */
    public static void addCustomDataDefine(String tagName, String id, Class dataClass, Class dataLoaderClass
            , Class dataProcessorClass) {
        DATA_STORE.addCustomProto(tagName, id, dataClass, dataLoaderClass, dataProcessorClass);
    }
    
}
