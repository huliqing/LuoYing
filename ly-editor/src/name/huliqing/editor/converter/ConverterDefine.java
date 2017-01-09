/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author huliqing
 */
public class ConverterDefine {
    
    public final String tagName;
    public final Class<? extends DataConverter> converter;
    public final List<PropertyConverterDefine> propertyConverters = new ArrayList();

    public ConverterDefine(String tagName, Class<? extends DataConverter> converterClass) {
        this.tagName = tagName;
        this.converter = converterClass;
    }

    public void addPropertyConverter(String property, Class<? extends PropertyConverter> propertyConverter) {
        propertyConverters.add(new PropertyConverterDefine(property, propertyConverter));
    }
    public void addPropertyConverter(PropertyConverterDefine propertyConverterDefine) {
        propertyConverters.add(propertyConverterDefine);
    }

    public void extendsFrom(ConverterDefine superConverter) {
        propertyConverters.addAll(superConverter.propertyConverters);
    }
}
