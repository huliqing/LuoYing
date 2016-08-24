package name.huliqing.core.object.attribute;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.object.Loader;

/**
 * RangeIntegerAttribute是一个能限制属性值范围的属性。
 * @author huliqing
 */
public class RangeIntegerAttribute extends IntegerAttribute implements NodeAttribute, ValueChangeListener<Number> {

    // 用于限制当前值的另一个属值值的id,必须是NumberAttribute类型
    private String bindMaxValueAttribute;
    
    // ---- inner
    // 这个值是用来限制RangeIntegerAttribute的最大值的。
    private NumberAttribute maxValueAttribute;
    private final List<Attribute> children = new ArrayList<Attribute>();
    
    @Override
    public void setData(AttributeData data) {
        super.setData(data); 
        bindMaxValueAttribute = data.getAsString("bindMaxValueAttribute");
    }
    
    @Override
    public List<Attribute> getChildren() {
        return children;
    }
    
    public NumberAttribute getMaxValueAttribute() {
        return maxValueAttribute;
    }

    @Override
    public void initialize() {
        super.initialize();
        if (bindMaxValueAttribute != null) {
            Attribute tempAttribute = Loader.load(bindMaxValueAttribute);
            if (!(tempAttribute instanceof NumberAttribute)) {
                throw new IllegalArgumentException("\"bindMaxValueAttribute\" property of RangeIntegerAttribute"
                        + " only support NumberAttribute,"
                        + " but found attribute type=" + tempAttribute + ", attribute id=" + bindMaxValueAttribute);
            }
            maxValueAttribute = (NumberAttribute) tempAttribute;
            maxValueAttribute.addListener(this);
            children.add(maxValueAttribute);
        }
    }

    @Override
    public void cleanup() {
        if (maxValueAttribute != null) {
            maxValueAttribute.removeListener(this);
        }
        children.clear();
        super.cleanup(); 
    }
    
    @Override
    protected void setAndNotify(int value) {
        if (maxValueAttribute != null) {
            if (value > maxValueAttribute.intValue()) {
                value = maxValueAttribute.intValue();
            }
        }
        super.setAndNotify(value);
    }
    
    // 如果maxValueAttribute的值发生变化，则判断当前属性值是否会超过maxValueAttribute所改变后的值，如果超过则要
    // 限制当前值的大小。
    @Override
    public void onValueChanged(Attribute maxValueAttribute, Number oldValue, Number newValue) {
        if (this.maxValueAttribute == maxValueAttribute) {
            if (this.intValue() > this.maxValueAttribute.intValue()) {
                setAndNotify(this.maxValueAttribute.intValue());
            }
        }
    }

}
