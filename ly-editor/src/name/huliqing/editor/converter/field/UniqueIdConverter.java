/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.field;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import name.huliqing.editor.converter.SimpleFieldConverter;

/**
 * 物体的唯一ID字段转换器,只显示，不能修改
 * @author huliqing
 */
public class UniqueIdConverter extends SimpleFieldConverter {

    private final TextField layout = new TextField("");
    
    @Override
    protected void updateUI() {
        layout.setText(data.getUniqueId() + "");
    }

    @Override
    protected Node createLayout() {
        return layout;
    }
    
}
