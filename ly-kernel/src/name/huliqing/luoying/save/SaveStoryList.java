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
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author huliqing
 */
public class SaveStoryList implements Savable {

    private final ArrayList<String> list = new ArrayList<String>();

    public ArrayList<String> getList() {
        return list;
    }
    
    public void addSaveName(String saveName) {
        // 如果存档已经存在，则先移除，然后再加到前面。以保存最新存档排在最前面
        if (list.contains(saveName)) {
            list.remove(saveName);
        }
        list.add(0, saveName);
    }
    
    public void removeSaveName(String saveName) {
        list.remove(saveName);
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(list.toArray(new String[]{}), "saveName", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        List<String> temp = Arrays.asList(ic.readStringArray("saveName", null));
        list.clear();
        if (temp != null) {
            list.addAll(temp);
        }
    }
    
}
