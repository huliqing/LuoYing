/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.magic;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.data.EffectData;
import name.huliqing.ly.data.MagicData;
import name.huliqing.ly.object.ControlAdapter;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.effect.Effect;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.object.entity.ModelEntity;
import name.huliqing.ly.object.hitchecker.HitChecker;
import name.huliqing.ly.object.scene.Scene;

/**
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractMagic<T extends MagicData> extends ModelEntity<T> implements Magic<T> {
    
    protected float useTime = 1.0f;
    
    //--------
    // 格式:effectId|timePoint,effectId|timePoint,effectId|timePoint...
    protected final Node magicRoot = new Node("MagicRoot");
    
    // 魔法的施放者，有可能为null.
    protected Entity source;
    
    // 魔法针对的主目标,如果魔法没有特定的目标，则有可能为null
    protected Entity[] targets;
    
    protected HitChecker hitChecker;
    
    // 保持对效果的引用，在结束时清理
    protected List<Effect> effects;
    
    protected float timeUsed;
     
    public AbstractMagic() {
        magicRoot.addControl(new ControlAdapter() {
            @Override
            public void update(float tpf) {
                magicUpdate(tpf);
            }
        });
    }
    
    @Override
    public void setData(T data) {
        super.setData(data);
        useTime = data.getAsFloat("useTime", useTime);
        timeUsed = data.getAsFloat("timeUsed", timeUsed);
    }
    
    @Override
    public T getData() {
        return super.getData(); 
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        // 有可能发生状态变化的字段才需要更新到Data.
        if (effects != null) {
            for (Effect effect : effects) {
                effect.updateDatas();
            }
        }
        data.setAttribute("timeUsed", timeUsed);
    }
    
    @Override
    public void initialize(Scene scene) {
        super.initialize(scene);
        
        if (data.getEffectDatas() != null) {
            if (effects == null) {
                effects = new ArrayList<Effect>(data.getEffectDatas().length);
            }
            effects.clear();
            for (EffectData ed : data.getEffectDatas()) {
                Effect effect = Loader.load(ed);
                magicRoot.attachChild(effect);
                effects.add(effect);
            }
        }
    }
    
    public void magicUpdate(float tpf) {
        timeUsed += tpf;
        if (timeUsed > useTime) {
            removeFromScene();
        }
        // 子类逻辑
    }
    
    @Override
    public void cleanup() {
        if (effects != null) {
            for (Effect e : effects) {
                e.requestEnd();
            }
        }
        timeUsed = 0;
        source = null;
        targets = null;
        super.cleanup();
    }
    
    @Override
    protected Spatial loadModel() {
        return magicRoot;
    }

    @Override
    public void setSource(Entity source) {
        this.source = source;
    }

    @Override
    public void setTargets(Entity... targets) {
        this.targets = targets;
    }

    @Override
    public boolean canHit(Entity entity) {
        if (source != null) {
            return hitChecker.canHit(source, entity);
        }
        return hitChecker.canHit(this, entity);
    }
}
