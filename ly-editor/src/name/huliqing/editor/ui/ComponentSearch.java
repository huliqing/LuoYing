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
import javafx.geometry.Point2D;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.stage.Popup;
import name.huliqing.editor.component.ComponentDefine;
import name.huliqing.editor.ui.utils.SearchListView;

/**
 * 组件查询器
 * @author huliqing
 * @param <T>
 */
public final class ComponentSearch<T extends ComponentDefine> {
    
    private final Popup popup = new Popup();
    private final SearchListView<T> componentView = new SearchListView(new ListView<>());

    public ComponentSearch() {
        this(null);
    }
    
    public ComponentSearch(List<T> items) {
        if (items != null) {
            setComponents(items);
        }
        
        popup.getContent().add(componentView);
        popup.setAutoHide(true);
        
        componentView.getListView().setCellFactory((ListView<T> param) -> new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getId());
                }
            }
        });
        // 匹配检查的时候要用字符串转换器
        componentView.setConverter((T t) -> t.getId());
        componentView.setPrefWidth(250);
        componentView.setPrefHeight(250);
        componentView.setEffect(new DropShadow());
    }

    public void setComponents(List<T> items) {
        if (items == null) 
            return;
        componentView.setAllItems(items);
    }
    
    public SearchListView getView() {
        return componentView;
    }
    
    public ListView<T> getListView() {
        return componentView.getListView();
    }

    public void show(Region node, double offsetX, double offsetY) {
        Point2D txtCoords = node.localToScene(0.0, 0.0);
        popup.show(node
                , txtCoords .getX() + node.getScene().getX() + node.getScene().getWindow().getX() + offsetX
                , txtCoords .getY() + node.getScene().getY()  + node.getScene().getWindow().getY() + offsetY + node.heightProperty().getValue());
    }
    
    public void hide() {
        popup.hide();
    }
}
