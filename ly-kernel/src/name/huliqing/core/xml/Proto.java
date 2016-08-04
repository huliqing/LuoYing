/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.xml;

import com.jme3.network.serializing.Serializable;
import java.util.Map;

/**
 * 物品的原型数据定义,所有ID相同的物品都引用同一个原形proto.
 * @author huliqing
 */
@Serializable
public class Proto extends Data { 
    
    private String tagName;
    private String id;
    
    /**
     * Only for Serializable
     */
    public Proto() {}
    
    public Proto(Map<String, String> attributes, String tagName) {
        super(attributes);
        this.tagName = tagName;
        this.id = attributes.get("id");
    } 

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
    public String getDataClass() {
        return getAttribute("dataClass");
    }
    
    /**
     * 数据容器类型的class全限定类名，如: "name.huliqing.fighter.data.SceneData",数据类型必须是"ProtoData".
     * @param dataClass 
     */
    public void setDataClass(String dataClass) {
        setAttribute("dataClass", dataClass);
    }
    
    /**
     * 获取用于载入数据的“载入器”的class全限定类名，如“name.huliqing.fighter.object.scene.SceneLoader”,
     * 数据类必须是“DataLoader”,如果没有指定，则该方法返回null.
     * @return 
     */
    public String getDataLoaderClass() {
        return getAttribute("dataLoaderClass");
    }
    
    /**
     * 设置用于载入数据的“载入器”的class全限定类名，如“name.huliqing.fighter.object.scene.SceneLoader”,
     * 数据类必须是“DataLoader”
     * @param dataLoaderClass 
     */
    public void setDataLoaderClass(String dataLoaderClass) {
        setAttribute("dataLoaderClass", dataLoaderClass);
    }
    
    /**
     * 获取用于处理数据的“处理器”的class全限定类名，如“name.huliqing.fighter.object.scene.Scene", 数据类型必须是
     * DataProcessor,如果没有指定，则该方法将返回null.
     * @return 
     */
    public String getDataProcessorClass() {
        return getAttribute("dataProcessorClass");
    }
    
    /**
     * 设置用于处理数据的“处理器”的class全限定类名，如“name.huliqing.fighter.object.scene.Scene", 数据类型必须是
     * DataProcessor。
     * @param dataProcessorClass
     */
    public void setDataProcessorClass(String dataProcessorClass) {
        setAttribute("dataProcessorClass", dataProcessorClass);
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
