/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import name.huliqing.editor.components.Component;
import name.huliqing.editor.constants.DataFormatConstants;
import name.huliqing.editor.constants.StyleConstants;

/**
 *
 * @author huliqing
 */
public class ComponentsView extends TitledPane {
//    private static final Logger LOG = Logger.getLogger(ComponentsView.class.getName());
    
    private final ListView<Component> listView = new ListView();

    public ComponentsView(String title, List<Component> components) {
        
        listView.getItems().clear();
        listView.getItems().addAll(components);

        listView.setCellFactory((ListView<Component> param) -> new ListCell<Component>() {
            @Override
            protected void updateItem(Component item, boolean empty) {
                super.updateItem(item, empty);
                String name = null;
                Node icon = null;
                if (item != null && !empty) {
                    name = item.getName();
                }
                setText(name);
                setGraphic(icon);
            }
        });

        listView.setOnDragDetected(this::doDragDetected);
        listView.setOnDragDone(this::doDragDone);
        
        setId(StyleConstants.ID_COMPONENTS);
        setText(title);
        setContent(listView);
    }

    private Component getMainSelectItem() {
        ObservableList<Component> items = listView.getSelectionModel().getSelectedItems();
        if (items.isEmpty()) {
            return null;
        }
        for (Component c : items) {
            if (c != null) {
                return c;
            }
        }
        return null;
    }

    private void doDragDetected(MouseEvent e) {
//        LOG.log(Level.INFO, "EntityComponents: doDragDetected.");
        Component selected = getMainSelectItem();
        if (selected == null) {
            e.consume();
            return;
        }
        Dragboard db = listView.startDragAndDrop(TransferMode.ANY);
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.put(DataFormatConstants.COMPONENT_ENTITY, selected);
        db.setContent(clipboardContent);
        e.consume();
    }

    private void doDragDone(DragEvent e) {
        e.consume(); // 不要直接取消事件传递
//        LOG.log(Level.INFO, "EntityComponents: doDragDone.");
    }
}
