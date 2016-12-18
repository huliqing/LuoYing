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
    
    // ---- inner
    // 标记着子属性是否已经添加到module上。
    private boolean attributesApplied;
    
    // 这个参数用于记住由GroupAttribute打包并添加到module上的所有属性的唯一id.
    // 在清理时只清理这些指定id的属性就可以。不应该使用属性id或名称，因为属性是可以在运行时替换的。
    // GroupAttribute添加上去的属性有可能在运行时被其它相同名称的属性替换掉，对于这些则GroupAttribute不应该在退出时清理。
    // 即只清理那些”明确的“由自己添加到模块上的属性”实例“就可以。
    private long[] attributeAppliedIds;
    
    @Override
    public void setData(AttributeData data) {
        super.setData(data); 
        this.attributeIds = data.getAsArray("attributes");
        this.attributesApplied = data.getAsBoolean("_attributesApplied", attributesApplied);
        this.attributeAppliedIds = data.getAsLongArray("_attributeAppliedIds");
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("_attributesApplied", attributesApplied);
        data.setAttribute("_attributeAppliedIds", attributeAppliedIds);
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
        List<Attribute> attributes = getAttributes();
        attributeAppliedIds = new long[attributes.size()];
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attr = attributes.get(i);
            module.addAttribute(attr);
            attributeAppliedIds[i] = attr.getData().getUniqueId();
        }
        attributesApplied = true;
    }
    
    @Override
    public void cleanup() {
        if (attributesApplied) {
            if (attributeAppliedIds != null) {
               Attribute attr;
                for (long attrId : attributeAppliedIds) {
                    attr = module.getAttribute(attrId);
                    // 属性有可能在运行时被替换，所以当GroupAttribute清理时，
                    // 这些由GroupAttribute添加上去的属性并不能绝对保存还存在着。
                    if (attr != null)  {
                        module.removeAttribute(attr);
                    }
                }
            }
            attributeAppliedIds = null;
            attributesApplied = false;
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
        return false; // ignore
    }


    
}
