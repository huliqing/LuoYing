/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import name.huliqing.ly.data.EntityData;
import name.huliqing.ly.data.ModuleData;
import name.huliqing.ly.xml.DataFactory;
import name.huliqing.ly.xml.DataLoader;
import name.huliqing.ly.xml.Proto;

/**
 *
 * @author huliqing
 */
public class EntityDataLoader implements DataLoader<EntityData>{

    @Override
    public void load(Proto proto, EntityData data) {
        
        // 载入模块配置,并根据ModuleOrder进行排序
        String[] moduleArr = proto.getAsArray("modules");
        if (moduleArr != null) {
            data.setModuleDatas(new ArrayList<ModuleData>(moduleArr.length));
            for (String mid : moduleArr) {
                data.getModuleDatas().add((ModuleData) DataFactory.createData(mid));
            }
            Collections.sort(data.getModuleDatas(), new Comparator<ModuleData>() {
                @Override
                public int compare(ModuleData o1, ModuleData o2) {
                    return o1.getModuleOrder() - o2.getModuleOrder();
                }
            });
        }
        
    }
    
}
