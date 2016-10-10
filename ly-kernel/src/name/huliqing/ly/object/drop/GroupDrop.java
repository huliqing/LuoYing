/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.drop;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.data.DropData;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.entity.Entity;

/**
 * 掉落组，GroupDrop可以包含多个子掉落设置.
 * @author huliqing
 */
public class GroupDrop extends AbstractDrop {
//    private static final Logger LOG = Logger.getLogger(GroupDrop.class.getName());
    
    private String[] dropIds;
    private List<Drop> drops;
    
    @Override
    public void setData(DropData data) {
        super.setData(data);
        dropIds = data.getAsArray("drops");
    }
    
    @Override
    public boolean doDrop(Entity source, Entity target) {
        if (drops == null && dropIds != null) {
             if (dropIds != null && dropIds.length > 0) {
                drops = new ArrayList<Drop>(dropIds.length);
                for (String dropId : dropIds) {
                    drops.add((Drop)Loader.load(dropId));
                }
            }
        }
        boolean hasDrop = false;
        if (drops != null) {
            for (int i = 0; i < drops.size(); i++) {
                drops.get(i).doDrop(source, target);
                hasDrop = true;
            }
        }
        if (hasDrop) {
            playDropSounds(source);
        }
        return hasDrop;
    }
    
}
