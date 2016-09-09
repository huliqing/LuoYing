/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.talent;

import name.huliqing.core.data.TalentData;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractTalent<T extends TalentData> implements Talent<T> {
    protected T data;
    protected Actor actor;

    /**
     * 天赋的当前等级
     */
    protected int level;
    
    /**
     * 天赋的允许的最高等级
     */
    protected int maxLevel = 10;
    
    /**
     * 标记天赋是否已经初始化
     */
    protected boolean initialized;

    @Override
    public void setData(T data) {
        this.data = data;
        level = data.getAsInteger("level", level);
        maxLevel = data.getAsInteger("maxLevel", maxLevel);
    }
    
    @Override
    public T getData() {
        return data;
    }
    
    /**
     * 更新天赋的实时数据到data中去。
     */
    protected void updateData() {
        data.setAttribute("level", level);
        data.setAttribute("maxLevel", maxLevel);
    }

    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("Talent already initialized! talent=" + this);
        }
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        initialized = false;
    }
    
    @Override
    public void setActor(Actor actor) {
        this.actor = actor;
    }
    
    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        if (level > maxLevel) {
            return;
        }
        
        // 如果天赋已经初始化，则清理后重新载入.
        if (initialized) {
            cleanup();
            this.level = level;
            initialize();
        } else {
            this.level = level;
        }
        
        // 更新一下data数据。
        updateData();
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
        updateData();
    }
    
}
