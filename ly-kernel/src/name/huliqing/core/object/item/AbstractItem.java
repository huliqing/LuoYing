/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.item;

import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.data.AttributeMatch;
import name.huliqing.core.data.ItemData;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.attribute.Attribute;
import name.huliqing.core.object.attribute.MatchAttribute;
import name.huliqing.core.object.effect.Effect;
import name.huliqing.core.object.effect.EffectManager;
import name.huliqing.core.object.sound.SoundManager;

/**
 * @author huliqing
 */
public abstract class AbstractItem implements Item {
    private static final Logger LOG = Logger.getLogger(AbstractItem.class.getName());
    private final AttributeService attributeService = Factory.get(AttributeService.class);

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
    public String getId() {
        return data.getId();
    }

    @Override
    public void use(Actor actor) {
        playEffects(actor, effects);
        playSounds(actor, sounds);
        // 子类逻辑
        // do "use" logic in children.
    }
    
    @Override
    public boolean canUse(Actor actor) {
        // 物品数量不够。
        if (data.getTotal() <= 0) {
            return false;
        }
        
        // 如果角色的属性中有一个不能和getMatchAttributes中要求的不匹配则视为不能使用。
        if (data.getMatchAttributes() != null) {
            Attribute attr;
            for (AttributeMatch am : data.getMatchAttributes()) {
                attr = attributeService.getAttributeByName(actor, am.getAttributeName());
                if (!(attr instanceof MatchAttribute)) {
                    if (LOG.isLoggable(Level.INFO)) {
                        LOG.log(Level.INFO, "Could not useItem, attribute not a MatchAttribute"
                                + "，actorId={0}, itemId={1},  match attributeName={2}", 
                                new Object[] {actor.getData().getId(), data.getId(), am.getAttributeName()});                        
                    }
                    return false;
                }
                if (!((MatchAttribute)attr).match(am.getValue())) {
                    LOG.log(Level.INFO, "Could not useItem, attribute not match,actorId={0}, itemId={1}"
                            + ", match attributeName={2}, match attributeValue={3}, actor attribute={4}"
                            , new Object[] {actor.getData().getId(), data.getId(), am.getAttributeName(), am.getValue(), attr});
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * 播放特效
     * @param actor
     * @param effects 
     */
    private void playEffects(Actor actor, String[] effects) {
        if (effects != null) {
            Effect effect;
            for (String eid : effects) {
                 effect = Loader.load(eid);
                 effect.setTraceObject(actor.getSpatial());
                 EffectManager.getInstance().addEffect(effect);
            }
        }
    }
    
    /**
     * 播放声效
     * @param actor
     * @param sounds 
     */
    private void playSounds(Actor actor, String[] sounds) {
        if (sounds != null) {
            for (String sid : sounds) {
                SoundManager.getInstance().playSound(sid, actor.getSpatial().getWorldTranslation());
            }
        }
    }
}
