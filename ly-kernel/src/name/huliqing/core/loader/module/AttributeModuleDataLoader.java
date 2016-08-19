/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader.module;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.data.module.AttributeModuleData;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.xml.DataLoader;
import name.huliqing.core.xml.Proto;

/**
 *
 * @author huliqing
 */
public class AttributeModuleDataLoader implements DataLoader<AttributeModuleData> {

    @Override
    public void load(Proto proto, AttributeModuleData data) {
        String[] attributeArr = proto.getAsArray("attributes");
        if (attributeArr != null) {
            List<AttributeData> attributes = new ArrayList<AttributeData>(attributeArr.length);
            for (String attrId : attributeArr) {
                attributes.add((AttributeData) DataFactory.createData(attrId));
            }
            data.setAttributes(attributes);
        }
    }
}
