///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.object.attribute;
//
//import java.util.ArrayList;
//import java.util.List;
//import name.huliqing.core.data.AttributeData;
//import name.huliqing.core.object.Loader;
//import name.huliqing.core.object.module.AttributeModule;
//
///**
// * ReplacerAttribute属性与GroupAttribute相似，可以将一系列的属性打包成一个属性组，
// * 区别是这个属性组中的所有属性在载入时都会偿试去替换掉原有的属性，替换的时候是根据属性<b>名称</b>来进行替换的。
// * 当ReplaceAttribute在载入一个子属性时会去判断角色是否已经存在了一个相同名称的子属性，
// * 如果已经存在则移除旧的，并将新的属性替换进去。
// * @deprecated 不再需要，现在AttributeModule在添加属性的时候可以根据id和名称自动替换掉旧的属性。
// * @author huliqing
// */
//public class ReplacerAttribute extends AbstractAttribute<Void, AttributeData> {
//    private AttributeModule module;
//    
//    // 初始配置的属性（id )
//    private String[] attributeIds;
//    
//    private List<Attribute> attributes;
//    
//    @Override
//    public void setData(AttributeData data) {
//        super.setData(data); 
//        this.attributeIds = data.getAsArray("attributes");
//    }
//    
//    @Override
//    public Void getValue() {
//        return null;
//    }
//
//    @Override
//    public void setValue(Void value) {
//        // ignore
//    }
//
//    @Override
//    public void initialize(AttributeModule module) {
//        super.initialize(module);
//        this.module = module;
//        attributes = getAttributes();
//        if (attributes != null) {
//            for (Attribute a : attributes) {
//                // 移除旧的属性
//                Attribute oldAttribute = module.getAttributeByName(a.getName());
//                if (oldAttribute != null) {
//                    module.removeAttribute(oldAttribute);
//                }
//                // 替换为新的属性
//                module.addAttribute(a);
//            }
//        }
//    }
//
//    @Override
//    public void cleanup() {
//        if (attributes != null) {
//            for (Attribute a : attributes) {
//                module.removeAttribute(a);
//            }
//            attributes.clear();
//            attributes = null;
//        }
//        super.cleanup();
//    }
//    
//    // 载入属性并进行代换
//    public List<Attribute> getAttributes() {
//        if (attributeIds == null || attributeIds.length <= 0) {
//            return null;
//        }
//        
//        // 载入初始配置
//        // 注：map key 是属性的名称
//        List<Attribute> tempList = new ArrayList<Attribute>(attributeIds.length);
//        for (String attrId : attributeIds) {
//            if (attrId.equals(getId())) {
//                continue; // 不能包含自己,以免死循环
//            }
//            tempList.add((Attribute)Loader.load(attrId));
//        }
//        return tempList;
//    }
//}
