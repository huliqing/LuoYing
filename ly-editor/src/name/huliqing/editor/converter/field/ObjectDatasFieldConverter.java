/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.field;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import name.huliqing.editor.component.ComponentDefine;
import name.huliqing.editor.constants.AssetConstants;
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
 * 针对Entity的objectDatas字段的转换器
 * @author huliqing
 */
public class ObjectDatasFieldConverter extends SimpleFieldConverter<JfxSceneEdit, EntityData> {

    private final VBox layout = new VBox();
    
    private final ToolBar toolBar = new ToolBar();
    private final ListView<ObjectData> listView = new ListView();
    
    private final ComponentSearch<ComponentDefine> componentInput = 
            new ComponentSearch(ComponentManager.getComponents(new ArrayList()));
    
    // 点击模块后显示子模块界面
    private final TitledPane childPanel = new TitledPane();
    private DataConverter currentDisplayConverter;
    
    public ObjectDatasFieldConverter() {
        Button add = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_ADD));
        Button remove = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_SUBTRACT));
        toolBar.getItems().addAll(add, remove);
        add.setOnAction(e -> {
            componentInput.show(add, -10, -10);
        });
        remove.setOnAction(e -> {
            ObjectData od = listView.getSelectionModel().getSelectedItem();
            if (od != null) {
                ObjectDataRemovedUndoRedo ur = new ObjectDataRemovedUndoRedo(od);
                ur.redo();
                addUndoRedo(ur);
            }
        });
        
        componentInput.getListView().setOnMouseClicked(e -> {
            ComponentDefine cd = componentInput.getListView().getSelectionModel().getSelectedItem();
            if (cd != null) {
                Jfx.runOnJme(() -> {
                    ObjectData od = Loader.loadData(cd.getId());
                    if (od != null) {
                        ObjectDataAddedUndoRedo ur = new ObjectDataAddedUndoRedo(od);
                        ur.redo();
                        addUndoRedo(ur);
                    }
                });
                componentInput.hide();
            }
        });
        
        layout.setPadding(Insets.EMPTY);
        layout.getChildren().addAll(toolBar, listView);
        
        listView.setPrefHeight(160);
        listView.setCellFactory((ListView<ObjectData> param) -> new ListCell<ObjectData>(){
            @Override
            protected void updateItem(ObjectData item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                setGraphic(null);
                if (!empty) {
                    setText(item.getId());
                }
            }
        });
        listView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ObjectData> observable
                , ObjectData oldValue, ObjectData newValue) -> {
            if (currentDisplayConverter != null && currentDisplayConverter.isInitialized()) {
                currentDisplayConverter.cleanup();
                childPanel.setContent(null);
            }
            // 注：newVaue是有可能为null的，当删除了列表中的节点时可能发生，所以要避免NPE
            if (newValue == null) {
                childPanel.setVisible(false);
                return;
            }
            currentDisplayConverter = ConverterManager.createDataConverter(jfxEdit, newValue, ObjectDatasFieldConverter.this);
            currentDisplayConverter.initialize();
            childPanel.setContent(currentDisplayConverter.getLayout());
            childPanel.setText(newValue.getId());
            childPanel.setVisible(true);
        });
        
        childPanel.setVisible(false);
    }

    @Override
    public void initialize() {
        super.initialize();
        jfxEdit.getPropertyPanel().getChildren().add(childPanel);
    }

    @Override
    public void cleanup() {
        jfxEdit.getPropertyPanel().getChildren().remove(childPanel);
        super.cleanup(); 
    }
    
    @Override
    protected void updateUI() {
        List<ObjectData> objectDatas = data.getObjectDatas();
        listView.getItems().clear();
        if (objectDatas != null) {
            listView.getItems().addAll(objectDatas);
        }
    }

    @Override
    protected Node createLayout() {
        return layout;
    }
 
    private class ObjectDataAddedUndoRedo implements UndoRedo {
        private final ObjectData odAdded;
        public ObjectDataAddedUndoRedo(ObjectData added) {
            this.odAdded = added;
        }
        @Override
        public void undo() {
            data.removeObjectData(odAdded);
            Jfx.runOnJfx(() -> {
                listView.getItems().remove(odAdded);
                notifyChanged();
            });
        }
        @Override
        public void redo() {
            data.addObjectData(odAdded);
            Jfx.runOnJfx(() -> {
                listView.getItems().add(odAdded);
                notifyChanged();
            });
        }
    }
    
    private class ObjectDataRemovedUndoRedo implements UndoRedo {
        private final ObjectData odRemoved;
        public ObjectDataRemovedUndoRedo(ObjectData odRemoved) {
            this.odRemoved = odRemoved;
        }
        @Override
        public void undo() {
            data.addObjectData(odRemoved);
            Jfx.runOnJfx(() -> {
                listView.getItems().add(odRemoved);
                notifyChanged();
            });
        }
        @Override
        public void redo() {
            data.removeObjectData(odRemoved);
            Jfx.runOnJfx(() -> {
                listView.getItems().remove(odRemoved);
                notifyChanged();
            });
        }
    }
}
