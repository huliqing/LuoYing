/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.item;

import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.ItemConstants;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.effect.Effect;
import name.huliqing.luoying.object.el.SBooleanEl;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.sound.SoundManager;

/**
 * @author huliqing
 */
public abstract class AbstractItem implements Item {
    private static final Logger LOG = Logger.getLogger(AbstractItem.class.getName());
    private final ElService elService = Factory.get(ElService.class);
    
    protected ItemData data;
    
    // 使用物品时的效果
    protected String[] effects;
    
    // 使用物品时的声音
    protected String[] sounds;
    
    // checkEl用于判断角色是否可以使用这件物品
    protected SBooleanEl checkEl;
    
    @Override
    public void setData(ItemData data) {
        this.data = data;
        effects = data.getAsArray("effects");
        sounds = data.getAsArray("sounds");
        checkEl = elService.createSBooleanEl(data.getAsString("checkEl", "#{true}"));
    }
    
    @Override
    public ItemData getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
    }

    @Override
    public String getId() {
        return data.getId();
    }

    @Override
    public void use(Entity actor) {
         if (effects != null) {
            for (String eid : effects) {
                 Effect effect = Loader.load(eid);
                 effect.setTraceObject(actor.getSpatial());
                 actor.getScene().addEntity(effect);
            }
        }
        if (sounds != null) {
            for (String sid : sounds) {
                SoundManager.getInstance().playSound(sid, actor.getSpatial().getWorldTranslation());
            }
        }
        
        // 子类逻辑
        // do "use" logic in children.
    }
    
    @Override
    public boolean canUse(Entity actor) {
        return checkStateCode(actor) == ItemConstants.STATE_OK;
    }
    
    @Override
    public int checkStateCode(Entity actor) {
        // 物品数量不够。
        if (data.getTotal() <= 0) {
            return ItemConstants.STATE_ITEM_NOT_ENOUGH;
        }
        
        checkEl.setSource(actor.getAttributeManager());
        if (!checkEl.getValue()) {
            return ItemConstants.STATE_CHECK_EL;
        }
        
        return ItemConstants.STATE_OK;
    }

}
