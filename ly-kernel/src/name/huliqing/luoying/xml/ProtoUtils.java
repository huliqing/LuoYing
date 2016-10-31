/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 用于获取拥有继承关系的物体原形。
 * @author huliqing
 */
class ProtoUtils {
    private static final Logger LOG = Logger.getLogger(ProtoUtils.class.getName());
    
    private final static String ATTRIBUTE_EXTENDS = "extends";
    
    /**
     * 获取object原形
     * @param dataStore
     * @param objectId
     * @return 
     */
    public synchronized static Proto getProto(DataStore dataStore, String objectId) {
        Proto proto = dataStore.getProto(objectId);
        if (proto == null) {
            LOG.log(Level.WARNING, "Could not find object={0}", objectId);
            return null;
        }
        
        String extendId = proto.getAsString(ATTRIBUTE_EXTENDS);
        if (extendId == null) {
            // 如果extId不存在，则说明该Proto没有继承关系，或者说继承关系已经处理过了。
            // 这个时候dataLoader和dataProcessor必须已经存在（通过自身tag配置或是继承自父tag），如果这个时候不存在，则
            // 需要则需要确定一个 
            checkLoaderAndProcessor(proto);
            return proto;
        } else {
            // checker用于记录“继承链”
            List<String> checker = new ArrayList<String>(3);
            checker.add(proto.getId());
            proto = extendsProto(dataStore, proto, dataStore.getProto(extendId), checker);
            return proto;
        }
    }
    
    /**
     * 处理Proto的继承，注：假设继承关系是这样的，A -> B -> C, 即C继承自B,
     * B继承自A, 则当查询一次C之后，所有参数都继承完毕（包含ABC)，ABC的继承
     * 关系也将不再存在，下次查找C时，将不再需要重新去处理继承关系,因为Proto
     * 是通用所有对象的,只要处理一次即可。
     * @param proto
     * @param parent
     * @return 
     */
    private static Proto extendsProto(DataStore dataStore, Proto proto, Proto parent, List<String> checker) {
        LOG.log(Level.INFO, "====processor extends: {0} extends {1}", new Object[] {proto, parent});
        
        // 防止自继承
        if (proto == parent) {
            throw new IllegalStateException("Proto could not extends self! proto=" + proto + ", extends parent=" + parent);
        }
            
        // remove201608xx, 以后不再有DataType.
//        // 限制不同DataType类型的继承，以减少复杂性,以防止死继承
//        if (proto.getDataType() != parent.getDataType()) {
//            throw new UnsupportedOperationException("Unsupported difference DataType extends! proto={0}" + proto + ", parentProto=" + parent);
//        }

        // 继承方式是这样的：从父类向下逐层继承
        String extendId=  parent.getAsString(ATTRIBUTE_EXTENDS);
        if (extendId != null) {
            // 检查是否存在无尽继承
            checker.add(parent.getId());
            if (checker.contains(extendId)) {
                throw new UnsupportedOperationException("Unsupported endless loop extends => " + checker + ", extId=" + extendId);
            }

            // 检查被继承的对象是否存在
            Proto extProto = dataStore.getProto(extendId);
            if (extProto == null) {
                throw new RuntimeException("Could not find extends object, extendId=" + extendId + ", extends=" + checker);
            }
            parent = extendsProto(dataStore, parent, extProto, checker);
        }
        
        // 继承父类参数
        Map<String, Object> merger = new HashMap<String, Object>();
        merger.putAll(parent.getOriginAttributes());
        merger.putAll(proto.getOriginAttributes());
        
        Map<String, Object> protoMap = proto.getOriginAttributes();
        protoMap.clear();
        protoMap.putAll(merger);
        
        // 移除掉这个参数，这样下次就不需要再去递归继承了，以节省性能。
        protoMap.remove(ATTRIBUTE_EXTENDS); 
        
        // 检查dataLoader和dataProcessor,如果没有的话则应该动态确定一个。
        checkLoaderAndProcessor(proto);
        
        LOG.log(Level.INFO, "processor extends result => {0}", proto);
        return proto;
        
    }
    
    private static void checkLoaderAndProcessor(Proto proto) {
        String tagName = proto.getTagName();
        if (proto.getDataClass() == null) {
            Class dataClass = DataFactory.getDataClass(tagName);
            proto.setDataClass(dataClass != null ? dataClass.getName() : null);
        }
        if (proto.getDataLoaderClass() == null) {
            Class dataLoaderClass = DataFactory.getDataLoaderClass(tagName);
            proto.setDataLoaderClass(dataLoaderClass != null ? dataLoaderClass.getName() : null);
        }
        if (proto.getDataProcessorClass() == null) {
            Class dataProcessorClass = DataFactory.getDataProcessorClass(tagName);
            proto.setDataProcessorClass(dataProcessorClass != null ? dataProcessorClass.getName() : null);
        }
    }
    
   
}
