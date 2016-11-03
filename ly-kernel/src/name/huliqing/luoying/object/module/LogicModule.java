/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.module;

import com.jme3.scene.control.Control;
import com.jme3.util.SafeArrayList;
import java.util.List;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.logic.Logic;
import name.huliqing.luoying.object.attribute.BooleanAttribute;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 逻辑控制器，控制角色的所有逻辑的运行。
 * @author huliqing
 */
public class LogicModule extends AbstractModule {
    private Control updateControl;
    private final SafeArrayList<Logic> logics = new SafeArrayList<Logic>(Logic.class);
    
    // 控制逻辑的内部开关,这个参数可以作为”总开关“
    private boolean enabled = true;
    
    // 可以额外绑定一个角色属性(Boolean)来作为逻辑开关， 如果绑定了这个属性，
    // 那么只有enabled和bindEnabledAttribute同为true时逻辑才会运行。
    private String bindEnabledAttribute;
    
    // ---- inner
    private BooleanAttribute enabledAttribute;
    
    @Override
    public void setData(ModuleData data) {
        super.setData(data);
        enabled = data.getAsBoolean("enabled", enabled);
        bindEnabledAttribute = data.getAsString("bindEnabledAttribute");
    }
    
    @Override
    public void updateDatas() {
        data.setAttribute("enabled", enabled);
    }

    /**
     * 判断是否打开所有逻辑功能
     * @return 
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 设置是否打开所有逻辑功能
     * @param enabled 
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    @Override
    public void initialize(Entity entity) {
        super.initialize(entity);
        updateControl = new AdapterControl() {
            @Override
            public void update(float tpf) {logicUpdate(tpf);}
        };
        this.entity.getSpatial().addControl(updateControl);
        
        // 绑定“自动AI”属性
        enabledAttribute = entity.getAttributeManager().getAttribute(bindEnabledAttribute);
        
        // 载入逻辑
        List<LogicData> logicDatas = entity.getData().getObjectDatas(LogicData.class, null);
        if (logicDatas != null) {
            for (LogicData ld : logicDatas) {
                addLogic((Logic) Loader.load(ld));
            }
        }
    }
    
    private void logicUpdate(float tpf) {
        if (!enabled) {
            return;
        }
        
        if (enabledAttribute != null && !enabledAttribute.getValue()) {
            return;
        }
        
        for (Logic logic : logics.getArray()) {
            logic.update(tpf);
        }
    }

    @Override
    public void cleanup() {
        for (Logic logic : logics) {
            logic.cleanup();
        }
        logics.clear();
        if (updateControl != null) {
            entity.getSpatial().removeControl(updateControl);
        }
        super.cleanup(); 
    }

    public void addLogic(Logic logic) {
        if (logics.contains(logic))
            return;
        
        logics.add(logic);
        entity.getData().addObjectData(logic.getData());
        
        logic.setActor(entity);
        logic.initialize();
    }

    public boolean removeLogic(Logic logic) {
        if (!logics.contains(logic))
            return false;
        
        entity.getData().removeObjectData(logic.getData()); 
        logics.remove(logic);
        logic.cleanup();
        return true;
    }
    
    public List<Logic> getLogics() {
        return logics;
    }
}
