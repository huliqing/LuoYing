/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.data.ProtoData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.enums.DataType;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.mvc.dao.ItemDao;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.manager.SoundManager;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.ItemListener;

/**
 *
 * @author huliqing
 */
public class ItemServiceImpl implements ItemService {

    private PlayService playService;
    private ItemDao itemDao;
    private HandlerService handlerService;
    
    @Override
    public void inject() {
        playService = Factory.get(PlayService.class);
        itemDao = Factory.get(ItemDao.class);
        handlerService = Factory.get(HandlerService.class);
    }
    
    @Override
    public void addItem(Actor actor, String itemId, int count) {
        itemDao.addItem(actor, itemId, count);
        
        // 通知侦听器
        List<ItemListener> ils = actor.getItemListeners();
        if (ils != null) {
            for (ItemListener il : ils) {
                il.onItemAdded(actor, itemId, count);
            }
        }
        
        // 提示获得物品,只提示当前场景中的角色
        if (actor == playService.getPlayer()) {
            playService.addMessage(ResourceManager.get(ResConstants.COMMON_REWARD_ITEM
                    , new Object[] {ResourceManager.getObjectName(itemId), count > 1 ? "(" + count + ")" : ""})
                    , MessageType.item);

            // 播放获得物品时的声效
            SoundManager.getInstance().playGetItemSound(itemId, actor.getModel().getWorldTranslation());
        }
    }

    @Override
    public int removeItem(Actor actor, String itemId, int count) {
        int trueRemoved = itemDao.removeItem(actor, itemId, count);
        
        // 通知侦听器
        List<ItemListener> ils = actor.getItemListeners();
        if (ils != null && trueRemoved > 0) {
            for (ItemListener il : ils) {
                il.onItemRemoved(actor, itemId, trueRemoved);
            }
        }
        
        return trueRemoved;
    }

    @Override
    public ProtoData getItem(Actor actor, String itemId) {
        return itemDao.getItemExceptSkill(actor, itemId);
    }

    @Override
    public List<ProtoData> getItems(Actor actor, List<ProtoData> store) {
        return itemDao.getItems(actor, store);
    }

    @Override
    public boolean isSellable(ProtoData data) {
        if (data.getId().equals(IdConstants.ITEM_GOLD))
            return false;
        
        if (data.getDataType() == DataType.skin) {
            return (!((SkinData) data).isUsing());
        }
        
        return true;
    }

    @Override
    public void syncItemTotal(Actor actor, String itemId, int total) {
        ProtoData protoData = getItem(actor, itemId);
        if (protoData == null) {
            if (total <= 0) {
                return;
            }
            itemDao.addItem(actor, itemId, total);
        } else {
            if (total < 0) {
                itemDao.removeItem(actor, itemId, Integer.MAX_VALUE - 1);
            } else {
                protoData.setTotal(total);
            }
        }
    }

    @Override
    public void useItem(Actor actor, String itemId) {
        ProtoData data = getItem(actor, itemId);
        if (data == null)
            return;
        
        boolean canUse = handlerService.canUse(actor, data);
        if (!canUse)
            return;
        
        handlerService.useForce(actor, data);
        
    }
    
    
}
