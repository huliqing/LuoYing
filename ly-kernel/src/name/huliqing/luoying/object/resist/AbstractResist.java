/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.resist;

import name.huliqing.luoying.data.ResistData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.utils.MathUtils;

/**
 *
 * @author huliqing
 */
public abstract class AbstractResist implements Resist {
    
    protected ResistData data;
    protected boolean initialized;
    protected Entity entity;

    @Override
    public void setData(ResistData data) {
        this.data = data;
    }
    
    @Override
    public ResistData getData() {
        return data;
    }
    
    @Override
    public void initialize() {
        if (initialized) {
            throw new UnsupportedOperationException("Resist already initialized! resist=" + data.getId());
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
    public float getResist() {
        return data.getValue();
    }
    
    @Override
    public void addResist(float resist) {
        data.setValue(MathUtils.clamp(data.getValue() + resist, 0.0f, 1.0f));
    }
    
    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
    
}
