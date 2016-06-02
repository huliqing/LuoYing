/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.data;

import com.jme3.network.serializing.Serializable;
import java.util.Map;
import name.huliqing.fighter.enums.Mat;
import name.huliqing.fighter.enums.DataType;

/**
 * 物品的原型数据定义,所有ID相同的物品都引用同一个原形proto.
 * @author huliqing
 */
@Serializable
public class Proto extends DataAttribute{ 
    
    private String tagName;
    private String id;
    private DataType type;
    
    /**
     * Only for Serializable
     */
    public Proto() {}
    
    public Proto(DataType type, Map<String, String> attributes, String tagName) {
        super(attributes);
        this.type = type;
        this.tagName = tagName;
    }
    
    /**
     * 检查某个参数中有多少个项，每个项以半角逗号分隔,如果参数不存在或没有值
     * ，则返回0
     * @param key
     * @return 
     */
    public int checkAttributeLength(String key) {
        String value = getAttribute(key);
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        return value.split(",").length;
    }
    
    public DataType getDataType() {
        return type;
    }
    
    /**
     * 获取物品ID， 该方法不应该返回null,任何物品类型都应有一个唯一ID。
     * 如果忘记了设置物品的ID，则该方法将返回-1
     * @return 
     */
    public String getId() {
        if (id == null) {
            id = getAttribute("id");
        }
        return id;
    }
    
    public String getName() {
        return getAttribute("name");
    }
    
    public String getDes() {
        return getAttribute("des");
    }
    
    /**
     * 获取物品的图标,任何物品都应该有一个图标,该值可能返回null,则系统将使用一个
     * 默认图标代替。
     * @return 
     */
    public String getIcon() {
        return getAttribute("icon");
    }
    
    /**
     * 获取物品的模型路径，某些物品可能没有模型，则可能返回null.
     * @return 
     */
    public String getFile() {
        return getAttribute("file");
    }
    
    /**
     * 获取物品的材质（mat)，如果没有设置则返回Mat.none.该材质信息目前主要
     * 用于计算物体碰撞声音。
     * @return 
     */
    public Mat getMat() {
        int matInt = getAsInteger("mat", Mat.none.getValue());
        return Mat.identify(matInt);
    }

    public String getTagName() {
        return tagName;
    }
    
    public String getUseHandler() {
        return getAttribute("useHandler", "empty");
    }

    /**
     * 获取所有原始参数，该方法只允许DataFactory内部调用.
     * @return 
     */
    public Map<String, String> getOriginAttributes() {
        return data;
    }

    @Override
    public String toString() {
        return "Proto{tagName=" + tagName + ", type=" + type + ", attributes=" + data  + '}';
    }

    
}
