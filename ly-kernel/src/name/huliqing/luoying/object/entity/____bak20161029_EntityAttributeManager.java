///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.luoying.object.entity;
//
//import name.huliqing.luoying.object.attribute.AttributeStore;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import name.huliqing.luoying.data.AttributeData;
//import name.huliqing.luoying.object.Loader;
//import name.huliqing.luoying.object.attribute.Attribute;
//import name.huliqing.luoying.object.attribute.AttributeListener;
//import name.huliqing.luoying.object.attribute.AttributeManager;
//import name.huliqing.luoying.object.attribute.NumberAttribute;
//
///**
// * Entity的属性管理器
// * @author huliqing
// */
//public class EntityAttributeManager implements AttributeManager {
//    private static final Logger LOG = Logger.getLogger(EntityAttributeManager.class.getName());
//    
//    private final AttributeStore store = new AttributeStore();
//    private final Entity entity;
//    private List<AttributeListener> listeners;
//    
//    public EntityAttributeManager(Entity entity) {
//        this.entity = entity;
//    }
//    
//    public void updateDatas() {
//        // xxx do update attribute datas
//    }
//    
//    public void initialize(Entity actor) {
//        // 载入所有属性
//        List<AttributeData> ods = actor.getData().getObjectDatas(AttributeData.class, null);
//        if (ods != null && !ods.isEmpty()) {
//            for (AttributeData od : ods) {
//                addAttribute((Attribute) Loader.load(od));
//            }
//        }
//    }
//    
//    public void cleanup() {
//        store.clear();
//    }
//    
//    /**
//     * 添加新的属性，注：如果已经存在相同id或名称的属性，则旧的属性会被替换掉。
//     * @param attribute 
//     */
//    @Override
//    public void addAttribute(Attribute attribute) {
//        // 如果已经存在指定属性，则替换掉
//        Attribute oldAttriubteById = store.getAttributeById(attribute.getId());
//        Attribute oldAttriubteByName = store.getAttributeByName(attribute.getName());
//        if (oldAttriubteById != null) {
//            removeAttribute(oldAttriubteById);
//        }
//        if (oldAttriubteByName != null) {
//            removeAttribute(oldAttriubteByName);
//        }
//        try {
//            store.addAttribute(attribute);
//            entity.getData().addObjectData(attribute.getData());
//            if (!attribute.isInitialized()) {
//                attribute.initialize(this);
//            }
//            if (listeners != null) {
//                for (int i = 0; i < listeners.size(); i++) {
//                    listeners.get(i).onAttributeAdded(this, attribute);
//                }
//            }
//        } catch (AttributeStore.AttributeConflictException ex) {
//            throw new RuntimeException(ex);
//        }
//    }
//    
//    /**
//     * 移除指定的属性。
//     * @param attribute
//     * @return 
//     */
//    @Override
//    public boolean removeAttribute(Attribute attribute) {
//        if (store.removeAttribute(attribute)) {
//            entity.getData().removeObjectData(attribute.getData());
//            attribute.cleanup();
//            if (listeners != null) {
//                for (int i = 0; i < listeners.size(); i++) {
//                    listeners.get(i).onAttributeRemoved(this, attribute);
//                }
//            }
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 通过属性名称获取指定的属性，如果属性不存在则返回null<br>
//     * 注：返回值为泛型(Attribute)，如果使用了特定的属性类型来接收返回值，
//     * 则当返回类型不能转换为指定类型时会导致转换异常。
//     * 要避免转换类型异常可使用{@link #getAttribute(java.lang.String, java.lang.Class) } 
//     * @param <T>
//     * @param attrName 属性名称，<b>不</b>是属性ID(<b>Not</b>id)
//     * @return 
//     * @see #getAttribute(java.lang.String, java.lang.Class) 
//     */
//    @Override
//    public <T extends Attribute> T getAttribute(String attrName) {
//        return (T) store.getAttributeByName(attrName);
//    }
//    
//    /**
//     * 查找指定的属性，如果找不到或者指定的属性类型不匹配则返回null.
//     * @param <T>
//     * @param attrName 属性名称，<b>不</b>是属性ID(<b>Not</b>id)
//     * @param type 如果不为null, 则找到的属性必须符合这个类型，否则返回null.
//     * @return 
//     * @see #getAttribute(java.lang.String) 
//     */
//    @Override
//    public <T extends Attribute> T getAttribute(String attrName, Class<T> type) {
//        Attribute attribute = store.getAttributeByName(attrName);
//        if (attribute == null) {
//            return null;
//        }
//        if (type != null && !type.isAssignableFrom(attribute.getClass())) {
//            LOG.log(Level.WARNING, "Attribute {0} is not type of {1}, entityId={2}"
//                    , new Object[] {attrName, type.getName(), entity.getData().getId()});
//            return null;
//        }
//        return (T) attribute;
//    }
//    
//    /**
//     * 获取角色当前的所有属性，注：返回的列表只能只读，否则报错。
//     * @return 
//     */
//    @Override
//    public List<Attribute>getAttributes() {
//        return store.getAttributes();
//    }
//    
//    /**
//     * 添加属性侦听器
//     * @param attributeListener 
//     */
//    @Override
//    public void addListener(AttributeListener attributeListener) {
//        if (listeners == null) {
//            listeners = new ArrayList<AttributeListener>();
//        }
//        if (!listeners.contains(attributeListener)) {
//            listeners.add(attributeListener);
//        }
//    }
//    
//    /**
//     * 移除指定的属性侦听器
//     * @param attributeListener
//     * @return 
//     */
//    @Override
//    public boolean removeListener(AttributeListener attributeListener) {
//        return listeners != null && listeners.remove(attributeListener);
//    }
//    
//    /**
//     * 给指定“名称”的属性添加值。注:所指定的属性必须存在，并且必须是 {@link NumberAttribute}类型的属性，
//     * 否则什么也不做。
//     * @param attrName 属性名称
//     * @param value 
//     */
//    public void addNumberAttributeValue(String attrName, float value) {
//        NumberAttribute attr = getAttribute(attrName, NumberAttribute.class);
//        if (attr != null) {
//            attr.add(value);
//        }
//    }
//    
//    /**
//     * 获取指定“名称“的NumberAttribute类型的属性的值，目标属性必须存在，并且必须是NubmerAttribute，
//     * 否则这个方法将指返回defValue值。
//     * @param attrName 属性名称
//     * @param defValue 默认值，如果找不到指定的属性或属性不是NumberAttribute类型则返回这个默认值。
//     * @return 
//     */
//    public float getNumberAttributeValue(String attrName, float defValue) {
//        NumberAttribute attr = getAttribute(attrName, NumberAttribute.class);
//        if (attr != null) {
//            return attr.floatValue();
//        }
//        return defValue;
//    }
//    
//}
