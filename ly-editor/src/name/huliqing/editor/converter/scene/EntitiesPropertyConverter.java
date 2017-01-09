/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.scene;

import java.util.List;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import name.huliqing.editor.converter.AbstractPropertyConverter;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.manager.ConverterManager;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.data.SceneData;

/**
 *
 * @author huliqing
 */
public class EntitiesPropertyConverter extends AbstractPropertyConverter<SceneData> {

    private final ListView<EntityData> listView = new ListView();
    
    @Override
    public void initialize(DataConverter<SceneData> parent, String property) {
        super.initialize(parent, property);
        
        List<EntityData> eds = parent.getData().getEntityDatas();
        if (eds != null) {
            eds.forEach(t -> {
                listView.getItems().add(t);
            });
        }
        listView.autosize();
        listView.setCellFactory(new CellInner());
        root.setContent(listView);
        
        listView.setOnMouseClicked(this::onMouseClicked);
    }

    private void onMouseClicked(MouseEvent e) {
        if (e.getButton() != MouseButton.PRIMARY) {
            return;
        }
        EntityData ed = listView.getSelectionModel().getSelectedItem();
        if (ed != null) {
            DataConverter cd = ConverterManager.createConverter(ed, this);
            ((Pane)parent.getNode().getParent()).getChildren().add(cd.getNode());
        }
    }
    
    private class CellInner implements Callback<ListView<EntityData>, ListCell<EntityData>> {

        @Override
        public ListCell<EntityData> call(ListView<EntityData> param) {
            ListCell<EntityData> lc = new ListCell<EntityData>() {
                @Override
                protected void updateItem(EntityData item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty && item != null) {
                        setText(item.getId() + "(" + item.getUniqueId() + ")");
                    }
                }
            };
            return lc;
        }
        
    }
}
