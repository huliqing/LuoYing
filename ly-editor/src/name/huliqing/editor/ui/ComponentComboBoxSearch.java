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
