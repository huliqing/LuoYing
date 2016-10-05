/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.save;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 保存快捷方式配置
 * @author huliqing
 */
public class ShortcutsSave implements Savable {
    // 主角玩家的快捷方式
    private ArrayList<ShortcutSave> shortcuts = new ArrayList<ShortcutSave>();

    public ArrayList<ShortcutSave> getShortcuts() {
        return shortcuts;
    }

    public void setShortcuts(ArrayList<ShortcutSave> shortcuts) {
        this.shortcuts = shortcuts;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.writeSavableArrayList(shortcuts, "shortcuts", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        ArrayList<ShortcutSave> tempShortcuts = ic.readSavableArrayList("shortcuts", null);
        shortcuts.clear();
        if (tempShortcuts != null) {
            shortcuts.addAll(tempShortcuts);
        }
    }
    
    
}
