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
package name.huliqing.luoying.object.magic;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.object.ControlAdapter;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.effect.Effect;
import name.huliqing.luoying.object.el.STBooleanEl;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.ModelEntity;
import name.huliqing.luoying.object.scene.Scene;

/**
 * @author huliqing
 */
public abstract class AbstractMagic extends ModelEntity implements Magic {
    private final ElService elService = Factory.get(ElService.class);
    
    private long source;
    private long[] targets;
    
    protected float useTime = 1.0f;
    protected float timeUsed;
    protected String[] effects;
    protected STBooleanEl hitCheckEl;
    
    //--------
    // 格式:effectId|timePoint,effectId|timePoint,effectId|timePoint...
    protected final Node magicRoot = new Node("MagicRoot");
    
    /**
     * 魔法的施放者，有可能为null.
     */
    protected Entity sourceEntity;
    
    /**
     * 魔法针对的特定目标,如果没有特定的目标，则有可能为null
     */
    protected List<Entity> targetEntities;
    
    // 保持对效果的引用，在结束时清理
    protected List<Effect> tempEffects;
    
    public AbstractMagic() {
        magicRoot.addControl(new ControlAdapter() {
            @Override
            public void update(float tpf) {
                updateMaigc(tpf);
            }
        });
    }
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        source = data.getAsLong("source");
        targets = data.getAsLongArray("targets");
        useTime = data.getAsFloat("useTime", useTime);
        timeUsed = data.getAsFloat("timeUsed", timeUsed);
        effects = data.getAsArray("effects");
        hitCheckEl = elService.createSTBooleanEl(data.getAsString("hitCheckEl", "#{true}"));
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        
        data.setAttribute("source", source);
        data.setAttribute("targets", targets);
        
        // 有可能发生状态变化的字段才需要更新到Data.
        if (tempEffects != null) {
            for (Effect effect : tempEffects) {
                effect.updateDatas();
            }
        }
        data.setAttribute("timeUsed", timeUsed);
    }
    
    @Override
    public void initEntity() {
        super.initEntity();
        if (effects != null) {
            if (tempEffects == null) {
                tempEffects = new ArrayList<Effect>(effects.length);
            }
            for (String eid : effects) {
                Effect effect = Loader.load(eid);
                effect.initialize();
                magicRoot.attachChild(effect);
                tempEffects.add(effect);
            }
        }
    }

    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        if (source > 0) {
            sourceEntity = scene.getEntity(source);
        }
        if (targets != null && targets.length > 0) {
            if (targetEntities == null) {
                targetEntities = new ArrayList<Entity>(targets.length);
            }
            for (long tid : targets) {
                Entity target = scene.getEntity(tid);
                if (target != null) {
                    targetEntities.add(target);
                }
            }
        }
    }
    
    private void updateMaigc(float tpf) {
        timeUsed += tpf;
        
        // 更新魔法逻辑
        updateMagicLogic(tpf);
        
        // 时间到则将魔法节点从场景清理出，注：这会导致scene被清理为null.
        // 不应该在这代码后面再给子类实现逻辑，这容易引起NPE.
        if (timeUsed > useTime) {
            removeFromScene();
        }
    }
    
    /**
     * 实现魔法逻辑
     * @param tpf 
     */
    public void updateMagicLogic(float tpf) {
        // 子类逻辑
    }
    
    @Override
    public void cleanup() {
        if (tempEffects != null) {
            for (Effect e : tempEffects) {
                e.requestEnd();
            }
            tempEffects.clear();
        }
        if (targetEntities != null) {
            targetEntities.clear();
        }
        timeUsed = 0;
        sourceEntity = null;
        super.cleanup();
    }
    
    @Override
    protected Spatial loadModel() {
        return magicRoot;
    }

}
