/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.drop;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.data.DropData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;

/**
 * 掉落组，GroupDrop可以包含多个子掉落设置.
 * @author huliqing
 */
public class GroupDrop extends Drop {
    private static final Logger LOG = Logger.getLogger(GroupDrop.class.getName());
    
    private String[] dropIds;
    private List<Drop> drops;
    
    @Override
    public void setData(DropData data) {
        super.setData(data);
        dropIds = data.getAsArray("drops");
    }
    
    @Override
    public void doDrop(Actor source, Actor target) {
        
        if (drops == null && dropIds != null) {
             if (dropIds != null && dropIds.length > 0) {
                drops = new ArrayList<Drop>(dropIds.length);
                for (String dropId : dropIds) {
                    drops.add((Drop)Loader.load(dropId));
                }
            }
        }
        
        if (drops != null) {
            for (int i = 0; i < drops.size(); i++) {
                LOG.log(Level.INFO, "doDrop, source={0}, target={1}, drop={2}"
                        , new Object[] {source.getData().getId(), target.getData().getId(), drops.get(i).getData().getId()});
                drops.get(i).doDrop(source, target);
            }
        }
    }
    
}
