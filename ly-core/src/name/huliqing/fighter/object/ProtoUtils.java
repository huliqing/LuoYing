/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.loader.data.ObjectLoader;

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
     * B继承自A, 则当查询一次C之后，所有参数都继续完毕（包含ABC)，ABC的继承
     * 关系也将不再存在，下次查找C时，将不再需要重新去处理继承关系,因为Proto
     * 是通用所有对象的,只要处理一次即可。
     * @param p
     * @param pp
     * @return 
     */
    private static Proto extendsProto(Proto p, Proto pp, List<String> checker) {
        if (Config.debug) {
            LOG.log(Level.INFO, "====processor extends: {0} extends {1}", new Object[] {p, pp});
        }
        if (pp != null && p != pp) { // p != pp 防止无用的自继承
            
            // 限制不同DataType类型的继承，以避免复杂性,以防止死继承
            if (p.getDataType() != pp.getDataType()) {
                throw new UnsupportedOperationException("Unsupported difference "
                        + "DataType extends! proto={0}" + p + ", parentProto=" + pp);
            }
            
            checker.add(pp.getId());
            String extId = pp.getAttribute(ATTRIBUTE_EXTENDS);
            if (extId != null) {
                // 检查是否存在无尽继承
                if (checker.contains(extId)) {
                    throw new UnsupportedOperationException("Unsupported endless loop extends => " 
                            + checker + ", extId=" + extId);
                }
                
                // 检查被继承的对象是否存在
                Proto extProto = ObjectLoader.findObjectDef(extId);
                if (extProto == null) {
                    throw new RuntimeException("Could not find extends object=" + extId + ", extends=" + checker);
                }
                pp = extendsProto(pp, extProto, checker);
            }
            Map<String, Object> pMap = p.getOriginAttributes();
            Map<String, Object> ppMap = pp.getOriginAttributes();
            Map<String, Object> merger = new HashMap<String, Object>(ppMap.size());
            merger.putAll(ppMap);
            merger.putAll(pMap);
            pMap.clear();
            pMap.putAll(merger);

            // 移除掉这个参数，这样下次就不需要让父类再去递归继承了，以节省性能。
            pMap.remove(ATTRIBUTE_EXTENDS);
            
            if (Config.debug) {
                LOG.log(Level.INFO, "processor extends result => {0}", p);
            }
            return p;
        }
        // 移除掉这个参数，这样下次就不需要再去继承，以节省性能。
        p.getOriginAttributes().remove(ATTRIBUTE_EXTENDS);

        return p;
    }
}
