/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.attribute;

/**
 *
 * @author huliqing
 */
public class NumberCompare {
    
    public static boolean isEqualTo(int value1, NumberAttribute na) {
        if (na instanceof IntegerAttribute) {
            return value1 == ((IntegerAttribute) na).intValue();
        }
        if (na instanceof FloatAttribute) {
            return value1 == ((FloatAttribute)na).floatValue();
        }
        return false;
    }
    
    public static boolean isEqualTo(float value1, NumberAttribute na) {
        if (na instanceof IntegerAttribute) {
            return value1 == ((IntegerAttribute) na).intValue();
        }
        if (na instanceof FloatAttribute) {
            return value1 == ((FloatAttribute)na).floatValue();
        }
        return false;
    }
}
