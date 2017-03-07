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
import name.huliqing.editor.ui.utils.SearchListView;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class ListViewPopup<T> {
    
    private final Popup popup = new Popup();
    private final SearchListView<T> searchListView = new SearchListView(new ListView<T>());
    
    public ListViewPopup() {
        this(null);
    }

    public ListViewPopup(List<T> items) {
        if (items != null) {
            setItems(items);
        }
        
        popup.getContent().add(searchListView);
        popup.setAutoHide(true);
        
        searchListView.getListView().setCellFactory((ListView<T> param) -> new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                setGraphic(null);
                if (!empty && item != null) {
                    setText(item.toString());
                }
            }
        });
        // 匹配检查的时候要用字符串转换器
        searchListView.setPrefWidth(250);
        searchListView.setPrefHeight(250);
        searchListView.setEffect(new DropShadow());
    }
    
    public void setItems(List<T> items) {
        if (items == null) 
            return;
        searchListView.setAllItems(items);
    }
    
    public void setConverter(SearchListView.Converter<T> converter) {
        searchListView.setConverter(converter);
    }
    
    public SearchListView getView() {
        return searchListView;
    }
    
    public ListView<T> getListView() {
        return searchListView.getListView();
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
