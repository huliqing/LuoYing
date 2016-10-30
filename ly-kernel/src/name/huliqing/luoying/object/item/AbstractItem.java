/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.item;

import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.constants.ItemConstants;
import name.huliqing.luoying.data.AttributeMatch;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.effect.Effect;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.sound.SoundManager;

/**
 * @author huliqing
 */
public abstract class AbstractItem implements Item {
    private static final Logger LOG = Logger.getLogger(AbstractItem.class.getName());
    
    protected ItemData data;
    
    // 使用物品时的效果
    protected String[] effects;
    
    // 使用物品时的声音
    protected String[] sounds;
    
    @Override
    public void setData(ItemData data) {
        this.data = data;
        effects = data.getAsArray("effects");
        sounds = data.getAsArray("sounds");
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
        
        // 如果角色的属性中有一个不能和getMatchAttributes中要求的不匹配则视为不能使用。
        if (data.getMatchAttributes() != null) {
            Attribute attr;
            for (AttributeMatch am : data.getMatchAttributes()) {
                attr = actor.getAttributeManager().getAttribute(am.getAttributeName());
                if (!(attr instanceof MatchAttribute)) {
                    if (LOG.isLoggable(Level.INFO)) {
                        LOG.log(Level.INFO, "Could not useItem, attribute not a MatchAttribute"
                                + "，actorId={0}, itemId={1},  match attributeName={2}", 
                                new Object[] {actor.getData().getId(), data.getId(), am.getAttributeName()});                        
                    }
                    return ItemConstants.STATE_ATTRIBUTE_NOT_MATCH;
                }
                if (!((MatchAttribute)attr).match(am.getValue())) {
                    LOG.log(Level.INFO, "Could not useItem, attribute not match,actorId={0}, itemId={1}"
                            + ", match attributeName={2}, match attributeValue={3}, actor attribute={4}"
                            , new Object[] {actor.getData().getId(), data.getId(), am.getAttributeName(), am.getValue(), attr});
                    return ItemConstants.STATE_ATTRIBUTE_NOT_MATCH;
                }
            }
        }
        return ItemConstants.STATE_OK;
    }

}
