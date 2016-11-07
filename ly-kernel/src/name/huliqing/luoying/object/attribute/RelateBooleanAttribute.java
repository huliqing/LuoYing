/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.LuoYingException;
import name.huliqing.luoying.data.AttributeData;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.object.el.CustomEl;

/**
 * 带有关联功能的Boolean属性，这种属性可以根据其它属性的变动而联动改变当前属性值。可以通过refs参数来关联其它属性，
 * 并通过valueEl来计算值,El必须返回一个Boolean类型的值。
 * @author huliqing
 */
public class RelateBooleanAttribute extends BooleanAttribute implements AttributeListener, ValueChangeListener<Object>{
//    private static final Logger LOG = Logger.getLogger(RelateBooleanAttribute.class.getName());
    private final ElService elService = Factory.get(ElService.class);
    private AttributeManager attributeManager;
    
    private RefAttribute[] refs;
    private CustomEl<Boolean> valueEl;
    
    // ---- 
    // 判断valueEl是否有效，只有在所有引用的属性都找到的情况下才有效,在无效的情况下获取值会报错。
    private boolean elValid;
    // 标记值是否有效，如果无效则需要重新计算。
    private boolean valid;
    
    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        // format:  refVars="h=attributeHealth,m=attributeMana,..."
        String[] refVars = data.getAsArray("refVars");
        if (refVars != null) {
            refs = new RefAttribute[refVars.length];
            for (int i = 0; i < refVars.length; i++) {
                String[] temp = refVars[i].split("=");
                RefAttribute ra = new RefAttribute();
                ra.refVarName = temp[0];
                ra.refAttributeName = temp[1];
                refs[i] = ra;
                // 引用的属性不能指向自身
                if (ra.refAttributeName.equals(getName())) {
                    throw new LuoYingException("\"refVars\" could not ref self, attributeId=" + getId() 
                            + ", attributeName=" + getName() 
                            + ", refVars=" + data.getAsString("refVars"));
                }
            }
        }
        // 表达式，示例 valueEl="#{h > 0 && m>0}"
        String tempValueEl = data.getAsString("valueEl");
        if (tempValueEl != null) {
            valueEl = elService.createCustomEl(tempValueEl);
        }
    }

    @Override
    public void initialize(AttributeManager am) {
        super.initialize(am);
        attributeManager = am;
        
        // 查找引用的属性,并添加到引用中
        if (refs != null) {
            List<Attribute> attrsToRef = new ArrayList<Attribute>(refs.length);
            for (RefAttribute ra : refs) {
                Attribute attr = am.getAttribute(ra.refAttributeName);
                if (attr != null) {
                    attrsToRef.add(attr);
                }
            }
            for (Attribute attr : attrsToRef) {
                checkAddRefAttribute(attr);
            }
        }
        
        // 注：由于属性的载入存在先后,并且属性允许动态添加和删除，所以需要添加侦听器来监听属性的变动。
        // 以便更新关联的属性。
        am.addListener(this);
        
        // 检查一下表达式是否有效, 并在有侦听器的情况下主动计算值。
        checkElValid();
        if (listeners != null && !listeners.isEmpty()) {
            caculateValue();
        }
    }
    
    @Override
    public void cleanup() {
        if (refs != null) {
            for (RefAttribute ra : refs) {
                if (ra.attribute != null) {
                    ra.attribute.removeListener(this);
                    ra.attribute = null;
                }
            }
        }
        attributeManager.removeListener(this);
        super.cleanup(); 
    }

    @Override
    public Boolean getValue() {
        if (!valid) {
            caculateValue();
        }
        return super.getValue();
    }
    
    // 重新计算表达式的值。
    private void caculateValue() {
        if (elValid && valueEl != null) {
            setValue(valueEl.getValue());
            valid = true;
        }
    }
    
    // 检查表达式是否已经有效，即所有需要引用的属性都已经找到。
    private void checkElValid() {
        elValid = true;
        // 检查El是否有效,如果有任何一个引用的属性未找到，则标记 elValid是无效的，不能计算值。
        if (refs != null) {
            for (RefAttribute ra : refs) {
                if (ra.attribute == null) {
                    elValid = false;
                    break;
                }
            }
        }
    }
    
    @Override
    public void onValueChanged(Attribute attribute) {
        if (refs == null)
            return;
        for (RefAttribute ra : refs) {
            if (ra.attribute == attribute) {
                if (valueEl != null) {
                    valueEl.setProperty(ra.refVarName, ra.attribute.getValue());
                }
                valid = false;
                // 引用的属性中任何一个发生改变，并且当前属性存在侦听器，则必须立即主动计算值,以触发侦听器，
                // 否则进行懒性计算，当getValue时再去计算。
                if (listeners != null && !listeners.isEmpty()) {
                    caculateValue();
                }
                break;
            }
        }
    }
    
    // 检查新添加的属性是否为引用的属性，如果是则替换为新的引用。
    private void checkAddRefAttribute(Attribute attrAdded) {
        boolean changed = false;
        if (refs != null) {
            for (RefAttribute ra : refs) {
                if (ra.refAttributeName.equals(attrAdded.getName())) {
                    // 替换引用的属性之前清理掉属性的值变侦听
                    if (ra.attribute != null) {
                        ra.attribute.removeListener(this);
                    }
                    // 指向新的引用
                    ra.attribute = attrAdded;
                    ra.attribute.addListener(this);
                    if (valueEl != null) {
                        valueEl.setProperty(ra.refVarName, ra.attribute.getValue());
                    }
                    changed = true;
                    break;
                }
            }
        }
        if (changed) {
            // 检查表达式是否有效
            checkElValid();
            // 标记值需要重新计算
            valid = false;
            // 如果存在侦听器，则必须主动计算值。
            if (listeners != null && !listeners.isEmpty()) {
                caculateValue();
            }
        }
    }
    
    // 检查被移除的属性是否是引用的属性，如果是则从引用中移除。
    private void checkRemoveRefAttribute(Attribute attrRemoved) {
        if (attrRemoved == null || refs == null)
            return;
        for (RefAttribute ra : refs) {
            if (ra.attribute == attrRemoved) {
                ra.attribute.removeListener(this);
                ra.attribute = null;
                if (valueEl != null) {
                    valueEl.setProperty(ra.refVarName, null);
                }
                // 如果有任何一个引用的属性移除了，那么el可能已经失效，虽然有可能表达式中未使用到引用的属性，
                // 但还是要确保不会出现不可预料的异常。
                elValid = false;
                // 值也需要重新计算
                valid = false;
                break;
            }
        }
    }
    
    // 检查新添加的属性是否正是关联的属性。
    @Override
    public void onAttributeAdded(AttributeManager am, Attribute attribute) {
        checkAddRefAttribute(attribute);
    }

    // 当属性移除时要检查是否移除了受到关联的属性。
    @Override
    public void onAttributeRemoved(AttributeManager am, Attribute attribute) {
        checkRemoveRefAttribute(attribute);
    }
    
    private class RefAttribute {
        // 引用名称
        String refVarName;
        // 引用的属性名称
        String refAttributeName;
        // 引用的属性实例
        Attribute attribute;
    }
    
}
