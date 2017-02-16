/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.utils;

/**
 *
 * @author huliqing
 */
public class Validator {
    
    public final static boolean isFloat(Object value) {
        if (value instanceof Float) {
            return true;
        }
        if (value == null)
            return false;
        try {
            Float.parseFloat(value.toString());
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    
    public final static boolean isInteger(Object value) {
        if (value instanceof Integer) {
            return true;
        }
        if (value == null)
            return false;
        try {
            Integer.parseInt(value.toString());
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    
    public final static boolean isLong(Object value) {
        if (value instanceof Long) {
            return true;
        }
        if (value == null)
            return false;
        try {
            Long.parseLong(value.toString());
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
