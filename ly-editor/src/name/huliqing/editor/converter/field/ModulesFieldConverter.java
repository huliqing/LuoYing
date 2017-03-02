/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.field;

import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import name.huliqing.editor.component.ComponentDefine;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.constants.ComponentConstants;
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
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.object.Loader;

/**
 *
 * @author huliqing
 */
public class ModulesFieldConverter extends SimpleFieldConverter<JfxSceneEdit, EntityData> {

    private final VBox layout = new VBox();
    
    private final ToolBar toolBar = new ToolBar();
    private final ListView<ModuleData> listView = new ListView();
    
    private final ComponentSearch<ComponentDefine> componentInput = 
            new ComponentSearch(ComponentManager.getComponentsByType(ComponentConstants.MODULE));
    
    private DataConverter dataConverter;
    
    public ModulesFieldConverter() {
        Button add = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_ADD));
        Button remove = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_SUBTRACT));
        toolBar.getItems().addAll(add, remove);
        add.setOnAction(e -> {
            componentInput.show(add, -10, -10);
        });
        remove.setOnAction(e -> {
            ModuleData moduleData = listView.getSelectionModel().getSelectedItem();
            if (moduleData != null) {
                ModuleDataRemovedUndoRedo ur = new ModuleDataRemovedUndoRedo(moduleData);
                ur.redo();
                addUndoRedo(ur);
            }
        });
        
        componentInput.getListView().setOnMouseClicked(e -> {
            ComponentDefine cd = componentInput.getListView().getSelectionModel().getSelectedItem();
            if (cd != null) {
                Jfx.runOnJme(() -> {
                    ModuleData moduleData = Loader.loadData(cd.getId());
                    if (moduleData != null) {
                        ModuleDataAddedUndoRedo ur = new ModuleDataAddedUndoRedo(moduleData);
                        ur.redo();
                        addUndoRedo(ur);
                    }
                });
            }
            componentInput.hide();
        });
        
        layout.setPadding(Insets.EMPTY);
        layout.getChildren().addAll(toolBar, listView);
        
        listView.setPrefHeight(160);
        listView.setCellFactory((ListView<ModuleData> param) -> new ListCell<ModuleData>(){
            @Override
            protected void updateItem(ModuleData item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                setGraphic(null);
                if (!empty) {
                    setText(item.getId());
                }
            }
        });
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ModuleData>() {
            @Override
            public void changed(ObservableValue<? extends ModuleData> observable, ModuleData oldValue, ModuleData newValue) {
                if (dataConverter != null && dataConverter.isInitialized()) {
                    dataConverter.cleanup();
                }
                // 注：newVaue是有可能为null的，当删除了列表中的节点时可能发生，所以要避免NPE
                if (newValue == null) {
                    return;
                }
                dataConverter = ConverterManager.createDataConverter(jfxEdit, newValue, ModulesFieldConverter.this);
                dataConverter.initialize();
                getParent().setChildContent(newValue.getId(), dataConverter.getLayout());
            }
        });
    }
    
    @Override
    protected void updateUI() {
        List<ModuleData> moduleDatas = data.getModuleDatas();
        listView.getItems().clear();
        if (moduleDatas != null) {
            listView.getItems().addAll(moduleDatas);
        }
    }
    
    @Override
    protected Node createLayout() {
        return layout;
    }
    
    private class ModuleDataAddedUndoRedo implements UndoRedo {
        private final ModuleData mdAdded;
        public ModuleDataAddedUndoRedo(ModuleData added) {
            this.mdAdded = added;
        }
        @Override
        public void undo() {
            data.removeModuleData(mdAdded);
            Jfx.runOnJfx(() -> {
                listView.getItems().remove(mdAdded);
                notifyChanged();
            });
        }
        @Override
        public void redo() {
            data.addModuleData(mdAdded);
            Jfx.runOnJfx(() -> {
                listView.getItems().add(mdAdded);
                notifyChanged();
            });
        }
    }
    
    private class ModuleDataRemovedUndoRedo implements UndoRedo {
        private final ModuleData mdRemoved;
        public ModuleDataRemovedUndoRedo(ModuleData mdRemoved) {
            this.mdRemoved = mdRemoved;
        }
        @Override
        public void undo() {
            data.addModuleData(mdRemoved);
            Jfx.runOnJfx(() -> {
                listView.getItems().add(mdRemoved);
                notifyChanged();
            });
        }
        @Override
        public void redo() {
            data.removeModuleData(mdRemoved);
            Jfx.runOnJfx(() -> {
                listView.getItems().remove(mdRemoved);
                notifyChanged();
            });
        }
    }
}
