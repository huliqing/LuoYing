/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

import name.huliqing.luoying.data.AttributeData;
import name.huliqing.luoying.object.module.AttributeModule;

/**
 *
 * @author huliqing 
 */
public abstract class AbstractAttribute implements Attribute {

    protected AttributeData data;
    protected boolean initialized;
    
    @Override
    public void setData(AttributeData data) {
        this.data = data;
    }

    @Override
    public AttributeData getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
    }

    @Override
    public String getId() {
        return data.getId();
    }

    @Override
    public String getName() {
        return data.getName();
    }
    
    @Override
    public void initialize(AttributeModule module) {
        if (initialized) {
            throw new IllegalStateException("Attribute already initialized! attributeId=" + this.getId());
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
    public String toString() {
        return "attributeId=" + getId() + ", attributeName=" + getName();
    }
    
}
