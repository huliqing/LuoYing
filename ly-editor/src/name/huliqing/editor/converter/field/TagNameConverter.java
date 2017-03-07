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
 * TagName字段转换，只作为只读。
 * @author huliqing
 */
public class TagNameConverter extends SimpleFieldConverter {

    private final TextField content = new TextField("");

    public TagNameConverter() {}
    
    @Override
    protected Node createLayout() {
        return content;
    }
    
    @Override
    protected void updateUI() {
        content.setText(data.getTagName());
    }
    
}
