/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.List;
import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.data.ItemData;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.ItemListener;
import name.huliqing.core.object.module.ItemModule;
import name.huliqing.core.object.sound.SoundManager;

/**
 *
 * @author huliqing
 */
public class ItemServiceImpl implements ItemService {
//    private static final Logger LOG = Logger.getLogger(ItemServiceImpl.class.getName());

    private PlayService playService;
    private HandlerService handlerService;
    
    @Override
    public void inject() {
        playService = Factory.get(PlayService.class);
        handlerService = Factory.get(HandlerService.class);
    }
    
    @Override
    public void addItem(Actor actor, String itemId, int count) {
        ItemModule module = actor.getModule(ItemModule.class);
        if (module != null) {
            module.addItem(itemId, count);
            // 提示获得物品,只提示当前场景中的角色
            if (actor == playService.getPlayer()) {
                playService.addMessage(ResourceManager.get(ResConstants.COMMON_REWARD_ITEM
                        , new Object[] {ResourceManager.getObjectName(itemId), count > 1 ? "(" + count + ")" : ""})
                        , MessageType.item);

                // 播放获得物品时的声效
                SoundManager.getInstance().playGetItemSound(itemId, actor.getSpatial().getWorldTranslation());
            }
        }
    }

    @Override
    public boolean removeItem(Actor actor, String itemId, int count) {
        ItemModule module = actor.getModule(ItemModule.class);
        return module != null && module.removeItem(itemId, count);
    }

    @Override
    public ItemData getItem(Actor actor, String itemId) {
        ItemModule module = actor.getModule(ItemModule.class);
        if (module != null) {
            return module.getItem(itemId);
        }
        return null;
    }

    @Override
    public List<ItemData> getItems(Actor actor) {
        ItemModule module = actor.getModule(ItemModule.class);
        if (module != null) {
            return module.getAll();
        }
        return null;
    }
    
    @Override
    public void addItemListener(Actor actor, ItemListener itemListener) {
        ItemModule module = actor.getModule(ItemModule.class);
        if (module != null) {
            module.addItemListener(itemListener);
        }
    }

    @Override
    public boolean removeItemListener(Actor actor, ItemListener itemListener) {
        ItemModule module = actor.getModule(ItemModule.class);
        return module != null && module.removeItemListener(itemListener);
    }
    
    @Override
    public boolean isSellable(ItemData data) {
        // 金币不能卖
        return !data.getId().equals(IdConstants.ITEM_GOLD);
    }

    @Override
    public void syncItemTotal(Actor actor, String itemId, int total) {
        ItemModule module = actor.getModule(ItemModule.class);
        if (module == null)
            return;
        
        ItemData item = module.getItem(itemId);
        if (item == null) {
            if (total <= 0) {
                return;
            }
            module.addItem(itemId, total);
        } else {
            if (total < 0) {
                module.removeItem(itemId, item.getTotal());
            } else {
                item.setTotal(total);
            }
        }
    }

    @Override
    public void useItem(Actor actor, String itemId) {
        ItemData data = getItem(actor, itemId);
        if (data == null)
            return;
        
        boolean canUse = handlerService.canUse(actor, data);
        if (!canUse)
            return;
        
        handlerService.useForce(actor, data);
        
    }


    
}
