/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.AttributeData;
import name.huliqing.luoying.object.Loader;

/**
 * 属性组，属性组允许将一系列的属性打包成一个属性。在属性组载入时会把这些子属性一个一个载入到角色中。
 * @author huliqing
 */
public class GroupAttribute extends AbstractAttribute<Void> {

    private AttributeManager module;
    
    // 初始配置的属性（id )
    private String[] attributeIds;
    
    private List<Attribute> attributes;
    // 标记着子属性是否已经添加到module上。
    private boolean attributesApplied;
    
    @Override
    public void setData(AttributeData data) {
        super.setData(data); 
        this.attributeIds = data.getAsArray("attributes");
        this.attributesApplied = data.getAsBoolean("attributesApplied", false);
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("attributesApplied", attributesApplied);
    }  
    
    @Override
    public Void getValue() {
        return null;
    }

    @Override
    public void initialize(AttributeManager module) {
        super.initialize(module);
        this.module = module;
        
        // 当属性是从存档中载入时，attributesApplied会为true,这时就不再需要去载入属性。因子属性已经在上次载入到
        // AttributeModule中去了，AttributeModule在重新载入时会自动去载入这些子属性,不需要再在这里载入
        if (attributesApplied) {
            return;
        }
        
        // 添加属性到attributeModule,注意要标记attributesApplied=true,并更新到data中去, 那么当下次从存档中载入时，
        // 就不再需要在这里载入到module中去了，因为会从attributeModule中直接载入。
        attributes = getAttributes();
        for (Attribute ad : attributes) {
            module.addAttribute(ad);
        }
        attributesApplied = true;
        updateDatas();
    }
    
    @Override
    public void cleanup() {
        if (attributes != null) {
            for (Attribute attr : attributes) {
                module.removeAttribute(attr);
            }
            attributes.clear();
            attributes = null;
            attributesApplied = false;
            updateDatas();
        }
        super.cleanup();
    }
    
    // 载入属性并进行代换
    public List<Attribute> getAttributes() {
        // 载入初始配置
        // 注：map key 是属性的名称
        List<Attribute> tempList = new ArrayList<Attribute>(attributeIds.length);
        for (String attrId : attributeIds) {
            if (attrId.equals(getId())) {
                continue; // 不能包含自己,以免死循环
            }
            tempList.add((Attribute)Loader.load(attrId));
        }
        return tempList;
    }

    @Override
    protected boolean doSetValue(Void newValue) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }


    
}
