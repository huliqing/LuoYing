/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.actor;

import com.jme3.font.BitmapFont;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import name.huliqing.ly.Factory;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.ly.layer.service.ActorService;
import name.huliqing.ly.layer.service.AttributeService;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.attribute.AbstractSimpleAttribute;
import name.huliqing.ly.object.attribute.Attribute;
import name.huliqing.ly.object.attribute.GroupAttribute;
import name.huliqing.ly.object.attribute.LimitAttribute;
import name.huliqing.ly.ui.ListView;
import name.huliqing.ly.ui.Row;
import name.huliqing.ly.ui.Text;

/**
 * 角色属性面板
 * @author huliqing
 */
public class AttributePanel extends ListView<Attribute> implements ActorPanel {
    private final ActorService actorService = Factory.get(ActorService.class);
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    private final DecimalFormat format = new DecimalFormat("#.#");
    
    // 临时性的用于过滤不想显示的属性名称，后续需要添加到xml配置中去。
    private final List<String> filterAttributeNames;
    
    private Actor actor;
    
    public AttributePanel(float width, float height) {
        super(width, height);
        
        filterAttributeNames = new ArrayList<String>(1);
        filterAttributeNames.add("attributeWeaponSlots");
    }
    
    /**
     * 设置是否显示面板
     * @param visible 
     */
    @Override
    public void setPanelVisible(boolean visible) {
        this.setVisible(visible);
    }
    
    @Override
    public void setPanelUpdate(Actor actor) {
        this.actor = actor;
        getDatas();
        super.refreshPageData();
    }
    
    @Override
    protected Row<Attribute> createEmptyRow() {
        return new AttributeRow(this);
    }
    
    @Override
    protected boolean filter(Attribute data) {
        if (data instanceof GroupAttribute) {
            return true;
        }
        if (filterAttributeNames.contains(data.getName())) {
            return true;
        }
        return super.filter(data); 
    }

    @Override
    public List<Attribute> getDatas() {
        if (actor != null) {
            return attributeService.getAttributes(actor);
        }
        return Collections.EMPTY_LIST;
    }
    
    private class AttributeRow extends Row<Attribute> {

        private final Text label;
        private final Text value;
        
        public AttributeRow(ListView parentView) {
            super(parentView);
            this.setLayout(Layout.horizontal);
            this.label = new Text("");
            this.value = new Text("");
            this.value.setAlignment(BitmapFont.Align.Right);
            this.addView(this.label);
            this.addView(this.value);
        }

        @Override
        protected void updateViewChildren() {
            super.updateViewChildren();
            label.setWidth(width * 0.5f);
            label.setHeight(height);
            value.setWidth(width * 0.5f);
            value.setHeight(height);
        }
        
        @Override
        public void displayRow(Attribute attr) {
            // 这里使用属性"名称"来获取资源文件中的值，与默认使用id不同。
            label.setText(ResourceManager.getObjectName(attr.getName()));
            
            if (attr instanceof LimitAttribute) {
                value.setText(((LimitAttribute)attr).getValue() + "/" + ((LimitAttribute) attr).getMaxLimit());
            } else if (attr instanceof AbstractSimpleAttribute) {
                value.setText(String.valueOf(((AbstractSimpleAttribute)attr).getValue()));
            } else {
                value.setText(attr.toString());
            }
        }
    }
}
