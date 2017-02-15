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
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import name.huliqing.editor.constants.DataFormatConstants;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.component.ComponentDefine;

/**
 *
 * @author huliqing
 */
public class ComponentsForm extends ListView<ComponentDefine> {
//    private static final Logger LOG = Logger.getLogger(ComponentsView.class.getName());
    
    public ComponentsForm(List<ComponentDefine> components) {
        
        getItems().addAll(components);

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
        clipboardContent.put(DataFormatConstants.COMPONENT_DEFINE, selected);
        db.setContent(clipboardContent);
        e.consume();
    }

    private void doDragDone(DragEvent e) {
        e.consume(); // 不要直接取消事件传递
//        LOG.log(Level.INFO, "EntityComponents: doDragDone.");
    }
}
