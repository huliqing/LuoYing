///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.luoying.mess;
//
//import com.jme3.network.serializing.Serializable;
//import name.huliqing.luoying.Factory;
//import name.huliqing.luoying.layer.service.PlayService;
//import name.huliqing.luoying.object.attribute.NumberAttribute;
//import name.huliqing.luoying.object.entity.Entity;
//
///**
// * 给Entity指定的属性添加属性值，属性类型必须是Number类型的
// * @author huliqing
// */
//@Serializable
//public class MessEntityAttributeAddValue extends MessBase {
//    
//    private long entityId;
//
//    private String attributeName;
//    
//    private float value;
//    
//    public long getEntityId() {
//        return entityId;
//    }
//
//    public void setEntityId(long actorId) {
//        this.entityId = actorId;
//    }
//
//    public String getAttributeName() {
//        return attributeName;
//    }
//
//    public void setAttributeName(String attributeName) {
//        this.attributeName = attributeName;
//    }
//
//    public float getValue() {
//        return value;
//    }
//
//    public void setValue(float value) {
//        this.value = value;
//    }
//
//    @Override
//    public void applyOnClient() {
//        PlayService playService = Factory.get(PlayService.class);
//        Entity actor = playService.getEntity(entityId);
//        if (actor != null) {
//            NumberAttribute nattr = actor.getAttributeManager().getAttribute(attributeName, NumberAttribute.class);
//            if (nattr != null) {
//                nattr.add(value);
//            }
//        }
//    }
//    
//}
