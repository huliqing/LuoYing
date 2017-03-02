/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author huliqing
 * @param <T>
 */
public class ComponentSearch<T extends ComponentDefine> {
    
    private final Popup popup = new Popup();
    private final SearchListView<T> componentView = new SearchListView(new ListView<T>());

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
