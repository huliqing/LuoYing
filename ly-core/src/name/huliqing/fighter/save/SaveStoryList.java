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
