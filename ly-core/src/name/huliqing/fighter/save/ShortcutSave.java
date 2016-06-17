/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.save;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import java.io.IOException;

/**
 * 用于保存单个的快捷方式的位置信息
 * @author huliqing
 */
public class ShortcutSave implements Savable {

    private String itemId;
    private float x; // x位置
    private float y; // y位置

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
    
   @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(itemId, "itemId", null);
        oc.write(x, "x", 0);
        oc.write(y, "y", 0);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        itemId = ic.readString("itemId", null);
        x = ic.readFloat("x", 0);
        y = ic.readFloat("y", 0);
    }
    
}
