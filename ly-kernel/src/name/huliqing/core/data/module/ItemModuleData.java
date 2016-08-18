/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data.module;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.ItemData;

/**
 *
 * @author huliqing
 */
@Serializable
public class ItemModuleData extends ModuleData {
 
    // 所有物品
    private List<ItemData> items;

    public List<ItemData> getItems() {
        return items;
    }

    public void setItems(List<ItemData> items) {
        this.items = items;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        if (items != null) {
            OutputCapsule oc = ex.getCapsule(this);
            oc.writeSavableArrayList(new ArrayList<ItemData>(items), "items", null);
        }
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im); 
        InputCapsule ic = im.getCapsule(this);
        items = ic.readSavableArrayList("items", null);
    }
}
