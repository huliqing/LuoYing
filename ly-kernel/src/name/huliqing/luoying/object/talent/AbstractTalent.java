/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.talent;

import name.huliqing.luoying.data.TalentData;
import name.huliqing.luoying.object.entity.Entity;

/**
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractTalent<T extends TalentData> implements Talent<T> {
    protected T data;
    protected Entity actor;

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
        level = data.getLevel();
        maxLevel = data.getMaxLevel();
    }
    
    @Override
    public T getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        data.setLevel(level);
        data.setMaxLevel(maxLevel);
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
    public void setActor(Entity actor) {
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
        data.setLevel(level);
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
        data.setMaxLevel(maxLevel);
    }
    
}
