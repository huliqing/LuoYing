/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

/**
 *
 * @author huliqing
 */
public class PropertyConverterDefine {
    
    public final String propertyName;
    public final Class<? extends PropertyConverter> propertyConverter;

    public PropertyConverterDefine(String propertyName, Class<? extends PropertyConverter> propertyConverter) {
        this.propertyName = propertyName;
        this.propertyConverter = propertyConverter;
    }
    
}
