/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.talent;

import name.huliqing.core.Factory;
import name.huliqing.core.data.TalentData;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.mvc.service.ElService;

/**
 * 属性类型的天赋，这种天赋可以增加或减少角色的属性值。
 * @author huliqing
 * @param <T>
 */
public class AttributeTalent<T extends TalentData> extends AbstractTalent<T> {
    private final ElService elService = Factory.get(ElService.class);
    private final AttributeService attributeService = Factory.get(AttributeService.class);

    protected String applyAttribute;
    protected String levelEl;
    
    // ----
    private int level;
    private float applyValue;
    // 判断天赋是否已经apply到角色身上, 需要这个判断是因为有可能角色是从存档中载入的，
    // 由于从存档中载入时角色的属性值已经被当前天赋改变过，所以就不再需要再去改变角色的属性，
    // 否则会造成属性值在存档后再次载入时不停的累加的bug.
    private boolean attributeApplied;

    @Override
    public void setData(T data) {
        super.setData(data); 
        this.applyAttribute = data.getAsString("applyAttribute");
        this.levelEl = data.getAsString("levelEl");
        this.level = data.getLevel();
        this.attributeApplied = data.getAsBoolean("attributeApplied", attributeApplied);
    }
    
    protected void updateData() {
        data.setLevel(level);
        data.setAttribute("attributeApplied", attributeApplied);
    }

    @Override
    public void initialize() {
        super.initialize();
        
        if (!attributeApplied) {
            applyValue = elService.getLevelEl(levelEl, level); 
            attributeService.addAttributeValue(actor, applyAttribute, applyValue);
            attributeApplied = true;
            updateData();
        }
    }

    @Override
    protected void doLogic(float tpf) {
        // dologic
    }

    @Override
    public void cleanup() {
        if (attributeApplied) {
            attributeService.addAttributeValue(actor, applyAttribute, -applyValue);
            attributeApplied = false;
            updateData();
        }
        super.cleanup();
    }

    @Override
    public void updateLevel(int level) {
        cleanup();
        this.level = level;
        initialize();
    }
    
}
