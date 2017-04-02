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
package name.huliqing.editor.ui;

import java.util.List;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import name.huliqing.editor.constants.DataFormatConstants;
import name.huliqing.editor.component.ComponentDefine;
import name.huliqing.editor.constants.ComponentConstants;
import name.huliqing.editor.constants.ConfigConstants;
import name.huliqing.editor.manager.ComponentManager;
import name.huliqing.editor.manager.ConfigManager.ConfigChangedListener;
import name.huliqing.editor.manager.Manager;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class ComponentsForm extends ListView<ComponentDefine> implements ConfigChangedListener{
    private static final Logger LOG = Logger.getLogger(ComponentsForm.class.getName());
    
    public ComponentsForm() {

        setCellFactory((ListView<ComponentDefine> param) -> new ListCell<ComponentDefine>() {
            @Override
            protected void updateItem(ComponentDefine item, boolean empty) {
                super.updateItem(item, empty);
                String name = null;
                Node icon = null;
                if (item != null && !empty) {
                    name = item.getId();
                }
                setText(name);
                setGraphic(icon);
            }
        });

        setOnDragDetected(this::doDragDetected);
        setOnDragDone(this::doDragDone);
        
        updateAassetDir();
        
        // 切换资源目录的时候要重置组件面板
        Manager.getConfigManager().addListener(this);
    }
    
    @Override
    public void onConfigChanged(String key) {
        if (key.equals(ConfigConstants.KEY_MAIN_ASSETS)) {
            Jfx.runOnJfx(() -> updateAassetDir());
        }
    }
    
    private void updateAassetDir() {
        getItems().clear();
        List<ComponentDefine> cds = ComponentManager.getComponentsByType(ComponentConstants.ENTITY);
        if (cds != null) {
            getItems().addAll(cds);
        }
        List<ComponentDefine> cdsFilter = ComponentManager.getComponentsByType(ComponentConstants.ENTITY_FILTER);
        if (cdsFilter != null) {
            getItems().addAll(cdsFilter);
        }
    }

    private ComponentDefine getMainSelectItem() {
        ObservableList<ComponentDefine> items = getSelectionModel().getSelectedItems();
        if (items.isEmpty()) {
            return null;
        }
        for (ComponentDefine c : items) {
            if (c != null) {
                return c;
            }
        }
        return null;
    }

    private void doDragDetected(MouseEvent e) {
//        LOG.log(Level.INFO, "EntityComponents: doDragDetected.");
        ComponentDefine selected = getMainSelectItem();
        if (selected == null) {
            e.consume();
            return;
        }
        Dragboard db = startDragAndDrop(TransferMode.ANY);
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.put(DataFormatConstants.COMPONENT_ENTITY, selected);
        db.setContent(clipboardContent);
        e.consume();
    }

    private void doDragDone(DragEvent e) {
//        LOG.log(Level.INFO, "EntityComponents: doDragDone.");
        e.consume();
    }

}
