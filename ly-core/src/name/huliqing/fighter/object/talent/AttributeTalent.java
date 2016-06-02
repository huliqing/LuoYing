/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.talent;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.TalentData;
import name.huliqing.fighter.game.service.AttributeService;
import name.huliqing.fighter.game.service.ElService;

/**
 *
 * @author huliqing
 */
public class AttributeTalent extends AbstractTalent {
    private final ElService elService = Factory.get(ElService.class);
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    
    protected String applyAttribute;
    protected String levelEl;
    
    // ----
    private int level;
    private float applyValue;
    private boolean init;
    
    public AttributeTalent(TalentData data) {
        super(data);
        this.applyAttribute = data.getProto().getAttribute("applyAttribute");
        this.levelEl = data.getProto().getAttribute("levelEl");
        this.level = data.getLevel();
    }

    @Override
    public void init() {
        if (init) {
            return;
        }
        applyValue = getLevelValue(levelEl, level);
        attributeService.applyStaticValue(actor, applyAttribute, applyValue);
        attributeService.applyDynamicValue(actor, applyAttribute, applyValue);
        attributeService.clampDynamicValue(actor, applyAttribute);
        init = true;
    }

    @Override
    protected void doLogic(float tpf) {
        // dologic
    }

    @Override
    public void cleanup() {
        if (init) {
            attributeService.applyStaticValue(actor, applyAttribute, -applyValue);
            attributeService.applyDynamicValue(actor, applyAttribute, -applyValue);
            attributeService.clampDynamicValue(actor, applyAttribute);
            init = false;
        }
    }

    @Override
    public void updateLevel(int level) {
        cleanup();
        this.level = level;
        init();
    }
    
    protected float getLevelValue(String levelEl, int level) {
        float levelValue = elService.getLevelEl(levelEl, level);
        return levelValue;
    }
    
}
