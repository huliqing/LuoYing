/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.components;

import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import name.huliqing.editor.constants.StyleConstants;

/**
 *
 * @author huliqing
 */
public abstract class AbstractComponents {
    
    private final TitledPane body = new TitledPane();
    protected boolean layoutBuilded;
    
    public AbstractComponents(String title) {
        body.setText(title);
        body.setPrefHeight(300);
    }

    public Node getNode() {
        if (!layoutBuilded) {
            body.setId(StyleConstants.ID_COMPONENTS);
            body.setContent(buildLayout());
            layoutBuilded = true;
        }
        return body;
    }
    
    protected abstract Node buildLayout();
}
