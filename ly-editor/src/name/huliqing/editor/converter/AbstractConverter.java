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
    public T getData() {
        return data;
    }
    
    @Override
    public void setFeatures(Map<String, Feature> features) {
        this.featureHelper = new FeatureHelper(features);
    }

    @Override
    public void setParent(C parent) {
        this.parent = parent;
    }

    @Override
    public C getParent() {
        return parent;
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
