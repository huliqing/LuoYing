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
package name.huliqing.editor.converter.field;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import name.huliqing.editor.component.ComponentDefine;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.converter.SimpleFieldConverter;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.editor.edit.scene.JfxSceneEdit;
import name.huliqing.editor.manager.ComponentManager;
import name.huliqing.editor.manager.ConverterManager;
import name.huliqing.editor.ui.ComponentSearch;
import name.huliqing.editor.ui.utils.JfxUtils;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 用于转换Entity的objectDatas字段,可以指定DataType和ComponentType.
 * 所有包含在EntityData的objectDatas字段中的列表数据都需要继承自这个类, 例如：SkillData,ChannelData,EffectData,LogicDatas...等等
 * @author huliqing
 */
public class EntityObjectDatasConverter extends SimpleFieldConverter<JfxSceneEdit, EntityData>{

    private static final Logger LOG = Logger.getLogger(EntityObjectDatasConverter.class.getName());
    
    private final static String FEATURE_DATA_TYPE = "dataType";
    private final static String FEATURE_COMPONENT_TYPE = "componentType";
    private Class<? extends ObjectData> dataType;
    private String componentType;

    protected ComponentSearch<ComponentDefine> componentList = new ComponentSearch();
    
    protected final VBox layout = new VBox();
    protected final ToolBar toolbar = new ToolBar();
    protected final ListView<ObjectData> listView = new ListView();
    
    private DataConverter dataConverter;
    
    public EntityObjectDatasConverter() {
        Button add = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_ADD));
        Button remove = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_SUBTRACT));
        
        add.setOnAction(e -> {
            componentList.show(add, -10, -10);
        });
        remove.setOnAction(e -> {
            List<ObjectData> eds = listView.getSelectionModel().getSelectedItems();
            if (eds == null || eds.isEmpty())
                return;
            
            Jfx.runOnJme(() -> {
                ObjectData od = listView.getSelectionModel().getSelectedItem();
                if (od != null) {
                    ObjectDataRemovedUndoRedo ur = new ObjectDataRemovedUndoRedo(od);
                    ur.redo();
                    jfxEdit.getJmeEdit().addUndoRedo(ur);
                }
            });
        });
        
        componentList.getListView().setOnMouseClicked(e -> {
            ComponentDefine cd = componentList.getListView().getSelectionModel().getSelectedItem();
            if (cd != null) {
                Jfx.runOnJme(() -> {
                    ObjectData od = Loader.loadData(cd.getId());
                    if (od != null) {
                        ObjectDataAddedUndoRedo ur = new ObjectDataAddedUndoRedo(od);
                        ur.redo();
                        jfxEdit.getJmeEdit().addUndoRedo(ur);
                    }
                });
            }
            componentList.hide();
        });
        
        listView.setCellFactory((ListView<ObjectData> param) -> new ListCell<ObjectData>(){
            @Override
            protected void updateItem(ObjectData item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                setGraphic(null);
                if (!empty) {
                    String name = item.getName();
                    if (name != null && !name.isEmpty()) {
                        setText(item.getId() + "(" + name + ")");
                    } else {
                        setText(item.getId());
                    }
                }
            }
        });
        
        listView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ObjectData> observable
                , ObjectData oldValue, ObjectData newValue) -> {
            if (dataConverter != null && dataConverter.isInitialized()) {
                dataConverter.cleanup();
            }
            // 注：newVaue是有可能为null的，当删除了列表中的节点时可能发生，所以要避免NPE
            if (newValue == null) {
                return;
            }
            dataConverter = ConverterManager.createDataConverter(jfxEdit, newValue, EntityObjectDatasConverter.this);
            dataConverter.initialize();
//            getParent().setChildContent(newValue.getId(), dataConverter.getLayout());
            getParent().setChildLayout(newValue.getId(), dataConverter);
        });
        listView.setPrefHeight(160);
        
        toolbar.getItems().addAll(add, remove);
        layout.getStyleClass().add(StyleConstants.CLASS_HVBOX);
        layout.getChildren().addAll(toolbar, listView);
    }

    @Override
    public void notifyChanged() {
        listView.refresh();// 这一句允许刷新列表中的物体名称。
        super.notifyChanged();
    }

    @Override
    public void initialize() {
        super.initialize();
        
        String tempDataType = featureHelper.getAsString(FEATURE_DATA_TYPE);
        try {
            dataType = (Class<? extends ObjectData>) Class.forName(tempDataType);
        } catch (Exception ex) {
            dataType = ObjectData.class;
            LOG.log(Level.WARNING
                    , "Unknow dataType! dataType=" + tempDataType + ", field=" + field + ", data=" + data.getId()
                    , ex);
        }
        componentType = featureHelper.getAsString(FEATURE_COMPONENT_TYPE);
        if (componentType != null) {
            componentList.setComponents(ComponentManager.getComponentsByType(componentType));
        } else {
            componentList.setComponents(ComponentManager.getComponents(null));
        }
    }

    @Override
    public void cleanup() {
        if (dataConverter != null) {
            if (dataConverter.isInitialized()) {
                dataConverter.cleanup();
            }
            dataConverter = null;
        }
        super.cleanup(); 
    }
    
    @Override
    protected Node createLayout() {
        return layout;
    }

    @Override
    protected void updateUI() {
        List dataList = new ArrayList();
        data.getObjectDatas(dataType, dataList);
        listView.getItems().clear();
        listView.getItems().addAll(dataList);
    }
    
    private class ObjectDataAddedUndoRedo implements UndoRedo {
        private final ObjectData added;
        public ObjectDataAddedUndoRedo(ObjectData added) {
            this.added = added;
        }
        @Override
        public void undo() {
            data.removeObjectData(added);
            Jfx.runOnJfx(() -> {
                listView.getItems().remove(added);
                notifyChanged();
            });
        }
        @Override
        public void redo() {
            data.addObjectData(added);
            Jfx.runOnJfx(() -> {
                listView.getItems().add(added);
                notifyChanged();
            });
        }
    }
    
    private class ObjectDataRemovedUndoRedo implements UndoRedo {
        private final ObjectData removed;
        public ObjectDataRemovedUndoRedo(ObjectData removed) {
            this.removed = removed;
        }
        @Override
        public void undo() {
            data.addObjectData(removed);
            Jfx.runOnJfx(() -> {
                listView.getItems().add(removed);
                notifyChanged();
            });
        }
        @Override
        public void redo() {
            data.removeObjectData(removed);
            Jfx.runOnJfx(() -> {
                listView.getItems().remove(removed);
                notifyChanged();
            });
        }
    }
}
