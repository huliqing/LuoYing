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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 物体原形工具，Proto是有继承关系的, Proto的继承是一个线性的链，通过ProtoUtils来获取完整的Proto。
 * @author huliqing
 */
class ProtoUtils {
    
    private static final Logger LOG = Logger.getLogger(ProtoUtils.class.getName());
    
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

        boolean extendsResolved = proto.getAsBoolean(Proto.ATTRIBUTE_EXTENDS_RESOLVED, false);
        if (extendsResolved) {
            // 如果已经处理完成了继承链或者extendsId不存在，则不需要再重复处理继承关系。
            // 这个时候dataLoader和dataProcessor必须已经存在（通过自身tag配置或是继承自父tag）
            // ，如果这个时候不存在，则需要确定一个 .
            checkLoaderAndProcessor(proto);
            return proto;
        }
        
        String extendsId = proto.getAsString(Proto.ATTRIBUTE_EXTENDS);
        if (extendsId == null) {
            checkLoaderAndProcessor(proto);
            proto.setAttribute(Proto.ATTRIBUTE_EXTENDS_RESOLVED, true);
            return proto;
        }
        
        // 处理Proto的继承链。
        // checker用于记录并检查“继承链”是否存在循环继承问题。
        List<String> checker = new ArrayList<String>();
        checker.add(proto.getId());
        proto = extendsProto(dataStore, proto, dataStore.getProto(extendsId), checker);
        
        return proto;
        
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

        // 继承方式是这样的：从父类向下逐层继承
        boolean extendsResolved = parent.getAsBoolean(Proto.ATTRIBUTE_EXTENDS_RESOLVED, false);
        if (!extendsResolved) {
            String extendsId=  parent.getAsString(Proto.ATTRIBUTE_EXTENDS);
            if (extendsId != null) {
                // 检查是否存在无尽继承
                checker.add(parent.getId());
                if (checker.contains(extendsId)) {
                    throw new UnsupportedOperationException("Unsupported endless loop extends => " + checker + ", extId=" + extendsId);
                }
                // 检查被继承的对象是否存在
                Proto extProto = dataStore.getProto(extendsId);
                if (extProto == null) {
                    throw new RuntimeException("Could not find extends object, extendId=" + extendsId + ", extends=" + checker);
                }
                parent = extendsProto(dataStore, parent, extProto, checker);
            }
            // 标记继承关系已经处理完毕，这样下次就不需要再去处理继承链了，以节省性能。
            parent.setAttribute(Proto.ATTRIBUTE_EXTENDS_RESOLVED, true);
        }
        
        // 继承父类参数,注：这里会把父类的ATTRIBUTE_EXTENDS_RESOLVED属性也继承了，但是不会有关系。因子proto也会同
        // 时设置一样的值。
        Map<String, Object> merger = new HashMap<String, Object>();
        merger.putAll(parent.getOriginAttributes());
        merger.putAll(proto.getOriginAttributes());
        
        Map<String, Object> protoMap = proto.getOriginAttributes();
        protoMap.clear();
        protoMap.putAll(merger);
        // 这里可以不需要再设置这个参数，因为merger中已经继承了parent的ATTRIBUTE_EXTENDS_RESOLVED设置。
        // 这里只是明确的标记一下。
        proto.setAttribute(Proto.ATTRIBUTE_EXTENDS_RESOLVED, true);
        
        // 检查dataLoader和dataProcessor,如果没有的话则应该动态确定一个。
        checkLoaderAndProcessor(proto);
        
        LOG.log(Level.INFO, "processor extends result => {0}", proto);
        return proto;
    }
    
    private static void checkLoaderAndProcessor(Proto proto) {
        String tagName = proto.getTagName();
        if (proto.getDataClass() == null) {
            Class dataClass = null;
            String dataClassStr = proto.getAsString(Proto.DATA_CLASS);
            if (dataClassStr != null) {
                try {
                    dataClass = Class.forName(dataClassStr);
                } catch (ClassNotFoundException ex) {
                    LOG.log(Level.SEVERE, "protoId=" + proto.getId() + ", tagName=" + proto.getTagName(), ex);
                }
            }
            if (dataClass == null) {
                dataClass = DataFactory.getDataClass(tagName);
            }
            proto.setDataClass(dataClass != null ? dataClass : ObjectData.class);
        }
        // 优先从Proto参数中查找自定义的载入器，如果没有则从配置中查找，
        // 如果再找不到DataLoader，则最后给一个NullLoader标记
        if (proto.getDataLoaderClass() == null) {
            Class dataLoaderClass = null;
            String dataLoaderClassStr = proto.getAsString(Proto.DATA_LOADER_CLASS);
            if (dataLoaderClassStr != null) {
                try {
                    dataLoaderClass = Class.forName(dataLoaderClassStr);
                } catch (ClassNotFoundException ex) {
                    LOG.log(Level.SEVERE, "protoId=" + proto.getId() + ", tagName=" + proto.getTagName(), ex);
                }
            }
            if (dataLoaderClass == null) {
                dataLoaderClass = DataFactory.getDataLoaderClass(tagName);
            }
            proto.setDataLoaderClass(dataLoaderClass != null ? dataLoaderClass : NullLoader.class);
        }
        // 如果找不到DataProcessor，则最后给一个NullProcessor标记
        if (proto.getDataProcessorClass() == null) {
            Class dataProcessorClass = null;
            String dataProcessorClassStr = proto.getAsString(Proto.DATA_PROCESSOR_CLASS);
            if (dataProcessorClassStr != null) {
                try {
                    dataProcessorClass = Class.forName(dataProcessorClassStr);
                } catch (ClassNotFoundException ex) {
                    LOG.log(Level.SEVERE, "protoId=" + proto.getId() + ", tagName=" + proto.getTagName(), ex);
                }
            }
            if (dataProcessorClass == null) {
                dataProcessorClass = DataFactory.getDataProcessorClass(tagName);
            }
            proto.setDataProcessorClass(dataProcessorClass != null ? dataProcessorClass : NullProcessor.class);
        }
    }
   
}
