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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import name.huliqing.editor.component.ComponentDefine;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.constants.ComponentConstants;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.converter.FieldConverter;
import name.huliqing.editor.edit.Mode;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.editor.edit.controls.ControlTile;
import name.huliqing.editor.edit.scene.JfxSceneEdit;
import name.huliqing.editor.edit.scene.JfxSceneEditListener;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;
import name.huliqing.editor.edit.scene.SceneEdit;
import name.huliqing.editor.manager.ComponentManager;
import name.huliqing.editor.manager.ConverterManager;
import name.huliqing.editor.ui.ComponentSearch;
import name.huliqing.editor.ui.utils.JfxUtils;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.scene.Scene;

/**
 * 场景的"entities"字段的转换器, 将entities转换为列表
 * @author huliqing
 */
public class EntitiesFieldConverter extends FieldConverter<JfxSceneEdit, EntityData> implements JfxSceneEditListener {

    private final VBox layout = new VBox();
    private final ToolBar toolBar = new ToolBar();
    
    private final FilterListView filterListView = new FilterListView();
    private boolean ignoreSelectEvent;
    
    private final Map<EntityData, DataConverter> entityConverterMaps = new HashMap();
    // 当前正在显示的EntityConverter
    private DataConverter dataConverter;
    private final ComponentSearch<ComponentDefine> componentSearch = new ComponentSearch(ComponentManager.getComponentsByType(ComponentConstants.ENTITY));
    
    public EntitiesFieldConverter() {
        // 工具栏
        Button add = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_ADD));
        Button remove = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_SUBTRACT));
        ToggleButton multSelect = new ToggleButton("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_MULT_SELECT));
        add.setOnAction(e -> {
            componentSearch.show(add, -10, -10);
        });
        remove.setOnAction(e -> {
            List<EntityData> eds = filterListView.listView.getSelectionModel().getSelectedItems();
            if (eds == null || eds.isEmpty())
                return;
            
            Jfx.runOnJme(() -> {
                List<EntityControlTile> ectsRemove = new ArrayList(eds.size());
                SceneEdit se = jfxEdit.getJmeEdit();
                for (EntityData ed : eds) {
                    EntityControlTile ect = se.getEntityControlTile(ed);
                    if (ect != null) {
                        ectsRemove.add(ect);
                    }
                }
                EntityRemovedUndoRedo erur = new EntityRemovedUndoRedo(ectsRemove);
                erur.redo();
                se.addUndoRedo(erur);
                
                // 这一步不需要，因为已经做了监听场景实体移除的功能,当场景实体移除的时候会自动触发onEntityRemoved方法
//                Jfx.runOnJfx(() -> {
//                    listView.getItems().removeAll(eds);
//                });
            });
        });
        multSelect.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                filterListView.listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            } else {
                filterListView.listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            }
        });
        
        componentSearch.getListView().setOnMouseClicked(e -> {
            ComponentDefine cd = componentSearch.getListView().getSelectionModel().getSelectedItem();
            if (cd != null) {
                ComponentManager.createComponent(cd, jfxEdit);
                componentSearch.hide();
            }
        });
        
        // 列表
        filterListView.listView.setCellFactory(new CellInner());
        filterListView.listView.getSelectionModel().selectedItemProperty().addListener(this::onJfxSelectChanged);
//        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        layout.getStyleClass().add(StyleConstants.CLASS_HVBOX);
        toolBar.getItems().addAll(add, remove, multSelect);
        layout.getChildren().addAll(toolBar, filterListView);
        
    }
        
    @Override
    protected Node createLayout() {
        return layout;
    }
    
    @Override
    public void notifyChanged() {
        filterListView.listView.refresh();// 这一句允许刷新列表中的物体名称。
        super.notifyChanged();
    }
    
    @Override
    public void initialize() {
        super.initialize();
        // 用于监听3D场景中选择物体的变化
        jfxEdit.addListener(this);
    }
    
    @Override
    public void cleanup() {
        if (dataConverter != null) {
            if (dataConverter.isInitialized()) {
                dataConverter.cleanup();
            }
            dataConverter = null;
        }
        jfxEdit.removeListener(this);
        super.cleanup(); 
    }

    private void onJfxSelectChanged(ObservableValue observable, EntityData oldValue, EntityData newValue) {
        if (ignoreSelectEvent) {
            return;
        }
        if (newValue != null) {
            // 注：重新设置选择的时候会触发事件，回调到onSelectChanged(EntitySelectObj)
            // 要注意避免在该方法中导致死循环重复。
            jfxEdit.setSelected(newValue);
            doUpdateEntityView(newValue);
        }
    }

    @Override
    public void onModeChanged(Mode mode) {
        // 不管
    }

    @Override
    public void onEntityAdded(EntityData entityData) {
        filterListView.listView.getItems().add(entityData);
    }

    @Override
    public void onEntityRemoved(EntityData ed) {
        filterListView.listView.getItems().remove(ed);
        entityConverterMaps.remove(ed);
    }

    @Override
    public void onSelectChanged(ControlTile selectObj) {
        if (selectObj == null) {
            ignoreSelectEvent = true;
            filterListView.listView.getSelectionModel().clearSelection();
            doUpdateEntityView(null);
            ignoreSelectEvent = false;
            return;
        }
        if (!(selectObj instanceof EntityControlTile))
            return;
        
        ignoreSelectEvent = true;
        EntityData ed = ((EntityControlTile)selectObj).getTarget().getData();
        filterListView.listView.getSelectionModel().select(ed);
        doUpdateEntityView(ed);
        ignoreSelectEvent = false;
    }
    
    private void doUpdateEntityView(EntityData entityData) {
        if (entityData == null) {
            return;
        }
        
        DataConverter dc = entityConverterMaps.get(entityData);
        if (dc == null) {
            dc = ConverterManager.createDataConverter(jfxEdit, entityData, this);
            entityConverterMaps.put(entityData, dc);
        }
        if (dataConverter != null) {
            dataConverter.cleanup();
        }
        dataConverter = dc;
        dataConverter.initialize();
        getParent().setChildLayout(entityData.getId(), dataConverter);
    }

    @Override
    public void updateView() {
        // ignore
    }

    private class CellInner implements Callback<ListView<EntityData>, ListCell<EntityData>> {

        @Override
        public ListCell<EntityData> call(ListView<EntityData> param) {
            ListCell<EntityData> lc = new ListCell<EntityData>() {
                @Override
                protected void updateItem(EntityData item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(null);
                    setGraphic(null);
                    if (!empty && item != null) {
                        setGraphic(new ListCellRow(item));
                    }
                    
                }
            };
            return lc;
        }
    }
    
    private class ListCellRow extends GridPane {
        private final Label nameLabel = new Label("");
        private final ToggleButton enableBtn = new ToggleButton("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_EYE));
        private final EntityData entityData;
        public ListCellRow(EntityData item) {
            entityData = item;
            String nameStr = item.getId();
            if (item.getName() != null && !item.getName().isEmpty()) {
                nameStr += "(" + item.getName() + ")";
            }
            nameLabel.setText(nameStr);
            nameLabel.setAlignment(Pos.CENTER_LEFT);
            enableBtn.setSelected(item.getAsBoolean("enabled", true));
            enableBtn.setAlignment(Pos.CENTER_RIGHT);
            enableBtn.setPrefWidth(16);
            enableBtn.selectedProperty().addListener((ObservableValue<? extends Boolean> ob, Boolean oldValue, Boolean newValue) -> {
                Scene scene = jfxEdit.getJmeEdit().getScene();
                if (scene == null)
                    return;
                Jfx.runOnJme(() -> {
                    Entity entity = scene.getEntity(entityData.getUniqueId());
                    if (entity != null) {
                        entity.setEnabled(newValue);
                    }
                });
            });
            setAlignment(Pos.CENTER_LEFT);
            addRow(0, nameLabel, enableBtn);
            GridPane.setHgrow(nameLabel, Priority.ALWAYS);
            GridPane.setHgrow(enableBtn, Priority.NEVER);
        }
    }
    
    private class EntityRemovedUndoRedo implements UndoRedo {
        private final List<EntityControlTile> ectRemoved = new ArrayList();
        public EntityRemovedUndoRedo(List<EntityControlTile> ectRemoved) {
            this.ectRemoved.addAll(ectRemoved);
        }
        @Override
        public void undo() {
            SceneEdit se = jfxEdit.getJmeEdit();
            for (int i = ectRemoved.size() - 1; i >= 0; i--) {
                se.addControlTile(ectRemoved.get(i));
            }
        }
        @Override
        public void redo() {
            SceneEdit se = jfxEdit.getJmeEdit();
            for (int i = 0; i < ectRemoved.size(); i++) {
                se.removeControlTile(ectRemoved.get(i));
            }
        }
    }
    
    // 场景实体列表,包含过滤功能
    private class FilterListView extends VBox {
        
        private final StackPane filterPane = new StackPane();
        private final HBox imageView = new HBox(JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_SEARCH));
        private final TextField inputFilter = new TextField();
        
        private final ListView<EntityData> listView = new ListView();
        private final List<EntityData> tempList = new ArrayList();
        
        public FilterListView() {
            imageView.setPadding(new Insets(0, 0, 0, 10));
            imageView.setMinWidth(16);
            imageView.setMaxWidth(16);
            imageView.prefHeightProperty().bind(filterPane.heightProperty());
            imageView.setAlignment(Pos.CENTER);
            inputFilter.prefWidthProperty().bind(filterPane.widthProperty());
            inputFilter.prefHeightProperty().bind(filterPane.heightProperty());
            inputFilter.setPadding(new Insets(0, 0, 0, 25));
            filterPane.setMinHeight(25);
            filterPane.setAlignment(Pos.CENTER_LEFT);
            
            filterPane.getChildren().add(inputFilter);
            filterPane.getChildren().add(imageView);
            getChildren().add(filterPane);
            getChildren().add(listView);
            
            inputFilter.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                updateList();
            });
            
            listView.setStyle("-fx-background-radius: 0 0 7 7;");
            inputFilter.setStyle("-fx-background-radius: 7 7 0 0;");
        }
        
        public void updateList() {
            Scene scene = jfxEdit.getJmeEdit().getScene();
            if (scene == null) {
                listView.getItems().clear();
                tempList.clear();
                return;
            }
            tempList.clear();
            listView.getItems().clear();
            
            List<Entity> entities = scene.getEntities();
            String filterText = inputFilter.getText().trim().toLowerCase();
            String entityStr;
            for (Entity e : entities) {
                if (filterText.isEmpty()) {
                    tempList.add(e.getData());
                    continue;
                }
                entityStr = e.getData().getId();
                if (e.getData().getName() != null) {
                    entityStr += "(" + e.getData().getName() + ")";
                }
                if (entityStr.toLowerCase().contains(filterText)) {
                    tempList.add(e.getData());
                }
            }
            listView.getItems().addAll(tempList);
        }
        
    }
}
