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

import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.edit.scene.SceneEdit;
import name.huliqing.editor.tools.entity.InstancedTool;
import name.huliqing.editor.ui.DataProcessorSearch;
import name.huliqing.editor.ui.utils.JfxUtils;
import name.huliqing.luoying.object.entity.impl.InstancedEntity;

/**
 * 用于渲染InstancedTool的JFX工具, 允许在场景刷实体的时候指定一个实例节点InstancedEntity,当刷实体的时候把所有
 * 实体都刷到这个节点上。
 * @author huliqing
 */
public class JfxInstancedTool extends JfxAbstractTool<InstancedTool>{

    private final VBox view = new VBox();
    
    private final Label title = new Label();
    
    private final HBox content = new HBox();
    private final TextField input = new TextField();
    private final Button selectButton = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_SEARCH));
    private DataProcessorSearch<InstancedEntity> entitySearch = new DataProcessorSearch();
    
    private InstancedEntity instancedEntity;

    public JfxInstancedTool() {
        view.getChildren().addAll(title, content);
        content.getChildren().addAll(input, selectButton);
        
        selectButton.setOnAction(e -> {
            SceneEdit se = (SceneEdit) this.toolbar.getEdit();
            List<InstancedEntity> ies = se.getScene().getEntities(InstancedEntity.class, new ArrayList<>());
            entitySearch.setItems(ies);
            entitySearch.show(input, -10, -8);
        });
        
        entitySearch.getListView().setOnMouseClicked(e -> {
            InstancedEntity ie = entitySearch.getListView().getSelectionModel().getSelectedItem();
            tool.setInstancedEntity(ie);
            updateView();
            entitySearch.hide();
        });
        
        input.focusedProperty().addListener((ObservableValue<? extends Boolean> observable
                , Boolean oldValue, Boolean newValue) -> {
            // 如果是获得焦点则不理睬。
            if (newValue) {
                return;
            }
            if (input.getText() == null || input.getText().isEmpty()) {
                tool.setInstancedEntity(null);
            }
        });
        
        input.setStyle("-fx-background-radius: 7 0 0 7;");
        selectButton.setStyle("-fx-background-radius: 0 7 7 0;");
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
            selectButton.setTooltip(new Tooltip(tool.getTips()));
        }
        
        updateView();
    }
    
    private void updateView() {
        instancedEntity = tool.getInstancedEntity();
        if (instancedEntity != null) {
            String ieName = instancedEntity.getData().getName();
            ieName = ieName != null ? ieName : "no name";
            input.setText(instancedEntity.getEntityId() + "(" + ieName + ")");
        } else {
            input.setText("");
        }
    }
    
}
