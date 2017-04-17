/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.object.module;

import com.jme3.scene.control.Control;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.logic.Logic;
import name.huliqing.luoying.object.attribute.BooleanAttribute;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 逻辑控制器，控制角色的所有逻辑的运行。 主要处理并监听LogicData这一类物体。
 * @author huliqing
 */
public class LogicModule extends AbstractModule {
    
    // 可以额外绑定一个角色属性(Boolean)来作为逻辑开关， 如果绑定了这个属性，
    // 那么只有enabled和bindEnabledAttribute同为true时逻辑才会运行。
    private String bindEnabledAttribute;
    
    // ---- inner
    private BooleanAttribute enabledAttribute;
    private final SafeArrayList<Logic> logics = new SafeArrayList<Logic>(Logic.class);
    private final Control updateControl = new AdapterControl() {
        @Override
        public void update(float tpf) {logicUpdate(tpf);}
    };
    
    @Override
    public void setData(ModuleData data) {
        super.setData(data);
        bindEnabledAttribute = data.getAsString("bindEnabledAttribute");
    }
    
    @Override
    public void initialize() {
        super.initialize();
        entity.getSpatial().addControl(updateControl);
        
        // 绑定“自动AI”属性
        enabledAttribute = entity.getAttribute(bindEnabledAttribute);
        
        // 载入逻辑
        List<LogicData> logicDatas = entity.getData().getObjectDatas(LogicData.class, new ArrayList<LogicData>());
        if (logicDatas != null) {
            for (LogicData ld : logicDatas) {
                addLogic((Logic) Loader.load(ld));
            }
        }
    }
    
    private void logicUpdate(float tpf) {
        if (!isEnabled()) {
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
        for (Logic logic : logics.getArray()) {
            logic.cleanup();
        }
        logics.clear();
        if (updateControl != null) {
            entity.getSpatial().removeControl(updateControl);
        }
        super.cleanup(); 
    }

    public boolean addLogic(Logic logic) {
        if (logics.contains(logic))
            return false;
        
        logics.add(logic);
        entity.getData().addObjectData(logic.getData());
        
        logic.setActor(entity);
        logic.initialize();
        return true;
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

    @Override
    public boolean handleDataAdd(ObjectData hData, int amount) {
        if (!(hData instanceof LogicData))
            return false;
        
        for (Logic l : logics) {
            if (l.getData().getId().equals(hData.getId())) {
                return false;
            }
        }
        return addLogic((Logic) Loader.load(hData));
    }
    
    @Override
    public boolean handleDataRemove(ObjectData hData, int amount) {
        if (!(hData instanceof LogicData))
            return false;
        
        Logic result = null;
        for (Logic l : logics) {
            if (l.getData() == hData) {
                result = l;
                break;
            }
        }
        if (result != null) {
            return removeLogic(result);
        }
        return false;
    }

    @Override
    public boolean handleDataUse(ObjectData hData) {
        return false;
    }

}
