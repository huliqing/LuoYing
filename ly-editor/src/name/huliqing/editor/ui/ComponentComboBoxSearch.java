/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import name.huliqing.editor.component.ComponentDefine;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.ui.utils.AutoCompleteComboBoxListener;
import name.huliqing.editor.ui.utils.JfxUtils;

/**
 * @deprecated 使用ComponentSearch代替
 * @author huliqing
 */
public class ComponentComboBoxSearch extends HBox {
    
    private final AutoCompleteComboBoxListener<ComponentDefine> ac;
    private final ComboBox<ComponentDefine> componentBox = new ComboBox();
    
    private final Button addBtn = new Button();
    private final Button removeBtn = new Button();
    
    public ComponentComboBoxSearch() {
        addBtn.setGraphic(JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_ADD));
        removeBtn.setGraphic(JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_SUBTRACT));
        
        getChildren().add(componentBox);
        getChildren().add(addBtn);
        getChildren().add(removeBtn);
        
        componentBox.setCellFactory((ListView<ComponentDefine> param) -> new ListCell<ComponentDefine>() {
            @Override
            protected void updateItem(ComponentDefine item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);
                setText(empty ? null : item.getId());
            }
        });
        componentBox.setConverter(new StringConverter<ComponentDefine>() {
            @Override
            public String toString(ComponentDefine object) {
                if (object != null) {
                    return object.getId();
                } 
                return null;
            }
            @Override
            public ComponentDefine fromString(String string) {
                for (ComponentDefine cd : componentBox.getItems()) {
                    if (cd.getId().equals(string))
                        return cd;
                }
                return null;
            }
        });
        
        setPadding(Insets.EMPTY);
        
        ac = new AutoCompleteComboBoxListener(componentBox);
    }
    
    /**
     * 设置可选择的组件列表
     * @param cds 
     */
    public void setComponentDefines(List<ComponentDefine> cds) {
        ac.setItems(cds);
    }
    
    /**
     * 获取当前选择的组件.
     * @return 
     */
    public ComponentDefine getValue() {
        return componentBox.getValue();
    }

    /**
     * 获取"添加"按钮
     * @return 
     */
    public Button getAddBtn() {
        return addBtn;
    }

    /**
     * 获取"减少"按钮
     * @return 
     */
    public Button getRemoveBtn() {
        return removeBtn;
    }
    
}
