///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.data;
//
//import com.jme3.math.ColorRGBA;
//import com.jme3.math.Quaternion;
//import com.jme3.math.Vector2f;
//import com.jme3.math.Vector3f;
//import com.jme3.network.serializing.Serializable;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import name.huliqing.fighter.enums.Mat;
//import name.huliqing.fighter.enums.DataType;
//import name.huliqing.fighter.utils.ConvertUtils;
//
///**
// * 物品的原型数据定义,所有ID相同的物品都引用同一个原形proto.
// * @author huliqing
// */
//@Serializable
//public class Proto extends DataMap{ 
//    
//    private String tagName;
//    private String id;
//    private DataType type;
////    private Map<String, String> attributes;
//    
//    /**
//     * Only for Serializable
//     */
//    public Proto() {}
//    
//    public Proto(DataType type, Map<String, String> attributes, String tagName) {
//        super(attributes);
//        this.type = type;
////        this.attributes = attributes;
//        this.tagName = tagName;
//    }
//    
////    public void putAttribute(String key, String value) {
////        attributes.put(key, value);
////    }
////    
////    /**
////     * 获取参数，如果不存在则返回null, 如果为空值也返回null
////     * @param key
////     * @return 
////     */
////    public String getAttribute(String key) {
////        if (attributes == null || attributes.isEmpty() || !attributes.containsKey(key)) {
////            return null;
////        }
////        String value = attributes.get(key);
////        if ("".equals(value)) {
////            return null;
////        }
////        return value;
////    }
////    
////    public String getAttribute(String key, String defValue) {
////        String value = getAttribute(key);
////        if (value == null) {
////            return defValue;
////        }
////        return value;
////    }
////    
////    public Integer getAsInteger(String key) {
////        String value = getAttribute(key);
////        if (value == null) {
////            return null;
////        }
////        return Integer.parseInt(value);
////    }
////    
////    public int getAsInteger(String key, int defValue) {
////        String value = getAttribute(key);
////        if (value == null) {
////            return defValue;
////        }
////        return Integer.parseInt(value);
////    }
////    
////    /**
////     * 把参数获取为整形数组，如果没有设置该参数则返回null.
////     * @param key
////     * @return 
////     */
////    public int[] getAsIntegerArray(String key) {
////        String[] strArr = getAsArray(key);
////        if (strArr != null) {
////            return ConvertUtils.toIntegerArray(strArr);
////        }
////        return null;
////    }
////    
////    /**
////     * 把参数获取为整形集合，如果没有设置该参数则返回null.
////     * @param key
////     * @return 
////     */
////    public List<Integer> getAsIntegerList(String key) {
////        String[] strArr = getAsArray(key);
////        if (strArr != null) {
////            return ConvertUtils.toIntegerList(strArr);
////        }
////        return null;
////    }
////    
////    public Float getAsFloat(String key) {
////        String value = getAttribute(key);
////        if (value == null) {
////            return null;
////        }
////        return Float.parseFloat(value);
////    }
////    
////    public float getAsFloat(String key, float defValue) {
////        String value = getAttribute(key);
////        if (value == null) {
////            return defValue;
////        }
////        return Float.parseFloat(value);
////    }
////    
////    public float[] getAsFloatArray(String key) {
////        String value = getAttribute(key);
////        if (value == null) {
////            return null;
////        }
////        String[] tempArr = value.split(",");
////        float[] resultArr = new float[tempArr.length];
////        for (int i = 0; i < resultArr.length; i++) {
////            resultArr[i] = Float.parseFloat(tempArr[i]);
////        }
////        return resultArr;
////    }
////    
////    public Boolean getAsBoolean(String key) {
////        String value = getAttribute(key);
////        if (value == null) {
////            return null;
////        }
////        return (value.equals("true") || value.equals("1"));
////    }
////    
////    public boolean getAsBoolean(String key, boolean defValue) {
////        String value = getAttribute(key);
////        if (value == null) {
////            return defValue;
////        }
////        return (value.equals("true") || value.equals("1"));
////    }
////    
////    /**
////     * 获取参数值，并以数组形式返回，原始参数值格式必须是使用半角逗号","分隔的,
////     * 如：属性值 "1,3,2" 将返回为数组 {"1","3","2"}.
////     * 如果属性值为null或空，则返回null.
////     * @param key
////     * @return 
////     */
////    public String[] getAsArray(String key) {
////        String temp = getAttribute(key);
////        if (temp == null) 
////            return null;
////        String[] result = temp.split(",");
////        for (int i = 0; i < result.length; i++) {
////            result[i] = result[i].trim();
////        }
////        return result;
////    }
////    
////    /**
////     * 获取参数值，并以List形式返回，原始参数值格式必须是使用半角逗号","分隔的,
////     * 如：属性值 "1,3,2", 如果属性值为null或空，则返回null.
////     * @param key
////     * @return 
////     */
////    public List<String> getAsList(String key) {
////        String[] arr = getAsArray(key);
////        if (arr == null)
////            return null;
////        return Arrays.asList(arr);
////    }
////    
////    /**
////     * 获取参数值，并以Vector3f形式返回，原始格式必须如： "x,y,z"，
////     * 如果参数不对，将返回null.
////     * @param key
////     * @return 
////     */
////    public Vector3f getAsVector3f(String key) {
////        String temp = getAttribute(key);
////        if (temp == null) 
////            return null;
////        String[] arr = temp.split(",");
////        if (arr.length < 3) {
////            return null;
////        }
////        Vector3f vec = new Vector3f();
////        vec.setX(Float.parseFloat(arr[0]));
////        vec.setY(Float.parseFloat(arr[1]));
////        vec.setZ(Float.parseFloat(arr[2]));
////        return vec;
////    }
////    
////    public Vector3f getAsVector3f(String key, Vector3f defValue) {
////        Vector3f temp = getAsVector3f(key);
////        return temp != null ? temp : defValue;
////    }
////    
////    /**
////     * 格式, "x|y|z,x|y|z,...", 如果没有指定参数则返回null.
////     * @param key
////     * @return 
////     */
////    public Vector3f[] getAsVector3fArray(String key) {
////        String temp = getAttribute(key);
////        if (temp == null) 
////            return null;
////        String[] arrStr = temp.split(",");
////        Vector3f[] result = new Vector3f[arrStr.length];
////        for (int i = 0; i < arrStr.length; i++) {
////            String[] xyz = arrStr[i].split("\\|");
////            Vector3f vec = new Vector3f();
////            vec.setX(Float.parseFloat(xyz[0]));
////            vec.setY(Float.parseFloat(xyz[1]));
////            vec.setZ(Float.parseFloat(xyz[2]));
////            result[i] = vec;
////        }
////        return result;
////    }
////    
////    public Vector2f getAsVector2f(String key) {
////        String temp = getAttribute(key);
////        if (temp == null) 
////            return null;
////        String[] arr = temp.split(",");
////        if (arr.length < 2) {
////            return null;
////        }
////        Vector2f vec = new Vector2f();
////        vec.setX(Float.parseFloat(arr[0]));
////        vec.setY(Float.parseFloat(arr[1]));
////        return vec;
////    }
////    
////    public Vector2f getAsVector2f(String key, Vector2f defValue) {
////        Vector2f temp = getAsVector2f(key);
////        return temp != null ? temp : defValue;
////    }
////    
////    /**
////     * 获取参数值，并以Vector4f形式返回，原始格式必须如： "x,y,z,w"，
////     * 如果参数不对，将返回null.
////     * @param key
////     * @return 
////     */
////    public Quaternion getAsQuaternion(String key) {
////        String temp = getAttribute(key);
////        if (temp == null) 
////            return null;
////        String[] arr = temp.split(",");
////        if (arr.length < 4) {
////            return null;
////        }
////        Quaternion qua = new Quaternion();
////        qua.set(Float.parseFloat(arr[0])
////                , Float.parseFloat(arr[1])
////                , Float.parseFloat(arr[2])
////                , Float.parseFloat(arr[3]));
////        return qua;
////    }
////    
////    public Quaternion getAsQuaternion(String key, Quaternion defValue) {
////        Quaternion temp = getAsQuaternion(key);
////        return temp != null ? temp : defValue;
////    }
////    
////    public ColorRGBA getAsColor(String key) {
////        String temp = getAttribute(key);
////        if (temp == null) 
////            return null;
////        String[] arr = temp.split(",");
////        ColorRGBA color = new ColorRGBA();
////        if (arr.length > 0) {
////            color.r = Float.parseFloat(arr[0]);
////        }
////        if (arr.length > 1) {
////            color.g = Float.parseFloat(arr[1]);
////        }
////        if (arr.length > 2) {
////            color.b = Float.parseFloat(arr[2]);
////        }
////        if (arr.length > 3) {
////            color.a = Float.parseFloat(arr[3]);
////        }
////        return color;
////    }
////    
////    public ColorRGBA getAsColor(String key, ColorRGBA defValue) {
////        ColorRGBA temp = getAsColor(key);
////        return temp != null ? temp : defValue;
////    }
//    
//    /**
//     * 检查某个参数中有多少个项，每个项以半角逗号分隔,如果参数不存在或没有值
//     * ，则返回0
//     * @param key
//     * @return 
//     */
//    public int checkAttributeLength(String key) {
//        String value = getAttribute(key);
//        if (value == null || value.trim().isEmpty()) {
//            return 0;
//        }
//        return value.split(",").length;
//    }
//    
//    public DataType getDataType() {
//        return type;
//    }
//    
//    /**
//     * 获取物品ID， 该方法不应该返回null,任何物品类型都应有一个唯一ID。
//     * 如果忘记了设置物品的ID，则该方法将返回-1
//     * @return 
//     */
//    public String getId() {
//        if (id == null) {
//            id = getAttribute("id");
//        }
//        return id;
//    }
//    
//    public String getName() {
//        return getAttribute("name");
//    }
//    
//    public String getDes() {
//        return getAttribute("des");
//    }
//    
//    /**
//     * 获取物品的图标,任何物品都应该有一个图标,该值可能返回null,则系统将使用一个
//     * 默认图标代替。
//     * @return 
//     */
//    public String getIcon() {
//        return getAttribute("icon");
//    }
//    
//    /**
//     * 获取物品的模型路径，某些物品可能没有模型，则可能返回null.
//     * @return 
//     */
//    public String getFile() {
//        return getAttribute("file");
//    }
//    
//    /**
//     * 获取物品的材质（mat)，如果没有设置则返回Mat.none.该材质信息目前主要
//     * 用于计算物体碰撞声音。
//     * @return 
//     */
//    public Mat getMat() {
//        int matInt = getAsInteger("mat", Mat.none.getValue());
//        return Mat.identify(matInt);
//    }
//
//    public String getTagName() {
//        return tagName;
//    }
//    
//    public String getUseHandler() {
//        return getAttribute("useHandler", "empty");
//    }
//
//    /**
//     * 获取所有原始参数，注意：该方法只允许DataLoaderFactory调用
//     * @return 
//     */
//    Map<String, String> getOriginAttributes() {
//        return data;
//    }
//
//    @Override
//    public String toString() {
//        return "Proto{tagName=" + tagName + ", type=" + type + ", attributes=" + data  + '}';
//    }
//
//    
//}
