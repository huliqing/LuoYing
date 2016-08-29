package name.huliqing.core.object.attribute;

import name.huliqing.core.data.AttributeData;

/**
 * 可限制取值范围的属性,可以通过设置minValue,maxValue来直接限制当前值的取值范围，
 * 或者通过绑定其它属性来限制当前属性的取值范围。
 * @author huliqing
 */
public class LimitIntegerAttribute extends IntegerAttribute implements LimitAttribute, ValueChangeListener<Number>{

    private int minValue = Integer.MIN_VALUE;
    private int maxValue = Integer.MAX_VALUE;
    private String minLimitAttributeName;
    private String maxLimitAttributeName;
    private NumberAttribute minLimitAttribute;
    private NumberAttribute maxLimitAttribute;
    
    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        minValue = data.getAsInteger("minValue", minValue);
        maxValue = data.getAsInteger("maxValue", maxValue);
        minLimitAttributeName = data.getAsString("minLimitAttributeName");
        maxLimitAttributeName = data.getAsString("maxLimitAttributeName");
    }

    @Override
    protected void updateData() {
        super.updateData();
        data.setAttribute("minValue", minValue);
        data.setAttribute("maxValue", maxValue);
        // minLimitAttributeName, maxLimitAttributeName不发生变化，所以不需要回设到data中去
//        data.setAttribute("minLimitAttributeName", minLimitAttributeName);
//        data.setAttribute("maxLimitAttributeName", maxLimitAttributeName);
    }

    @Override
    public float getMaxLimit() {
        return maxValue;
    }

    @Override
    public void setMax() {
        setAndNotify(maxValue);
    }

    @Override
    public float getMinLimit() {
        return minValue;
    }

    @Override
    public void setMin() {
        setAndNotify(minValue);
    }

    @Override
    public void initialize(AttributeStore store) {
        super.initialize(store);
        
        // 从store中找出需要绑定的用于限制最小值或最高值的attribute
        if (minLimitAttributeName != null) {
            Attribute tempAttribute = store.getAttributeByName(minLimitAttributeName);
            if (!(tempAttribute instanceof NumberAttribute)) {
                throw new IllegalArgumentException("\"minLimitAttributeName\" property of LimitIntegerAttribute"
                        + " only support NumberAttribute,"
                        + " but found other attribute, attribute=" + tempAttribute 
                        + ", attributeId=" + tempAttribute.getId() 
                        + ", attributeName=" + tempAttribute.getName());
            }
            minLimitAttribute = (NumberAttribute) tempAttribute;
            minLimitAttribute.addListener(this);
            if (minLimitAttribute.isInitialized()) {
                minValue = minLimitAttribute.intValue();
            }
        }
        
        if (maxLimitAttributeName != null) {
            Attribute tempAttribute = store.getAttributeByName(maxLimitAttributeName);
            if (!(tempAttribute instanceof NumberAttribute)) {
                throw new IllegalArgumentException("\"maxLimitAttributeName\" property of LimitIntegerAttribute"
                        + " only support NumberAttribute,"
                        + " but found other attribute, attribute=" + tempAttribute 
                        + ", attributeId=" + tempAttribute.getId() 
                        + ", attributeName=" + tempAttribute.getName());
            }
            maxLimitAttribute = (NumberAttribute) tempAttribute;
            maxLimitAttribute.addListener(this);
            if (maxLimitAttribute.isInitialized()) {
                maxValue = maxLimitAttribute.intValue();
            }
        }
        
        // 初始化值并触发一次侦听器
        setAndNotify(getValue());
        
    }

    @Override
    public void cleanup() {
        if (minLimitAttribute != null) {
            minLimitAttribute.removeListener(this);
        }
        if (maxLimitAttribute != null) {
            maxLimitAttribute.removeListener(this);
        }
        super.cleanup(); 
    }
    
    @Override
    protected void setAndNotify(int value) {
        if (value < minValue) {
            value = minValue;
        }
        if (value > maxValue) {
            value = maxValue;
        }
        super.setAndNotify(value);
    }
    
    @Override
    public void onValueChanged(Attribute attribute, Number oldValue, Number newValue) {
        if (attribute == minLimitAttribute) {
            minValue = minLimitAttribute.intValue();
            setAndNotify(intValue());
        }
        if (attribute == maxLimitAttribute) {
            maxValue = maxLimitAttribute.intValue();
            setAndNotify(intValue());
        }
    }

}
