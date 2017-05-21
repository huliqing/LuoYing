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

import java.util.LinkedList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.converter.SimpleFieldConverter;
import name.huliqing.editor.edit.scene.JfxSceneEdit;
import name.huliqing.editor.edit.scene.SceneEdit;
import name.huliqing.editor.ui.utils.JfxUtils;
import name.huliqing.editor.ui.utils.SearchListView;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.impl.BatchEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.xml.ObjectData;

/**
 * BatchEntity的entities字段的转换器。
 * 通过从场景中添加实体后添加到BatchEntity中。
 * @author huliqing
 */
public class BatchEntityEntitiesConverter extends SimpleFieldConverter<JfxSceneEdit, ObjectData> {

    private final VBox layout = new VBox();
    private final ToolBar toolbar = new ToolBar();
    private final ListView<Entity> entitiesListView = new ListView();
    
    private final Button add = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_ADD));
    private final Button remove = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_SUBTRACT));
    private final Button rebatch = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_REFRESH));
    
    private final EntitySearch<Entity> entitySearch= new EntitySearch();
    
    // 当前的BatchEntity实体
    private BatchEntity batchEntity;
    
    public BatchEntityEntitiesConverter() {
        layout.getChildren().add(toolbar);
        layout.getChildren().add(entitiesListView);
        toolbar.getItems().addAll(add, remove, rebatch);
        
        add.setOnAction((ActionEvent event) -> {
            SceneEdit se = this.jfxEdit.getJmeEdit();
            if (se == null || se.getScene() == null)
                return;
            entitySearch.setItems(se.getScene().getEntities());
            entitySearch.show(add, -10, -10);
        });
        
        remove.setOnAction((ActionEvent event) -> {
            List<Entity> items = entitiesListView.getSelectionModel().getSelectedItems();
            if (items == null)
                return;
            SceneEdit sceneEdit = this.jfxEdit.getJmeEdit();
            Jfx.runOnJme(() -> {
                items.forEach(e -> {
                    batchEntity.removeBatchEntity(e);
                });
                batchEntity.applyBatch();
                batchEntity.updateDatas();
                Jfx.runOnJfx(() -> {updateView();});
            });
        });
        
        rebatch.setOnAction(e -> {
            Jfx.runOnJme(() -> {
                batchEntity.doRebatch();
            });
        });
        
        entitiesListView.setCellFactory((ListView<Entity> param) -> new ListCell<Entity>() {
            @Override
            protected void updateItem(Entity item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                setGraphic(null);
                if (!empty && item != null) {
                    setText(item.getData().getId() + "(" + item.getData().getName() + ")(" + item.getEntityId() + ")");
                }
            }
        });
        entitiesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        entitiesListView.setPrefHeight(200);
    }

    @Override
    public void initialize() {
        super.initialize();
        // 从场景找到BatchEntity
        batchEntity = (BatchEntity) jfxEdit.getJmeEdit().getScene().getEntity(data.getUniqueId());
    }
    
    @Override
    protected void updateUI() {
        entitiesListView.getItems().clear();
        long[] tempEntities = data.getAsLongArray(field);
        if (tempEntities != null) {
            Scene scene = this.jfxEdit.getJmeEdit().getScene();
            List<Entity> entities = new LinkedList();
            for (long eid : tempEntities) {
                Entity entity = scene.getEntity(eid);
                if (entity != null) {
                    entities.add(entity);
                }
            }
            entitiesListView.getItems().addAll(entities);
        }
    }

    @Override
    protected Node createLayout() {
        return layout;
    }
    
      // ---- 用于查询场景中的实体
    private final class EntitySearch<T extends Entity> {
        private final Popup popup = new Popup();
        private final VBox searchViewGroup = new VBox();
        private final SearchListView<Entity> searchListView = new SearchListView(new ListView());
        private final Button addAll = new Button("全部添加");

        public EntitySearch() {
            this(null);
        }

        public EntitySearch(List<Entity> items) {
            if (items != null) {
                setItems(items);
            }
            searchListView.getChildren().add(addAll);
            searchViewGroup.getChildren().addAll(searchListView);
            popup.getContent().add(searchViewGroup);
            popup.setAutoHide(true);
            searchListView.getListView().setCellFactory((ListView<Entity> param) -> new ListCell<Entity>() {
                @Override
                protected void updateItem(Entity item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.getData().getId() + "(" + item.getData().getName() + ")");
                    }
                }
            });
            // 匹配检查的时候要用字符串转换器
            searchListView.setConverter((Entity t) -> t.getData().getId());
            searchListView.setPrefWidth(250);
            searchListView.setPrefHeight(250);
            searchListView.setEffect(new DropShadow());
            searchListView.setConverter((Entity t) -> t.getData().getId() + "(" + t.getData().getName() + ")" + t.getData().getUniqueId());
            
            // 将经过过滤后的所有实体添加到SourceTool列表中。
            addAll.setOnAction((e) -> {
                popup.hide();
                List<Entity> filterItems = searchListView.getListView().getItems();
                Jfx.runOnJme(() -> {
                    filterItems.forEach(entity -> {
                        batchEntity.addBatchEntity(entity);
                    });
                    batchEntity.applyBatch();
                    batchEntity.updateDatas();
                    Jfx.runOnJfx(() -> {updateView();});
                });
            });
            
            searchListView.getListView().setOnMouseClicked(e -> {
                popup.hide();
                Entity entity = searchListView.getListView().getSelectionModel().getSelectedItem();
                if (entity == null)
                    return;
                Jfx.runOnJme(() -> {
                    batchEntity.addBatchEntity(entity);
                    batchEntity.applyBatch();
                    batchEntity.updateDatas();
                    Jfx.runOnJfx(() -> {updateView();});
                });
            });
        }

        public void setItems(List<Entity> items) {
            if (items == null) {
                return;
            }
            searchListView.setAllItems(items);
        }

        public void show(Region node, double offsetX, double offsetY) {
            Point2D txtCoords = node.localToScene(0.0, 0.0);
            popup.show(node,
                    txtCoords.getX() + node.getScene().getX() + node.getScene().getWindow().getX() + offsetX,
                    txtCoords.getY() + node.getScene().getY() + node.getScene().getWindow().getY() + offsetY + node.heightProperty().getValue());
        }
    }
}
