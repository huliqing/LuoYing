/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.dao;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.LY;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.ObjectData;
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
        ObjectData item = DataFactory.createData(itemId);
        if (item == null) {
            return false;
        }
        
        List<ObjectData> items = getAll(actor);
        ObjectData od = findItem(actor, itemId);
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
        List<ObjectData> items = getAll(actor);
        ObjectData od = findItem(actor, itemId);
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
    public ObjectData getItemExceptSkill(Actor actor, String objectId) {
        List<ObjectData> items = getAll(actor);
        for (ObjectData od : items) {
            if (od.getId().equals(objectId)) {
                return od;
            }
        }
        return null;
    }
    
    private List<ObjectData> getAll(Actor actor) {
        List<ObjectData> items = actor.getData().getItemStore().getAll();
        return items;
    }
    
    @Override
    public List<ObjectData> getItems(Actor actor, List<ObjectData> store) {
        if (store == null) {
            store = new ArrayList<ObjectData>();
        }
        List<ObjectData> items = getAll(actor);
        for (ObjectData pd : items) {
            store.add(pd);
        }
        return store;
    }

    private ObjectData findItem(Actor actor, String itemId) {
        List<ObjectData> items = getAll(actor);
        for (ObjectData od : items) {
            if (od.getId().equals(itemId)) {
                return od;
            }
        }
        return null;
    }
 

}
