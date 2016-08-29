/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.drop;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.DropData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;

/**
 * 掉落组，GroupDrop可以包含多个子掉落设置.
 * @author huliqing
 */
public class GroupDrop extends Drop {
    
    private List<Drop> drops;
    
    @Override
    public void setData(DropData data) {
        super.setData(data);
        String[] dropArr = data.getAsArray("drops");
        if (dropArr != null && dropArr.length > 0) {
            drops = new ArrayList<Drop>(dropArr.length);
            for (String dropId : dropArr) {
                drops.add((Drop)Loader.load(dropId));
            }
        }
    }
    
    @Override
    public void doDrop(Actor source, Actor target) {
        if (drops != null && !drops.isEmpty()) {
            for (int i = 0; i < drops.size(); i++) {
                drops.get(i).doDrop(source, target);
            }
        }
    }
    
}
