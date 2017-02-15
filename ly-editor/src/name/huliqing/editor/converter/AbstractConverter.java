/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import java.util.Map;
import name.huliqing.editor.converter.define.Feature;
import name.huliqing.editor.edit.JfxAbstractEdit;
import name.huliqing.luoying.xml.ObjectData;

/**
 * @author huliqing
 * @param <E>
 * @param <T>
 * @param <C>  父转换器
 */
public abstract class AbstractConverter<E extends JfxAbstractEdit, T extends ObjectData, C extends Converter> implements Converter<T, C> {
    
    protected C parent;
    protected FeatureHelper featureHelper;
     
    protected boolean initialized;
    
    /** JFX编辑器 */
    protected E jfxEdit;
    
    /** 转换的数据类型 */
    protected T data;

    @Override
    public void setData(T data) {
        this.data = data;
    }
    
    @Override
    public void setFeatures(Map<String, Feature> features) {
        this.featureHelper = new FeatureHelper(features);
    }

    @Override
    public void setParent(C parent) {
        this.parent = parent;
    }
    
    public void setEdit(E edit) {
        this.jfxEdit = edit;
    }

    @Override
    public void notifyChanged() {
        if (parent != null) {
            parent.notifyChanged();
        }
    }
    
    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException();
        }
        this.initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        initialized = false;
    }
    
    
}
