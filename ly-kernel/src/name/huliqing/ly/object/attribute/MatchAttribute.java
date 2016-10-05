/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.attribute;

/**
 * @author huliqing
 */
public interface MatchAttribute {
    
    /**
     * 判断当前属性值是否与指定目标other匹配
     * @param other
     * @return 
     */
    boolean match(Object other);
}
