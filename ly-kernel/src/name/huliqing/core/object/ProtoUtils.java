/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Config;
import name.huliqing.core.xml.Proto;
import name.huliqing.core.enums.DataType;
import name.huliqing.core.loader.ObjectLoader;

/**
 *
 * @author huliqing
 */
public class ProtoUtils {
    private static final Logger LOG = Logger.getLogger(ProtoUtils.class.getName());
    
    private final static String ATTRIBUTE_EXTENDS = "extends";
    
    /**
     * 获取object原形
     * @param objectId
     * @return 
     */
    public synchronized static Proto getProto(String objectId) {
        Proto proto = ObjectLoader.findObjectDef(objectId);
        if (proto == null) {
            LOG.log(Level.WARNING, "Could not find object={0}", objectId);
            return null;
        }
        
        String extId = proto.getAttribute(ATTRIBUTE_EXTENDS);
        if (extId == null) {
            // 如果extId不存在，则说明该Proto没有继承关系，或者说继承关系已经处理过了。
            // 这个时候dataLoader和dataProcessor必须已经存在（通过自身tag配置或是继承自父tag），如果这个时候不存在，则
            // 需要则需要确定一个 
            checkLoaderAndProcessor(proto);
            return proto;
        } else {
            // checker用于记录“继承链”
            List<String> checker = new ArrayList<String>(3);
            checker.add(proto.getId());
            proto = extendsProto(proto, ObjectLoader.findObjectDef(extId), checker);
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
    private static Proto extendsProto(Proto proto, Proto parent, List<String> checker) {
        if (Config.debug) {
            LOG.log(Level.INFO, "====processor extends: {0} extends {1}", new Object[] {proto, parent});
        }
        
        // 防止自继承
        if (proto == parent) {
            throw new IllegalStateException("Proto could not extends self! proto=" + proto + ", extends parent=" + parent);
        }
            
        // 限制不同DataType类型的继承，以避免复杂性,以防止死继承
        if (proto.getDataType() != parent.getDataType()) {
            throw new UnsupportedOperationException("Unsupported difference DataType extends! proto={0}" + proto + ", parentProto=" + parent);
        }

        // 继承方式是这样的：从父类向下逐层继承
        String extId = parent.getAttribute(ATTRIBUTE_EXTENDS);
        if (extId != null) {
            // 检查是否存在无尽继承
            checker.add(parent.getId());
            if (checker.contains(extId)) {
                throw new UnsupportedOperationException("Unsupported endless loop extends => " + checker + ", extId=" + extId);
            }

            // 检查被继承的对象是否存在
            Proto extProto = ObjectLoader.findObjectDef(extId);
            if (extProto == null) {
                throw new RuntimeException("Could not find extends object=" + extId + ", extends=" + checker);
            }
            parent = extendsProto(parent, extProto, checker);
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
        
        if (Config.debug) {
            LOG.log(Level.INFO, "processor extends result => {0}", proto);
        }
        return proto;
        
    }
    
    private static void checkLoaderAndProcessor(Proto proto) {
        String tagName = proto.getTagName();
        int dataType = proto.getDataType();
        if (proto.getDataClass() == null) {
            Class dataClass = DataFactory.findProtoData(tagName, dataType);
            proto.setDataClass(dataClass != null ? dataClass.getName() : null);
        }
        if (proto.getDataLoader() == null) {
            Class dataLoaderClass = DataFactory.findDataLoader(tagName, dataType);
            proto.setDataLoader(dataLoaderClass != null ? dataLoaderClass.getName() : null);
        }
        if (proto.getDataProcessor() == null) {
            Class dataProcessorClass = DataFactory.findDataProcessor(tagName, dataType);
            proto.setDataProcessor(dataProcessorClass != null ? dataProcessorClass.getName() : null);
        }
    }
    
    public static void main(String[] args) {
        ObjectLoader.initData();
        DataFactory.initRegister();
        System.out.println("==" + getProto("sceneExtendsA"));
        System.out.println("==" + getProto("sceneExtendsB"));
        System.out.println("==" + getProto("sceneExtendsC"));
    }
}
