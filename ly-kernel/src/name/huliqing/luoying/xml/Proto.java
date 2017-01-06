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

import com.jme3.network.serializing.Serializable;
import java.util.Map;

/**
 * 物品的原型数据定义,所有ID相同的物品都引用同一个原形proto.
 * @author huliqing
 */
@Serializable
public class Proto extends Data { 
    
    public final static String ATTRIBUTE_ID = "id";
    public final static String ATTRIBUTE_EXTENDS = "extends";
    public final static String DATA_CLASS = "dataClass";
    public final static String DATA_LOADER_CLASS = "dataLoaderClass";
    public final static String DATA_PROCESSOR_CLASS = "dataProcessorClass";
    
    private String tagName;
    private String id;
    
    // 注：由于这些class的存在，Proto可能无法正常序列化，但是Proto本身的设计也不是用来传输的,所以没有关系。
    private Class dataClass;
    private Class dataLoaderClass;
    private Class dataProcessorClass;
    
    public Proto() {}
    
    public Proto(String tagName, Map<String, String> attributes) {
        super(attributes);
        this.tagName = tagName;
        this.id = attributes.get(ATTRIBUTE_ID);
    }
//    public Proto(String tagName, String id, Map<String, String> attributes) {
//        super(attributes);
//        this.tagName = tagName;
//        this.id = id;
//    }
    
    public String getId() {
        return id;
    }
    
    public String getTagName() {
        return tagName;
    }
    
    /**
     * 获取数据容器类型的class全限定类名，如: "name.huliqing.fighter.data.SceneData",数据类型必须是"ProtoData".
     * 如果没有指定，则该方法返回null.
     * @return 
     */
    public Class getDataClass() {
        return dataClass;
    }
    
    /**
     * 数据容器类型的class全限定类名，如: "name.huliqing.fighter.data.SceneData",数据类型必须是"ProtoData".
     * @param dataClass 
     */
    public void setDataClass(Class dataClass) {
        this.dataClass = dataClass;
    }
    
    /**
     * 获取用于载入数据的“载入器”的class全限定类名，如“name.huliqing.fighter.object.scene.SceneLoader”,
     * 数据类必须是“DataLoader”,如果没有指定，则该方法返回null.
     * @return 
     */
    public Class getDataLoaderClass() {
        return this.dataLoaderClass;
    }
    
    /**
     * 设置用于载入数据的“载入器”的class全限定类名，如“name.huliqing.fighter.object.scene.SceneLoader”,
     * 数据类必须是“DataLoader”
     * @param dataLoaderClass 
     */
    public void setDataLoaderClass(Class dataLoaderClass) {
        this.dataLoaderClass = dataLoaderClass;
    }
    
    /**
     * 获取用于处理数据的“处理器”的class全限定类名，如“name.huliqing.fighter.object.scene.Scene", 数据类型必须是
     * DataProcessor,如果没有指定，则该方法将返回null.
     * @return 
     */
    public Class getDataProcessorClass() {
        return dataProcessorClass;
    }
    
    /**
     * 设置用于处理数据的“处理器”的class全限定类名，如“name.huliqing.fighter.object.scene.Scene", 数据类型必须是
     * DataProcessor。
     * @param dataProcessorClass
     */
    public void setDataProcessorClass(Class dataProcessorClass) {
        this.dataProcessorClass = dataProcessorClass;
    }

    /**
     * 获取所有原始参数，该方法只允许DataFactory内部调用.
     * @return 
     */
    public Map<String, Object> getOriginAttributes() {
        return data;
    }

    @Override
    public String toString() {
        return "Proto{" + "tagName=" + tagName + ", id=" + id + '}';
    }


}
