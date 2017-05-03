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

import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.converter.SimpleFieldConverter;
import name.huliqing.editor.edit.scene.JfxSceneEdit;
import name.huliqing.editor.ui.DataProcessorSearch;
import name.huliqing.editor.ui.utils.JfxUtils;
import name.huliqing.editor.ui.utils.SearchListView;
import name.huliqing.editor.ui.utils.SearchListView.Converter;
import name.huliqing.editor.utils.Validator;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 这个转换器用于转换字段类型为: 指向一个场景实体的唯一id的字段。
 * 也就是字段源对象要关联一个目标对象，但不需要关联整个对象的数据(ObjectData)，只要关联它的唯一ID就可以。
 * 源对象可以在目标载入场景后，通过目标对象的唯 一ID从场景中去找到这个目标。
 * @author huliqing
 */
public class UniqueIdConverter extends SimpleFieldConverter<JfxSceneEdit, ObjectData>{

    private final HBox layout = new HBox();
    private final TextField input = new TextField();
    private final Button selectButton = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_SEARCH));
    private final DataProcessorSearch<Entity> searchHelper = new DataProcessorSearch();
    private final SearchListView.Converter<Entity> converter = new StringConverter();
    
    private Long lastValueSaved;
    
    public UniqueIdConverter() {
        input.setStyle(StyleConstants.CSS_CORNER_ROUND_LEFT);
        selectButton.setStyle(StyleConstants.CSS_CORNER_ROUND_RIGHT);
        layout.getChildren().addAll(input, selectButton);
        
        input.focusedProperty().addListener((ObservableValue<? extends Boolean> observable
                , Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                return;
            }
            updateChangedAndSave();
        });
        input.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateChangedAndSave();
            }
        });
        searchHelper.getListView().setOnMouseClicked(e -> {
            searchHelper.hide();
            Entity entity = searchHelper.getListView().getSelectionModel().getSelectedItem();
            if (entity != null) {
                input.setText(entity.getEntityId() + "");
                updateChangedAndSave();
            }
        });
        
        searchHelper.setConverter(converter);
        selectButton.setOnAction(e -> {
            List<Entity> entities = jfxEdit.getJmeEdit().getScene().getEntities();   
            searchHelper.setItems(entities);
            searchHelper.show(input, -10, -10);
        });
        searchHelper.getListView().setCellFactory((ListView<Entity> param) -> {
            ListCell<Entity> lc = new ListCell<Entity>() {
                @Override
                protected void updateItem(Entity item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(null);
                    setGraphic(null);
                    if (!empty && item != null) {
                        setText(converter.convertToString(item));
                    }
                }
            };
            return lc;
        });
    }
    
    private void updateChangedAndSave() {
        String newStrValue = input.getText();
        if (newStrValue == null || newStrValue.trim().isEmpty()) {
            if (lastValueSaved == null) {
                return;
            } else {
                updateAttribute(null);
                addUndoRedo(lastValueSaved, null);
                lastValueSaved = null;
                return;
            }
        }
        
        if (!Validator.isLong(newStrValue)) {
            return;
        }
        
        Long newValue = name.huliqing.luoying.xml.Converter.getAsLong(newStrValue);
        if (lastValueSaved != null && lastValueSaved.longValue() == newValue.longValue()) {
            return;
        }
        updateAttribute(newValue);
        addUndoRedo(lastValueSaved, newValue);
        lastValueSaved = newValue;
    }
    
    @Override
    protected void updateUI() {
        lastValueSaved = data.getAsLong(field);
        input.setText(lastValueSaved != null ? lastValueSaved + "" : "");
    }

    @Override
    protected Node createLayout() {
        return layout;
    }
    
    private class StringConverter implements Converter<Entity> {
        @Override
        public String convertToString(Entity t) {
            return t.getData().getId() + "(" + t.getData().getName() + ")(" + t.getEntityId() + ")";
        }
    }
    
}
