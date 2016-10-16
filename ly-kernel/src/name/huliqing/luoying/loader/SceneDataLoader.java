/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.loader;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.data.ObjectData;
import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.data.SceneData;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.xml.DataLoader;

/**
 * 用于载入场景数据
 * @author huliqing
 * @param <T>
 */
public class SceneDataLoader<T extends SceneData> implements DataLoader<T> {

    @Override
    public void load(Proto proto, T store) {        
        // 环境物体
        String[] envIds = proto.getAsArray("entities");
        if (envIds != null && envIds.length > 0) {
            List<EntityData> edStore = store.getEntityDatas();
            if (edStore == null) {
                edStore = new ArrayList<EntityData>(envIds.length);
                store.setEntityDatas(edStore);
            }
            for (String eid : envIds) {
                EntityData ed = DataFactory.createData(eid);
                edStore.add(ed);
            }
        }
    }
    
}
