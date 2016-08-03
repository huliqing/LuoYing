/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.dao;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.LY;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.xml.ProtoData;
import name.huliqing.core.xml.DataFactory;

/**
 * @author huliqing
 */
public class ItemDaoImpl implements ItemDao {

    @Override
    public void inject() {
        // 
    }
    
    @Override
    public boolean addItem(Actor actor, String itemId, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Could not addItem. \"amount\" "
                    + "could not less than 0. actor=" + actor.getData().getId() 
                    + ", itemId=" + itemId + ", amount=" + amount);
        }
        ProtoData item = DataFactory.createData(itemId);
        if (item == null) {
            return false;
        }
        
        List<ProtoData> items = getAll(actor);
        ProtoData od = findItem(actor, itemId);
        // 如果包裹中不存在物品，则创建一个新的,注意：创建的时候要判断物品是否存在。
        if (od == null) {
            od = item;
            od.setTotal(amount);
            items.add(od);
        } else {
            od.increaseTotal(amount);
        }
        actor.getData().getItemStore().setLastModifyTime(LY.getGameTime());
        return true;
    }

    @Override
    public int removeItem(Actor actor, String itemId, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Could not removeItem. \"amount\" "
                    + "could not less than 0. actor=" + actor.getData().getId() 
                    + ", itemId=" + itemId + ", amount=" + amount);
        }
        List<ProtoData> items = getAll(actor);
        ProtoData od = findItem(actor, itemId);
        int trueRemoved = 0;
        if (od != null) {
            trueRemoved = amount > od.getTotal() ? od.getTotal() : amount;
            
            od.increaseTotal(-1 * trueRemoved);
            if (od.getTotal() <= 0) {
                items.remove(od);
            }
            actor.getData().getItemStore().setLastModifyTime(LY.getGameTime());
            return trueRemoved;
        }
        return 0;
    }
    
    @Override
    public ProtoData getItemExceptSkill(Actor actor, String objectId) {
        List<ProtoData> items = getAll(actor);
        for (ProtoData od : items) {
            if (od.getId().equals(objectId)) {
                return od;
            }
        }
        return null;
    }
    
    private List<ProtoData> getAll(Actor actor) {
        List<ProtoData> items = actor.getData().getItemStore().getAll();
        return items;
    }
    
    @Override
    public List<ProtoData> getItems(Actor actor, List<ProtoData> store) {
        if (store == null) {
            store = new ArrayList<ProtoData>();
        }
        List<ProtoData> items = getAll(actor);
        for (ProtoData pd : items) {
            store.add(pd);
        }
        return store;
    }

    private ProtoData findItem(Actor actor, String itemId) {
        List<ProtoData> items = getAll(actor);
        for (ProtoData od : items) {
            if (od.getId().equals(itemId)) {
                return od;
            }
        }
        return null;
    }
 

}
