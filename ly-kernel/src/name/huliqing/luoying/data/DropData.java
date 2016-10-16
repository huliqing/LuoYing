/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huliqing
 */
@Serializable
public class DropData extends ObjectData {
    
    // 必须掉落的物品列表。
    private List<DropItem> baseItems;

    // 可随机掉落的物品列表
    private List<DropItem> randomItems;

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        if (baseItems != null)
            oc.writeSavableArrayList(new ArrayList<DropItem>(baseItems), "baseItems", null);
        if (randomItems != null)
            oc.writeSavableArrayList(new ArrayList<DropItem>(randomItems), "randomItems", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        baseItems = ic.readSavableArrayList("baseItems", null);
        randomItems = ic.readSavableArrayList("randomItems", null);
    }
    
    public DropData() {}

    public List<DropItem> getBaseItems() {
        return baseItems;
    }

    public void setBaseItems(List<DropItem> baseItems) {
        this.baseItems = baseItems;
    }

    public List<DropItem> getRandomItems() {
        return randomItems;
    }

    public void setRandomItems(List<DropItem> randomItems) {
        this.randomItems = randomItems;
    }
  
    /**
     * 设置必然掉落的物品ID列表，默认的掉落数量都为1。
     * @param objectIds 
     */
    public void setBaseDrop(String... objectIds) {
        if (baseItems == null) {
            baseItems = new ArrayList<DropItem>(objectIds.length);
        }
        baseItems.clear();
        for (String id : objectIds) {
            DropItem di = new DropItem(id, 1, 1);
            baseItems.add(di);
        }
    }
    
    /**
     * 添加“必然”掉落的物品id,可指定掉落数量
     * @param id 物品id
     * @param count 掉落数量
     */
    public void addBaseDrop(String id, int count) {
        if (baseItems == null) {
            baseItems = new ArrayList<DropItem>(1);
        }
        DropItem di = new DropItem(id, count, 1);
        baseItems.add(di);
    }
  
}
