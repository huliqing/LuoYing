/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;

/**
 *
 * @author huliqing
 */
@Serializable
public class DropItem implements Savable {
    // 物品ID
    private String itemId;
    // 掉落数量,默认为1，小于1的话没有意义
    private int count = 1;
    // 掉落机率[0.0~1.0]，如果为0，则永远不可能掉落；如果为1，则还可受全局
    // 机率影响,该机率最终会和全局机率相剩以获得最终机率。默认为1.
    private float factor = 1;

    public DropItem() {}
    
    public DropItem(String itemId) {
        this(itemId, 1, 1);
    }

    public DropItem(String itemId, int count) {
        this(itemId, count, 1);
    }

    public DropItem(String itemId, int count, float factor) {
        this.itemId = itemId;
        this.count = count;
        this.factor = factor;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public float getFactor() {
        return factor;
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
        
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(itemId, "itemId", null);
        oc.write(count, "count", 1);
        oc.write(factor, "factor", 1);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        itemId = ic.readString("itemId", null);
        count = ic.readInt("count", 1);
        factor = ic.readFloat("factor", 1);
    }
}
