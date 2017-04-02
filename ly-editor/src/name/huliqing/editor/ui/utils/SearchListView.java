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
package name.huliqing.editor.ui.utils;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.AssetConstants;

/**
 *
 * @author huliqing
 */
public class SearchListView<T> extends VBox {
    
    private final List<T> allItems = new ArrayList();
    private final ObservableList filterItems = FXCollections.observableArrayList();
    
    private final ListView<T> listView;
    
    private final StackPane filterPane = new StackPane();
    private final HBox imageView = new HBox(JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_SEARCH));
    private final TextField inputFilter = new TextField();
    
    public interface Converter<T> {
        String convertToString(T t);
    }
    
    public interface SelectListener<T> {
        void onSelected(T item);
    }
    
    private Converter<T> converter;
    private SelectListener<T> selectListener;
    
    public SearchListView(ListView<T> lv) {
        this.listView = lv;
        
        getChildren().add(listView);
        getChildren().add(filterPane);
        filterPane.getChildren().add(inputFilter);
        filterPane.getChildren().add(imageView);
        filterPane.setAlignment(Pos.CENTER_LEFT);
        
        lv.setOnMouseClicked((MouseEvent event) -> {
            if (selectListener != null) {
                selectListener.onSelected(lv.getSelectionModel().getSelectedItem());
            }
        });
        
        inputFilter.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            updateList();
        });
        inputFilter.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP) {
                    listView.getSelectionModel().selectPrevious();
                } else if (event.getCode() == KeyCode.DOWN) {
                    listView.getSelectionModel().selectNext();
                }
            }
        });
        
        listView.prefWidthProperty().bind(widthProperty());
        listView.prefHeightProperty().bind(heightProperty().subtract(filterPane.heightProperty()));
        filterPane.prefWidthProperty().bind(widthProperty());
        filterPane.setMinHeight(25);
        inputFilter.prefWidthProperty().bind(filterPane.widthProperty());
        inputFilter.prefHeightProperty().bind(filterPane.heightProperty());
        inputFilter.setPadding(new Insets(0, 0, 0, 25));
        imageView.setPadding(new Insets(0, 0, 0, 10));
        imageView.setMinWidth(16);
        imageView.setMaxWidth(16);
        imageView.prefHeightProperty().bind(filterPane.heightProperty());
        imageView.setAlignment(Pos.CENTER);
        
        setPadding(new Insets(5));
        setStyle("-fx-background-color: lightgray;");
        listView.setStyle("-fx-border-width:0;-fx-background-color:transparent;");
    }
    
    private void updateList() {
        String inputLowerCase = inputFilter.getText().trim().toLowerCase();
        filterItems.clear();
        String temp;
        for (T t : allItems) {
            if (converter != null) {
                temp = converter.convertToString(t).toLowerCase();
            } else {
                temp = t.toString().toLowerCase();
            }
            if (temp.contains(inputLowerCase)) {
                filterItems.add(t);
            }
        }
        listView.setItems(filterItems);
    }
    
    /**
     * 设置所有的数据列表
     * @param items 
     */
    public void setAllItems(List<T> items) {
        allItems.clear();
        allItems.addAll(items);
        updateList();
    }
    
    /**
     * 设置一个对象转换器，用于将对象转换为字符串(默认情况下SearchListView直接将对象进行toString以便进行搜索匹配
     * 检查)
     * @param converter 
     */
    public void setConverter(Converter<T> converter) {
        this.converter = converter;
    }

    public void setSelectListener(SelectListener<T> selectListener) {
        this.selectListener = selectListener;
    }

    public ListView<T> getListView() {
        return listView;
    }
}
