/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.module;

import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.ResistData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.DataHandler;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.resist.Resist;

/**
 *
 * @author huliqing
 */
public class ResistModule extends AbstractModule implements DataHandler<ResistData> {

    private SafeArrayList<Resist> resists;
    
    @Override
    public void updateDatas() {
        if (resists != null) {
            for (Resist r : resists.getArray()) {
                r.updateDatas();
            }
        }
    }

    @Override
    public void initialize(Entity entity) {
        super.initialize(entity);
        List<ResistData> rds = entity.getData().getObjectDatas(ResistData.class, new ArrayList<ResistData>());
        for (ResistData rd : rds) {
            addResistInner(rd);
        }
    }

    @Override
    public void cleanup() {
        if (resists != null) {
            for (Resist r : resists.getArray()) {
                r.cleanup();
            }
            resists.clear();
        }
        super.cleanup(); 
    }
    
    @Override
    public Class<ResistData> getHandleType() {
        return ResistData.class;
    }

    @Override
    public boolean handleDataAdd(ResistData data, int amount) {
        // 移除旧的,抗性不允许重复。
        removeResistInner(data);
        
        // 增加新的
        addResistInner(data);
        return true;
    }
    
    @Override
    public boolean handleDataRemove(ResistData data, int amount) {
        return removeResistInner(data);
    }
    
    @Override
    public boolean handleDataUse(ResistData data) {
        return false;
    }
    
    /**
     * 获取对某个状态的抗性值。
     * @param state
     * @return 
     */
    public float getResist(String state) {
        if (resists == null || resists.isEmpty()) {
            return 0;
        }
        // 从所有可以抵抗指定状态的抗性中找出一个抵抗值最大的
        float maxValue = 0;
        for (Resist r : resists.getArray()) {
            if (r.isResistState(state) && r.getResist() > maxValue) {
                maxValue = r.getResist();
            }
        }
        return maxValue;
    }
    
    /**
     * 移除对指定状态的抵抗能力,这个方法会从所有的抗性中移除对指定状态的抵抗能力。
     * @param state 
     */
    public void removeResistState(String state) {
        if (resists == null || resists.isEmpty()) 
            return;
        
        for (Resist r : resists.getArray()) {
            r.removeState(state);
        }
    }
    
    private void addResistInner(ResistData newRd) {
        if (resists == null) {
            resists = new SafeArrayList<Resist>(Resist.class);
        }
        Resist newR = Loader.load(newRd);
        entity.getData().addObjectData(newRd);
        resists.add(newR);
        newR.setEntity(entity);
        newR.initialize();
    }
    
    private boolean removeResistInner(ResistData rdRemove) {
        if (resists == null || resists.isEmpty()) {
            return false;
        }
        for (Resist resist : resists.getArray()) {
            if (resist.getData().getUniqueId() == rdRemove.getUniqueId()) {
                entity.getData().removeObjectData(resist.getData());
                resists.remove(resist);
                resist.cleanup();
                return true;
            }
        }
        return false;
    }

}
