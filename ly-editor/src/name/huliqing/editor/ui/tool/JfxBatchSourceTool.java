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
package name.huliqing.editor.ui.tool;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.tools.batch.BatchSourceTool;
import name.huliqing.editor.ui.utils.JfxUtils;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 用于选择实体列表进行batch
 * @author huliqing
 */
public class JfxBatchSourceTool extends JfxAbstractTool<BatchSourceTool>{

    private final VBox view = new VBox();
    
    private final Label title = new Label();
    private final ToolBar btnPanel = new ToolBar();
    private final Button add = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_ADD));
    private final Button remove = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_SUBTRACT));
    
    private final ListView<Entity> content = new ListView();
    
    public JfxBatchSourceTool() {
        btnPanel.getItems().addAll(add, remove);
        view.getChildren().addAll(title, btnPanel, content);
        
        add.setOnAction((ActionEvent event) -> {
            Jfx.runOnJme(() -> {
//                SimpleJmeEdit je = (SimpleJmeEdit) toolbar.getEdit();
//                ControlTile selected = je.getSelected();
//                if (selected == null) 
//                    return;
//                
//                Object selectObj = selected.getTarget();
//                if (!(selectObj instanceof Entity)) {
//                    return;
//                }
//                
//                Entity se = (Entity) selectObj;
//                if (tool.getSources().contains(se.getData())) {
//                    return;
//                }
//                AddSourceUndoRedo asur = new AddSourceUndoRedo(se.getData());
//                asur.redo();
//                toolbar.getEdit().addUndoRedo(asur);
            });
        });
        
        remove.setOnAction((ActionEvent event) -> {
            Entity entity = content.getSelectionModel().getSelectedItem();
            if (entity == null) 
                return;
            
            Jfx.runOnJme(() -> {
//                if (tool.getEntities() == null || !tool.getEntities().contains(ed)) 
//                    return;
//                
//                RemoveSourceUndoRedo rsur = new RemoveSourceUndoRedo(ed);
//                rsur.redo();
//                toolbar.getEdit().addUndoRedo(rsur);
            });
        });
        
        content.setCellFactory((ListView<Entity> param) -> new ListCell<Entity>() {
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
        
        content.prefWidthProperty().bind(view.widthProperty());
        content.prefHeightProperty().bind(view.heightProperty().subtract(btnPanel.heightProperty()));
        view.setMinHeight(160);
    }
    
    @Override
    protected Region createView() {
        return view;
    }

    @Override
    public void initialize() {
        super.initialize();
        
        title.setText(tool.getName());
        
        if (tool.getTips() != null) {
            btnPanel.setTooltip(new Tooltip(tool.getTips()));
        }
        
        updateView();
    }
    
    private void updateView() {
        List<Entity> entities = tool.getEntities();
        content.getItems().clear();
        if (entities != null) {
            content.getItems().addAll(entities);
        }
    }
    
//    private class AddSourceUndoRedo implements UndoRedo {
//        private final EntityData sourceAdded;
//        public AddSourceUndoRedo(EntityData sourceAdded) {
//            this.sourceAdded = sourceAdded;
//        }
//        
//        @Override
//        public void undo() {
//            tool.removeSource(sourceAdded);
//            Jfx.runOnJfx(() -> updateView());
//        }
//
//        @Override
//        public void redo() {
//            tool.addSource(sourceAdded);
//            Jfx.runOnJfx(() -> updateView());
//        }
//    }
//    
//    private class RemoveSourceUndoRedo implements UndoRedo {
//        private final EntityData sourceRemoved;
//        public RemoveSourceUndoRedo(EntityData sourceRemoved) {
//            this.sourceRemoved = sourceRemoved;
//        }
//        
//        @Override
//        public void undo() { 
//            tool.addSource(sourceRemoved);
//            Jfx.runOnJfx(() -> updateView());
//        }
//
//        @Override
//        public void redo() {
//            tool.removeSource(sourceRemoved);
//            Jfx.runOnJfx(() -> updateView());
//        }
//    }
}
