/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.enums;

import name.huliqing.fighter.manager.ResourceManager;

/**
 * 定义人物身体的各个部分.
 * @author huliqing
 */
public enum SkinPart {
    ear(0), eye(1), face(3), foot(4), hair(5), hand(6), lowerBody(7), upperBody(8);
    
    private int value;
    private SkinPart(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public String getDesName() {
        return ResourceManager.get("skin.part" + value);
    }
    
    /**
     * 通过value的字符串形式来识别SkinPart。
     * ear="0", eye="1", face="3", foot="4", hair="5", hand="6", lowerBody="7", upperBody="8";
     * @param value
     * @return 
     */
    public static SkinPart identify(String value) {
       return identify(Integer.parseInt(value));
    }
    
    /**
     * 通过value的字符串形式来识别SkinPart。
     * ear=0, eye=1, face=3, foot=4, hair=5, hand=6, lowerBody=7, upperBody=8;
     * @param value
     * @return 
     */
    public static SkinPart identify(int value) {
       SkinPart[] sps = SkinPart.values();
       for (SkinPart sp : sps) {
           if (sp.getValue() == value) {
               return sp;
           }
       }
       throw new UnsupportedOperationException("不支持的SkinPart类型:" + value);
    }
    
}
