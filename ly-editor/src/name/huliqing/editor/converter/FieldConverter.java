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

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Region;
import name.huliqing.editor.edit.JfxAbstractEdit;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 字段转换器
 * @author huliqing
 * @param <E>
 * @param <T>
 */
public abstract class FieldConverter<E extends JfxAbstractEdit, T extends ObjectData> extends AbstractConverter<E, T, DataConverter> {
//    private static final Logger LOG = Logger.getLogger(AbstractFieldConverter.class.getName());
    
    /** 指定要DISABLED的字段，格式:"field1,field2,..." */
    public final static String FEATURE_DISABLED = "disabled"; 
    
    /** 让字段折叠 */
    public final static String FEATURE_COLLAPSED = "collapsed";
    
    // 转换的字段
    protected String field;
    
    protected final TitledPane root = new TitledPane();
    
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public Region getLayout() {
        return root;
    }

    @Override
    public DataConverter getParent() {
        return parent;
    }

    @Override
    public void initialize() {
        super.initialize();
        root.setText(field);
        root.setAnimated(false);
        root.setAlignment(Pos.CENTER_LEFT);
        
        // features
        boolean disabled = featureHelper.getAsBoolean(FEATURE_DISABLED);
        boolean collapsed = featureHelper.getAsBoolean(FEATURE_COLLAPSED);
        
        // Layout and features
        Node layout = createLayout();
        layout.setDisable(disabled);
        root.setExpanded(!collapsed);
        root.setContent(layout);
    }

    @Override
    public void cleanup() { 
        root.setContent(null);
        super.cleanup();
    }
    
    /**
     * 创建Layout布局并返回
     * @return 
     */
    protected abstract Node createLayout();
    
    /**
     * 更新View组件
     */
    public abstract void updateView();

    
}
