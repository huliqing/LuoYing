/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.state;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 可以改变角色属性数值的状态.
 * @author huliqing
 */
public class AttributeState extends AbstractState {
    private final EntityService entityService = Factory.get(EntityService.class);
    
    private String bindNumberAttribute;
    private float addValue;
    // 是否在属性移除后恢复属性值
    private boolean restore;
    
    // ---- inner
    // 被修改的指定属性
    // 实际操作的属性值
    private float finalAddValue;
    // 标记属性是否已经作用到目标
    private boolean attributeApplied;
    private Entity sourceActor;
    
    @Override
    public void setData(StateData data) {
        super.setData(data); 
        this.bindNumberAttribute = data.getAsString("bindNumberAttribute");
        this.addValue = data.getAsFloat("addValue", addValue);
        this.restore = data.getAsBoolean("restore", restore);
        this.attributeApplied = data.getAsBoolean("attributeApplied", attributeApplied);
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("attributeApplied", attributeApplied);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        if (attributeApplied) {
            return;
        }
        sourceActor = getSourceActor();
        
        // data.getResist()为抵抗率，取值 [0.0~1.0], 如果为1.0则说明完全抵抗. 
        finalAddValue = addValue * (1 - getResist());
        entityService.hitNumberAttribute(actor, bindNumberAttribute, finalAddValue, sourceActor);
        attributeApplied = true;
        updateDatas();
    }
    
    @Override
    public void cleanup() {
        if (attributeApplied && restore) {
            entityService.hitNumberAttribute(actor, bindNumberAttribute, -finalAddValue, sourceActor);
            attributeApplied = false;
            updateDatas();
        }
        super.cleanup();
    }

}
