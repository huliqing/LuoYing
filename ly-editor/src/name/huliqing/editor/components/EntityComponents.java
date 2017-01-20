/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.components;

import java.util.List;
import java.util.logging.Logger;
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
import name.huliqing.editor.manager.ComponentManager;
import name.huliqing.editor.manager.ComponentManager.Component;
import name.huliqing.editor.manager.EditManager;

/**
 *
 * @author huliqing
 */
public class EntityComponents extends AbstractComponents {

    private static final Logger LOG = Logger.getLogger(EntityComponents.class.getName());

    private List<Component> COMPONENTS;
    private final ListView<Component> listView = new ListView();

    public EntityComponents() {
        super("Entity Components");
    }

    @Override
    public Node buildLayout() {
        COMPONENTS = ComponentManager.getComponents("SceneEntity");
        listView.getItems().clear();
        listView.getItems().addAll(COMPONENTS);

        listView.setCellFactory((ListView<Component> param) -> new ListCell<Component>() {
            @Override
            protected void updateItem(Component item, boolean empty) {
                super.updateItem(item, empty);
                String name = null;
                Node icon = null;
                if (item != null && !empty) {
                    name = item.name;
                }
                setText(name);
                setGraphic(icon);
            }
        });

        listView.setOnDragDetected(this::doDragDetected);
        listView.setOnDragDone(this::doDragDone);
        return listView;
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
