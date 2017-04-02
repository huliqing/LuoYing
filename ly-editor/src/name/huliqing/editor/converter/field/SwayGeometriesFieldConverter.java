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

import com.jme3.scene.Geometry;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.converter.SimpleFieldConverter;
import name.huliqing.editor.edit.scene.JfxSceneEdit;
import name.huliqing.editor.ui.ListViewPopup;
import name.huliqing.editor.ui.utils.JfxUtils;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.utils.GeometryUtils;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 用于转换PlantEntity的SwayGeometries字段，以方便选择”摇动“的网格物体.
 * @author huliqing
 */
public class SwayGeometriesFieldConverter extends SimpleFieldConverter<JfxSceneEdit, ObjectData>{

    private final VBox layout = new VBox();
    private final ToolBar toolbar = new ToolBar();
    private final ListView<String> listView = new ListView();
    private final ListViewPopup<Geometry> listViewPopup = new ListViewPopup();
    
    private List<String> lastValues;
    
    public SwayGeometriesFieldConverter() {
        Button add = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_ADD));
        Button remove = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_SUBTRACT));
        
        add.setOnAction(e -> {
            listViewPopup.show(add, -10, -10);
        });
        remove.setOnAction(e -> {
            String itemSel = listView.getSelectionModel().getSelectedItem();
            if (itemSel == null)
                return;
            
            List<String> afterValues = new ArrayList(lastValues);
            afterValues.remove(itemSel);
            updateAttribute(afterValues);
            addUndoRedo(lastValues, afterValues);
            lastValues = afterValues;
            updateUI();
        });
        
        listViewPopup.getListView().setOnMouseClicked(e -> {
            listViewPopup.hide();
            Geometry cd = listViewPopup.getListView().getSelectionModel().getSelectedItem();
            if (cd == null || (lastValues != null && lastValues.contains(cd.getName()))) {
                return;
            }
            List<String> afterValues = lastValues == null ? new ArrayList(1) : new ArrayList(lastValues);
            afterValues.add(cd.getName());
            updateAttribute(afterValues);
            addUndoRedo(lastValues, afterValues);
            lastValues = afterValues;
            updateUI();
        });
        
        listView.setCellFactory((ListView<String> param) -> new ListCell<String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                setGraphic(null);
                if (!empty) {
                    setText(item);
                }
            }
        });
      
        listView.setPrefHeight(160);
        
        toolbar.getItems().addAll(add, remove);
        layout.getStyleClass().add(StyleConstants.CLASS_HVBOX);
        layout.getChildren().addAll(toolbar, listView);
    }

    @Override
    public void initialize() {
        super.initialize();
        Entity en = jfxEdit.getJmeEdit().getScene().getEntity(data.getUniqueId());
        if (en != null) {
            Jfx.runOnJme(() -> {
                List<Geometry> geos = GeometryUtils.findAllGeometry(en.getSpatial());
                listViewPopup.setItems(geos);
            });
        }
    }
    
    @Override
    protected void updateUI() {
        lastValues = data.getAsStringList(field);
        listView.getItems().clear();
        if (lastValues != null) {
            listView.getItems().addAll(lastValues);
        }
    }

    @Override
    protected Node createLayout() {
        return layout;
    }
    
}
