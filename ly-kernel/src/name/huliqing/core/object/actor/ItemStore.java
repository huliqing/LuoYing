/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actor;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import name.huliqing.core.data.ObjectData;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.LY;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.data.SkinData;

/**
 * 保存角色的各种杂物信息等等。
 * @author huliqing
 */
@Serializable
public class ItemStore implements Savable {
    
    // 所有物品
    private ArrayList<ObjectData> items = new ArrayList<ObjectData>();
    // 最后一次添加物品，删除物品的时间
    private long lastModifyTime;

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.writeSavableArrayList(items, "items", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        ArrayList<ObjectData> temp = ic.readSavableArrayList("items", null);
        items.clear();
        if (temp != null) {
            items.addAll(temp);
        }
    }
    
    public ItemStore() {}
    
    /**
     * 获取所有物品列表,注意: 不要手动移除列表中的物品.移除物品应该使用：
     * {@link #removeItem(name.huliqing.fighter.data.ProtoData) }方法
     * @return 
     */
    public List<ObjectData> getAll() {
        return items;
    }
    
    /**
     * 获取除了“装备”和“武器”之外的所有物品
     * @param store
     * @return 
     */
    public List<ObjectData> getOthers(List<ObjectData> store) {
        if (store == null) {
            store = new ArrayList<ObjectData>();
        }
        for (ObjectData od : items) {
            if (od instanceof SkinData) {
                continue;
            }
            store.add(od);
        }
        return store;
    }

    /**
     * @deprecated 使用ActorDao.getItem代替
     * @param itemId
     * @return 
     */
    public ObjectData getItem(String itemId) {
        return findObjectData(itemId);
    }
    
    /**
     * 删除物品
     * @param itemId
     * @param amount 
     */
    public boolean removeItem(String itemId, int amount) {
        if (itemId == null || amount <= 0) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "ItemId could not be null, and amount must more than 0!");
            return false;
        }
            
        ObjectData od = findObjectData(itemId);
        if (od != null) {
            od.increaseTotal(-1 * amount);
            if (od.getTotal() <= 0) {
                items.remove(od);
            }
            lastModifyTime = LY.getGameTime();
            return true;
        }
        return false;
    }
    
    /**
     * 清理删除所有的物品
     */
    public void clearItems() {
        items.clear();
        lastModifyTime = LY.getGameTime();
    }
    
    /**
     * 获得当前金币数量
     * @return 
     */
    public int getTotalGold() {
        for (ObjectData od : items) {
            if (od.getId().equals(IdConstants.ITEM_GOLD)) {
                return od.getTotal();
            }
        }
        return 0;
    }
    
    /**
     * 通过物品ID查找物品.
     * @param itemId
     * @return 
     */
    private ObjectData findObjectData(String itemId) {
        for (ObjectData od : items) {
            if (od.getId().equals(itemId)) {
                return od;
            }
        }
        return null;
    }

    public boolean addItem(ObjectData item, int amount) {
        if (amount <= 0) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Amount must more than 0");
            return false;
        }
        ObjectData od = findObjectData(item.getId());
        // 如果包裹中不存在物品，则创建一个新的,注意：创建的时候要判断物品是否存在。
        if (od == null) {
            od = item;
            od.setTotal(amount);
            items.add(od);
        } else {
            od.increaseTotal(amount);
        }
        lastModifyTime = LY.getGameTime();
        return true;
    }

    /**
     * 获取最近一次添加物品或减少物品的时间
     * @return 
     */
    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
    
    
}
