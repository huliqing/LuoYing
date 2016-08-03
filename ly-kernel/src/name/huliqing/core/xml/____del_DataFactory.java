///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.xml;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// *
// * @author huliqing
// */
//public class DataFactory {
//
//    private static final Logger LOG = Logger.getLogger(DataFactory.class.getName());
//    
//    // TagName -> ProtoData,用于注册自定义的数据容器
//    private final static Map<String, Class<? extends ProtoData>> TAG_DATAS = new HashMap<String, Class<? extends ProtoData>>();
//    
//    // TagName -> DataLoader, 自定义数据载入器
//    private final static Map<String, Class<? extends DataLoader>> TAG_LOADERS = new HashMap<String, Class<? extends DataLoader>>();
//    
//    // TagName -> DataProcessor, 自定义数据处理器
//    private final static Map<String, Class<? extends DataProcessor>> TAG_PROCESSORS = new HashMap<String, Class<? extends DataProcessor>>();
//    
//    /**
//     * 注册一个数据类型
//     * @param tagName 
//     * @param dataTypeClass 
//     */
//    public static void registerData(String tagName, Class<? extends ProtoData> dataTypeClass) {
//        TAG_DATAS.put(tagName, dataTypeClass);
//        LOG.log(Level.INFO, "Serializer registerClass={0}", dataTypeClass);
//    }
//    
//    /**
//     * 注册一个数据载入器,如果dataLoaderClass为null,则清除已有的载入器.
//     * @param tagName
//     * @param dataLoaderClass 
//     */
//    public static void registerLoader(String tagName, Class<? extends DataLoader> dataLoaderClass) {
//        if (dataLoaderClass == null) {
//            TAG_LOADERS.remove(tagName);
//            return;
//        }
//        TAG_LOADERS.put(tagName, dataLoaderClass);
//    }
//    
//    /** 
//     * 注册一个数据处理器
//     * @param tagName 
//     * @param dataProcessorClass 
//     */
//    public static void registerProcessor(String tagName, Class<? extends DataProcessor> dataProcessorClass) {
//        if (dataProcessorClass == null) {
//            TAG_PROCESSORS.remove(tagName);
//            return;
//        }
//        TAG_PROCESSORS.put(tagName, dataProcessorClass);
//    }
//    
//    /**
//     * 注册数据类型、数据载入器及数据处理器
//     * @param tagName
//     * @param dataClass
//     * @param dataLoaderClass
//     * @param dataProcessorClass 
//     */
//    public static void register(String tagName
//            , Class<? extends ProtoData> dataClass
//            , Class<? extends DataLoader> dataLoaderClass
//            , Class<? extends DataProcessor> dataProcessorClass
//            ) {
//        registerData(tagName, dataClass);
//        registerLoader(tagName, dataLoaderClass);
//        registerProcessor(tagName, dataProcessorClass);
//    }
//    
//    /**
//     * 通过ID来创建并载入Data
//     * @param <T>
//     * @param id
//     * @return 
//     */
//    public static <T extends ProtoData> T createData(String id) {
//        Proto proto = ProtoUtils.getProto(id);
//        if (proto == null) {
//            throw new NullPointerException("Could not find object, id=" + id);
//        }
//        
//        try {
//            String dataClass = proto.getDataClass();
//            if (dataClass == null) {
//                throw new NullPointerException("No \"dataClass\"  set for proto, id=" + id + ", proto=" + proto);
//            }
//            ProtoData protoData = (ProtoData) Class.forName(dataClass).newInstance();
//            
//            String dataLoader = proto.getLoaderClass();
//            if (dataLoader == null) {
//                throw new NullPointerException("No \"dataLoader\" set for proto, id=" + id + ", proto=" + proto);
//            }
//            DataLoader dl = (DataLoader) Class.forName(dataLoader).newInstance();
//            protoData.setId(id);
//            dl.load(proto, protoData);
//            return (T) protoData;
//            
//        } catch (NullPointerException ex) {
//            LOG.log(Level.SEVERE, "Could not createData by id={0}, error={1}",  new Object[]{id, ex.getMessage()});
//        } catch (ClassNotFoundException ex) {
//            LOG.log(Level.SEVERE, "Could not createData by id={0}, error={1}",  new Object[]{id, ex.getMessage()});
//        } catch (InstantiationException ex) {
//            LOG.log(Level.SEVERE, "Could not createData by id={0}, error={1}",  new Object[]{id, ex.getMessage()});
//        } catch (IllegalAccessException ex) {
//            LOG.log(Level.SEVERE, "Could not createData by id={0}, error={1}",  new Object[]{id, ex.getMessage()});
//        }
//        return null;
//    }
//    
//    /**
//     * 获取一个处理器
//     * @param <T>
//     * @param data
//     * @return 
//     */
//    public static <T extends DataProcessor> T createProcessor(ProtoData data) {
//        if (data == null) {
//            LOG.log(Level.WARNING, "Data could not be null");
//            return null;
//        }
//        Class<? extends DataProcessor> dpClass = TAG_PROCESSORS.get(data.getTagName());
//        if (dpClass == null) {
//            throw new NullPointerException("Could not find data processor to createProcessor"
//                    + ", tagName=" + data.getTagName() 
//                    + ", dataId=" + data.getId());
//        }
//        try {
//            DataProcessor dp = dpClass.newInstance();
//            dp.setData(data);
//            return (T) dp;
//        } catch (InstantiationException ex) {
//            throw new RuntimeException("Could not create processor! tagName=" + data.getTagName() 
//                    + ", dataId=" + data.getId()
//                    + ", dataProcessor=" + dpClass.getName()
//                    + ", error=" + ex.getMessage()
//                    );
//        } catch (IllegalAccessException ex) {
//            throw new RuntimeException("Could not create processor! tagName=" + data.getTagName()
//                    + ", dataId=" + data.getId()
//                    + ", dataProcessor=" + dpClass.getName()
//                    + ", error=" + ex.getMessage()
//            );
//        }
//    }
//    
//    /**
//     * Get data class by tagName
//     * @param tagName
//     * @return 
//     */
//    public static Class<? extends ProtoData> getDataClass(String tagName) {
//        return TAG_DATAS.get(tagName);
//    }
//    
//    /**
//     * Get data loader class by tagName
//     * @param tagName
//     * @return 
//     */
//    public static Class<? extends DataLoader> getDataLoaderClass(String tagName) {
//        return TAG_LOADERS.get(tagName);
//    }
//    
//    /**
//     * Get data processor class by tagName.
//     * @param tagName
//     * @return 
//     */
//    public static Class<? extends DataProcessor> getDataProcessorClass(String tagName) {
//        return TAG_PROCESSORS.get(tagName);
//    }
//}
