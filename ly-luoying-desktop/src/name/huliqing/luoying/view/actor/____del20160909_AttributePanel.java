package name.huliqing.luoying.view.actor;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.view.actor;
//
//import com.jme3.font.BitmapFont;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.List;
//import name.huliqing.core.Factory;
//import name.huliqing.core.constants.ResConstants;
//import name.huliqing.core.mvc.service.ActorService;
//import name.huliqing.core.manager.ResourceManager;
//import name.huliqing.core.mvc.service.AttributeService;
//import name.huliqing.core.object.actor.Actor;
//import name.huliqing.core.object.attribute.AbstractSimpleAttribute;
//import name.huliqing.core.object.attribute.Attribute;
//import name.huliqing.core.object.attribute.LimitAttribute;
//import name.huliqing.core.ui.UIFactory;
//import name.huliqing.core.ui.LinearLayout;
//import name.huliqing.core.ui.Text;
//import name.huliqing.core.ui.UI;
//
///**
// * 角色属性面板
// * @author huliqing
// */
//public class AttributePanel extends LinearLayout implements ActorPanel {
//    private final ActorService actorService = Factory.get(ActorService.class);
//    private final AttributeService attributeService = Factory.get(AttributeService.class);
//    private final DecimalFormat format = new DecimalFormat("#.#");
//    
//    // 等级
//    private AttrItem level;
//    
//    // 属性列表，保持对列表的引用
//    private final List<AttrItem> attrs = new ArrayList<AttrItem>();
//    
//    public AttributePanel() {
//        super();
//        setPadding(5, 0, 5, 0);
//        level = new AttrItem(ResourceManager.get(ResConstants.COMMON_LEVEL), "");
//        addView(level);
//    }
//
//    @Override
//    public void updateViewChildren() {
//        super.updateViewChildren();
//        List<UI> uis = this.getViews();
//        float itemHeight = height / uis.size();
//        for (UI ui : uis) {
//            AttrItem item = (AttrItem) ui;
//            item.setWidth(width - 10);
//            item.setHeight(itemHeight);
////            item.setMargin(5, 0, 0, 0);
//        }
//    }
//    
//    /**
//     * 设置是否显示面板
//     * @param visible 
//     */
//    @Override
//    public void setPanelVisible(boolean visible) {
//        this.setVisible(visible);
//    }   
//    
//    @Override
//    public void setPanelUpdate(Actor actor) {
//        if (actor == null) {
//            return;
//        }
//        
//        // level
//        level.setValue(actorService.getLevel(actor));
//        
//        // attributes
//        List<Attribute> attributes = attributeService.getAttributes(actor);
//        
//        int count = 0;
//        if (attributes != null) {
//            for (Attribute attr:  attributes) {
//                AttrItem item;
//                if (count >= attrs.size()) {
//                    item = new AttrItem("", "");
//                    addView(item);
//                    attrs.add(item);
//                } else {
//                    item = (AttrItem) attrs.get(count);
//                }
//                item.setLabel(ResourceManager.getObjectName(attr.getData()));
//                if (attr instanceof LimitAttribute) {
//                    item.setValue(((LimitAttribute)attr).getValue() + "/" + ((LimitAttribute) attr).getMaxLimit());
//                } else if (attr instanceof AbstractSimpleAttribute) {
//                    item.setValue(((AbstractSimpleAttribute)attr).getValue());
//                } else {
//                    item.setValue(attr.toString());
//                }
//                count++;
//            }
//        }
//        
//        // 角色的属性配置数量可能不尽相同，可能存在空的栏位，则清空。
//        if (count < attrs.size()) {
//            for (int i = count; i < attrs.size(); i++) {
//                AttrItem item = (AttrItem) attrs.get(i);
//                item.setValue("");
//                item.setVisible(false);
//            }
//        }
//
//    }
//    
//    private class AttrItem extends LinearLayout {
//        private Text label;
//        private Text value;
//
//        public AttrItem(String label, Object value) {
//            super();
//            this.setLayout(Layout.horizontal);
//            this.label = new Text(label);
//            this.value = new Text(String.valueOf(value));
//            this.addView(this.label);
//            this.addView(this.value);
//        }
//
//        @Override
//        protected void updateViewChildren() {
//            super.updateViewChildren();
//            
//            float fontSize = UIFactory.getUIConfig().getBodyFontSize();
//            if (fontSize > height) {
//                fontSize = height;
//            }
//            
//            this.label.setWidth(width * 0.6f);
//            this.label.setHeight(height);
//            this.label.setFontSize(fontSize);
////            this.label.setVerticalAlignment(BitmapFont.VAlign.Center);
//            
//            this.value.setWidth(width * 0.4f);
//            this.value.setHeight(height);
//            this.value.setFontSize(fontSize);
//            this.value.setVerticalAlignment(BitmapFont.VAlign.Center);
//            this.value.setAlignment(BitmapFont.Align.Right);
//            value.setMargin(0, 0, 10, 0);
//        }
//        
//        public void setValue(Object value) {
//            this.value.setText(String.valueOf(value));
//        }
//
//        public void setLabel(String label) {
//            this.label.setText(label);
//        }
//
//    }
//}
