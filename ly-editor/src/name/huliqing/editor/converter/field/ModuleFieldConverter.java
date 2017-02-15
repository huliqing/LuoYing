/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.field;

import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.Callback;
import name.huliqing.editor.component.ComponentDefine;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.converter.SimpleFieldConverter;
import name.huliqing.editor.edit.scene.JfxSceneEdit;
import name.huliqing.editor.manager.ComponentManager;
import name.huliqing.editor.ui.ComponentSearch;
import name.huliqing.editor.ui.utils.JfxUtils;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.data.ModuleData;

/**
 *
 * @author huliqing
 */
public class ModuleFieldConverter extends SimpleFieldConverter<JfxSceneEdit, EntityData> {

    private final VBox layout = new VBox();
    private final Button addBtn = new Button("", JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_ADD));
    private final ListView<ModuleData> listView = new ListView();
    
    private final ComponentSearch<ComponentDefine> componentInput = new ComponentSearch();
    
    public ModuleFieldConverter() {
        layout.setPadding(Insets.EMPTY);
        layout.getChildren().add(addBtn);
        layout.getChildren().add(listView);
        
        listView.setPrefHeight(160);
//        listView.setPrefWidth(180);
        listView.setCellFactory(new Callback<ListView<ModuleData>, ListCell<ModuleData>>() {
            @Override
            public ListCell<ModuleData> call(ListView<ModuleData> param) {
                return new ListCell<ModuleData>(){
                    @Override
                    protected void updateItem(ModuleData item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(null);
                        setGraphic(null);
                        if (!empty) {
                            setText(item.getId());
                        }
                    }
                };
            }
        });
        
        componentInput.setComponents(ComponentManager.getComponents("entity"));
        addBtn.setOnAction(e -> {
            componentInput.show(addBtn, -10, -10);
        });
    }
    
    @Override
    protected void updateUI() {
        List<ModuleData> moduleDatas = data.getModuleDatas();
        listView.getItems().clear();
        if (moduleDatas != null) {
            listView.getItems().addAll(moduleDatas);
        }
    }

    @Override
    protected Node createLayout() {
        return layout;
    }
    
}
