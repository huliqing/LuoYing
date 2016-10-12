/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.loader;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.data.EntityData;
import name.huliqing.ly.data.ObjectData;
import name.huliqing.ly.xml.Proto;
import name.huliqing.ly.data.SceneData;
import name.huliqing.ly.xml.DataFactory;
import name.huliqing.ly.xml.DataLoader;

/**
 * 用于载入场景数据
 * @author huliqing
 * @param <T>
 */
public class SceneDataLoader<T extends SceneData> implements DataLoader<T> {

    @Override
    public void load(Proto proto, T store) {        
        // 环境物体
        String[] envIds = proto.getAsArray("envs");
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
