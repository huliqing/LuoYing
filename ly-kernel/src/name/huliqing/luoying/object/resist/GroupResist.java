/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.resist;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ResistData;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.object.Loader;

/**
 * GroupResist只用于打包
 * @author huliqing
 */
public class GroupResist extends AbstractResist {
    private final EntityService entityService = Factory.get(EntityService.class);

    private boolean applied;
    private long[] appliedIds;

    @Override
    public void setData(ResistData data) {
        super.setData(data);
        applied = data.getAsBoolean("_applied", applied);
        appliedIds = data.getAsLongArray("_appliedIds");
    }
    
    @Override
    public void updateDatas() {
        data.setAttribute("_applied", applied);
        data.setAttribute("_appliedIds", appliedIds);
    }
    
    @Override
    public void initialize() {
        super.initialize(); 
        if (applied) {
            return;
        }
        
        // 格式："resist1|value, resist2|value,..."
        String[] tempResists = data.getAsArray("resists");
        if (tempResists != null && tempResists.length > 0) {
            appliedIds = new long[tempResists.length];
            for (int i = 0; i < tempResists.length; i++) {
                String[] tempArr = tempResists[i].split("\\|");
                ResistData resistData = Loader.loadData(tempArr[0]);
                if (tempArr.length > 1) {
                    resistData.setValue(Float.parseFloat(tempArr[1]));
                }
                entityService.addObjectData(entity, resistData, 1);
                // 记住通过GroupResist添加的抗性,在退出时要清理掉。
                appliedIds[i] = resistData.getUniqueId();
            }
        }
        applied = true;
    }

    @Override
    public void cleanup() {
        if (applied) {
            if (appliedIds != null) {
                for (long resistId : appliedIds) {
                    entityService.removeObjectData(entity, resistId, 1);
                }
                appliedIds = null;
            }
            applied = false;
        }
        super.cleanup();
    }
    
    @Override
    public float getResist() {
        return 0;
    }

    @Override
    public void addResist(float resist) {
        // ignore
    }

    @Override
    public boolean isResistState(String state) {
        return false;
    }

    @Override
    public void addState(String state) {
        // ignore
    }

    @Override
    public boolean removeState(String state) {
        return false; // ignore
    }
    
}
