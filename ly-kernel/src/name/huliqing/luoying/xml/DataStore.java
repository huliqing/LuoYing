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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 数据容器，存储所有的最原始数据。
 * @author huliqing
 */
class DataStore {
    private final static Logger LOG = Logger.getLogger(DataStore.class.getName());
    
    private final static Map<String, Proto> PROTO_MAP = new HashMap<String, Proto>();
    
    public void addProto(Proto proto) {
        PROTO_MAP.put(proto.getId(), proto);
    }
    
    public void addProtos(List<Proto> protos) {
        for (Proto p : protos) {
            if (p == null)
                continue;
            addProto(p);
        }
    }
    
    public boolean removeProto(String protoId) {
        return PROTO_MAP.remove(protoId) != null;
    }
    
    /**
     * 添加自定义的数据类型, 注：如果指定的ID已经存在，则数据将会被覆盖。
     * @param tagName
     * @param id 
     * @param dataClass
     * @param dataLoaderClass
     * @param dataProcessorClass
     */
    public void addCustomProto(String tagName, String id
            , Class dataClass, Class dataLoaderClass, Class dataProcessorClass) {
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(Proto.ATTRIBUTE_ID, id);
        Proto proto = new Proto(tagName, attributes);
        if (dataClass != null) {
            proto.setDataClass(dataClass);
        }
        if (dataLoaderClass != null) {
            proto.setDataLoaderClass(dataLoaderClass);
        }
        if (dataProcessorClass != null) {
            proto.setDataProcessorClass(dataProcessorClass);
        }
        addProto(proto);
    }
    
    /**
     * 通过继承的方式动态创建一个新的Proto， 新创建的Proto的所有属性继承自父Proto
     * @param id protoId
     * @param extProtoId 父Proto的id
     * @return 
     */
    public Proto createProtoByExtends(String id, String extProtoId) {
        Proto parentProto = getProto(extProtoId);
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(Proto.ATTRIBUTE_ID, id);
        attributes.put(Proto.ATTRIBUTE_EXTENDS, parentProto.getId());
        Proto newProto = new Proto(parentProto.getTagName(), attributes);
        return newProto;
    }
    
    /**
     * 直接获取原形数据，这是最原始的数据，不会处理继承关系，作为内部方法调用，不要直接使用。外部调用应该使用
     * {@link DataFactory#getProto(java.lang.String) }
     * @param id
     * @return 
     */
    public Proto getProto(String id) {
        Proto result = PROTO_MAP.get(id);
        if (result != null) {
            return result;
        }
        LOG.log(Level.WARNING, "Unknow objectId={0}", id);
        return null;
    }
    
    /**
     * Get all proto data, this will return an unmodifiable collection.
     * @return 
     */
    public Collection<Proto> findAll() {
        return Collections.unmodifiableCollection(PROTO_MAP.values());
    }
    
    /**
     * 清理所有数据
     */
    public void clearData() {
        PROTO_MAP.clear();
    }
    
    

}
