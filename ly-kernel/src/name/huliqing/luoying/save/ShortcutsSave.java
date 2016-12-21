/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.save;

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
