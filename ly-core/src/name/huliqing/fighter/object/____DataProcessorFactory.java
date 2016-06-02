///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object;
//
//import com.jme3.network.serializing.Serializer;
//import java.util.HashMap;
//import java.util.Map;
//import name.huliqing.fighter.data.DataLoaderFactory;
//import name.huliqing.fighter.data.Proto;
//import name.huliqing.fighter.data.ProtoData;
//import name.huliqing.fighter.object.scene.RandomSceneData;
//import name.huliqing.fighter.object.scene.RandomScene;
//
///**
// *
// * @author huliqing
// */
//public class DataProcessorFactory {
//    
//    // TagName -> DataProcessor class
//    private final static Map<String, Class<? extends DataProcessor>> 
//            processorMap = new HashMap<String, Class<? extends DataProcessor>>();
//    
//    // TagName -> ProtoData class
//    private final static Map<String, Class<? extends ProtoData>>
//            protoDataMap = new HashMap<String, Class<? extends ProtoData>>();
//    
//    static {
//        registerProcessor("sceneRandom", RandomScene.class);
//    }
//    
//    /**
//     * 注册一个处理器
//     * @param tagName
//     * @param cp 
//     */
//    public static void registerProcessor(String tagName, Class<? extends DataProcessor> cp) {
//        try {
//            // 注册处理器
//            processorMap.put(tagName, cp);
//            // 注册Data
//            DataProcessor dp = cp.newInstance();
//            protoDataMap.put(tagName, dp.getDataType());
//            
//            // 有bug,不能这样注册
////            Serializer.registerClass(dp.getDataType());
//        } catch (Exception e) {
//            throw new RuntimeException("Could not register DataProcessor"
//                    + ", tagName=" + tagName + ", cp=" + cp + ", error=" + e.getMessage());
//        }
//    }
//    
//    public static void registerSerializer(Class clz) {
//        Serializer.registerClass(clz);
//    }
//    
//    public static ProtoData createData(String id) {
//        Proto proto = DataLoaderFactory.getProto(id);
//        if (proto == null) {
//            throw new NullPointerException("Could not find object, id=" + id);
//        }
//        
//        Class<? extends ProtoData> pdClass = protoDataMap.get(proto.getTagName());
//        if (pdClass == null) {
//            throw new NullPointerException("Could not find ProtoData, id=" + id + ", tagName=" + proto.getTagName());
//        }
//        
//        try {
//            ProtoData data = (ProtoData) pdClass.newInstance();
//            // 载入数据
//            data.loadData(proto);
//            return data;
//        } catch (Exception ex) {
//            throw new RuntimeException("Could not createData"
//                    + ", id=" + id 
//                    + ", tagName=" + proto.getTagName()
//                    + ", error=" + ex.getMessage());
//        }
//    }
//    
//    public static DataProcessor createProcessor(ProtoData data) {
//        String tagName = data.getTagName();
//        Class<? extends DataProcessor> dpClass = processorMap.get(tagName);
//        if (dpClass == null) {
//            throw new NullPointerException("Could not find processor, tagName=" + tagName + ", dataId=" + data.getId());
//        }
//        try {
//            DataProcessor dp = dpClass.newInstance();
//            dp.setData(data);
//            return dp;
//        } catch (Exception ex) {
//            throw new RuntimeException("Could not create processor! tagName=" + tagName 
//                    + ", processorClass=" + dpClass.getName()
//                    + ", error=" + ex.getMessage()
//                    + ", dataId=" + data.getId());
//        }
//    }
//}
