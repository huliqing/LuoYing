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

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.edit.JmeEdit;
import name.huliqing.editor.edit.scene.SceneEdit;
import name.huliqing.editor.tools.EntityValueTool;
import name.huliqing.editor.tools.ValueChangedListener;
import name.huliqing.editor.tools.ValueTool;
import name.huliqing.editor.ui.DataProcessorSearch;
import name.huliqing.editor.ui.utils.JfxUtils;
import name.huliqing.editor.ui.utils.SearchListView.Converter;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.object.entity.Entity;

/**
 *  用于从场景中选择一个实体
 * @author huliqing
 */
public class JfxEntityValueTool extends JfxAbstractTool<EntityValueTool> implements ValueChangedListener<Entity>{

    private final VBox view = new VBox();
    private final Label title = new Label();
    
    private final HBox layout = new HBox();
    private final TextField input = new TextField();
    private final Button selectButton = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_SEARCH));
    private final DataProcessorSearch<Entity> searchHelper = new DataProcessorSearch();
    private final Converter<Entity> converter = new StringConverter();
    
    public JfxEntityValueTool() {
        input.setStyle(StyleConstants.CSS_CORNER_ROUND_LEFT);
        selectButton.setStyle(StyleConstants.CSS_CORNER_ROUND_RIGHT);
        layout.getChildren().addAll(input, selectButton);
        
        selectButton.setOnAction(e -> {
            JmeEdit je = toolbar.getEdit();
            if (!(je instanceof SceneEdit)) {
                Logger.getLogger(JfxEntityValueTool.class.getName())
                        .log(Level.WARNING, "JfxEntityValueTool only support SceneEdit! edit={0}", je.getClass());
                return;
            }
            SceneEdit se = (SceneEdit) je;
            if (se.getScene() == null)
                return;
            Class<Entity> typeLimit = (Class<Entity>)tool.getEntityType(); 
            if (typeLimit != null) {
                searchHelper.setItems(se.getScene().getEntities(typeLimit, null));
            } else {
                searchHelper.setItems(se.getScene().getEntities());
            }
            searchHelper.show(selectButton, -10, -10);
        });

        searchHelper.setConverter(converter);
        searchHelper.getListView().setOnMouseClicked(e -> {
            searchHelper.hide();
            Entity entity = searchHelper.getListView().getSelectionModel().getSelectedItem();
            if (entity != null) {
                updateValueToEdit(entity);
            }
        });
        
        input.focusedProperty().addListener((ObservableValue<? extends Boolean> observable
                , Boolean oldValue, Boolean newValue) -> {
            if (newValue) return;
            // 清空
            if (input.getText().trim().isEmpty()) {
                updateValueToEdit(null);
            }
        });
        
        view.getChildren().addAll(title, layout);
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
            input.setTooltip(new Tooltip(tool.getTips()));
        }
        
        updateValueToView(tool.getValue());
        tool.addValueChangeListener(this);
    }

    @Override
    public void cleanup() {
        tool.removeValueChangeListener(this);
        super.cleanup();
    }

    @Override
    public void onValueChanged(ValueTool<Entity> vt, Entity oldValue, Entity newValue) {
        Jfx.runOnJfx(() -> {
            updateValueToView(newValue);
        });
    }
    
    private void updateValueToEdit(Entity entity) {
        Jfx.runOnJme(() -> {
            tool.setValue(entity);
        });
    }
    
    // 当Jfx组件值发生变化时更新到编辑场景中
    private void updateValueToView(Entity newValue) {
        if (newValue != null) {
            input.setText(converter.convertToString(newValue));
        } else {
            input.setText("");
        }
    }
    
    private class StringConverter implements Converter<Entity> {

        @Override
        public String convertToString(Entity t) {
            String str;
            if (t.getData().getName() != null) {
                str = t.getData().getId() + "(" + t.getData().getName() + ")(" + t.getEntityId() + ")";
            } else {
                str = t.getData().getId() + "(" + t.getEntityId() + ")";
            }
            return str;
        }
    }
    
}
