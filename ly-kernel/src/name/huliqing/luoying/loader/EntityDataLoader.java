/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import name.huliqing.luoying.LuoYingException;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.data.define.CountObject;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.xml.DataLoader;
import name.huliqing.luoying.xml.Proto;

/**
 * @author huliqing
 * @param <T>
 */
public class EntityDataLoader<T extends EntityData> implements DataLoader<T>{

    @Override
    public void load(Proto proto, T data) {
        
        // 载入模块配置,并根据ModuleOrder进行排序
        String[] moduleArr = proto.getAsArray("modules");
        if (moduleArr != null) {
            data.setModuleDatas(new ArrayList<ModuleData>(moduleArr.length));
            for (String mid : moduleArr) {
                try {
                    data.getModuleDatas().add((ModuleData) DataFactory.createData(mid));
                } catch (Exception e) {
                    throw new LuoYingException("Could not load moduleData, moduleId=" + mid + ", entityId=" + proto.getId(), e);
                }
            }
            Collections.sort(data.getModuleDatas(), new Comparator<ModuleData>() {
                @Override
                public int compare(ModuleData o1, ModuleData o2) {
                    return o1.getModuleOrder() - o2.getModuleOrder();
                }
            });
        }
        
        // 载入数据， 格式: objectDatas="objectData1|amount,objectData2|amount,..."
        String[] objectArr = proto.getAsArray("objectDatas");
        if (objectArr != null) {
            data.setObjectDatas(new ArrayList<ObjectData>(objectArr.length));
            for (String oid : objectArr) {
                String[] tempArr = oid.split("\\|");
                ObjectData od = Loader.loadData(tempArr[0]);
                // 如果是可量化的物体，并且设置了数量。
                if (od instanceof CountObject && tempArr.length > 1) {
                    try {
                        ((CountObject) od).setTotal(Integer.parseInt(tempArr[1]));
                    } catch (NumberFormatException nfe) {
                        throw new LuoYingException("Could not set amount for objectData, "
                                + "please check the format of property of \"objectDatas\", entityId=" + data.getId(), nfe);
                    }
                }
                data.getObjectDatas().add(od);
            }
        }
        
    }
    
}
