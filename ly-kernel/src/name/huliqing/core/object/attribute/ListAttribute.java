/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.attribute;

import java.util.List;
import name.huliqing.core.data.AttributeData;

/**
 *
 * @author huliqing
 */
public class ListAttribute extends AbstractAttribute<List, AttributeData> {
    
    private List value;

    @Override
    public List getValue() {
        return value;
    }

    @Override
    public void setValue(List value) {
        this.value = value;
    }

    @Override
    public boolean match(Object other) {
        return super.match(other); 
    }
    
}
