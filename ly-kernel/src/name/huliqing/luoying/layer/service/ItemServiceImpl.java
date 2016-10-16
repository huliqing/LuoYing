/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.constants.ResConstants;
import name.huliqing.luoying.enums.MessageType;
import name.huliqing.luoying.manager.ResourceManager;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.item.Item;
import name.huliqing.luoying.object.module.ItemListener;
import name.huliqing.luoying.object.module.ItemModule;
import name.huliqing.luoying.object.sound.SoundManager;

/**
 *
 * @author huliqing
 */
public class ItemServiceImpl implements ItemService {
    private PlayService playService;
    
    @Override
    public void inject() {
        playService = Factory.get(PlayService.class);
    }
    
    @Override
    public void addItem(Entity actor, String itemId, int count) {
        ItemModule module = actor.getEntityModule().getModule(ItemModule.class);
        if (module != null) {
            module.addItem(itemId, count);
            
            // xxx 重构
//            // 提示获得物品,只提示当前场景中的角色
//            if (actor == playService.getPlayer()) {
//                playService.addMessage(ResourceManager.get(ResConstants.COMMON_REWARD_ITEM
//                        , new Object[] {ResourceManager.getObjectName(itemId), count > 1 ? "(" + count + ")" : ""})
//                        , MessageType.item);
//
//                // 播放获得物品时的声效
//                if (itemId.equals(IdConstants.ITEM_GOLD)) {
//                    SoundManager.getInstance().playSound(IdConstants.SOUND_GET_COIN, actor.getSpatial().getWorldTranslation());
//                } else {
//                    SoundManager.getInstance().playSound(IdConstants.SOUND_GET_ITEM, actor.getSpatial().getWorldTranslation());
//                }
//            }

        }
    }

    @Override
    public void removeItem(Entity actor, String itemId, int count) {
        ItemModule module = actor.getEntityModule().getModule(ItemModule.class);
        if (module != null) {
            module.removeItem(itemId, count);
        }
    }

    @Override
    public Item getItem(Entity actor, String itemId) {
        ItemModule module = actor.getEntityModule().getModule(ItemModule.class);
        if (module != null) {
            return module.getItem(itemId);
        }
        return null;
    }

    @Override
    public List<Item> getItems(Entity actor) {
        ItemModule module = actor.getEntityModule().getModule(ItemModule.class);
        if (module != null) {
            return module.getItems();
        }
        return null;
    }
    
    @Override
    public void addItemListener(Entity actor, ItemListener itemListener) {
        ItemModule module = actor.getEntityModule().getModule(ItemModule.class);
        if (module != null) {
            module.addItemListener(itemListener);
        }
    }

    @Override
    public boolean removeItemListener(Entity actor, ItemListener itemListener) {
        ItemModule module = actor.getEntityModule().getModule(ItemModule.class);
        return module != null && module.removeItemListener(itemListener);
    }

    /**
     * @deprecated 不再使用
     * @param actor
     * @param itemId
     * @param total 
     */
    @Override
    public void syncItemTotal(Entity actor, String itemId, int total) {
        ItemModule module = actor.getEntityModule().getModule(ItemModule.class);
        if (module == null)
            return;
        
        Item item = module.getItem(itemId);
        if (item == null) {
            if (total <= 0) {
                return;
            }
            module.addItem(itemId, total);
        } else {
            if (total < 0) {
                module.removeItem(itemId, item.getData().getTotal());
            } else {
                item.getData().setTotal(total);
            }
        }
    }

    @Override
    public void useItem(Entity actor, String itemId) {
        ItemModule module = actor.getEntityModule().getModule(ItemModule.class);
        if (module != null) {
            Item item = module.getItem(itemId);
            if (item != null) {
                module.useItem(item);
            }
        }
    }


    
}
