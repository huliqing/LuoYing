/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import name.huliqing.editor.manager.EditManager;

/**
 *
 * @author huliqing
 */
public class YingConfigView extends ScrollPane {
    
    public YingConfigView() {
        Button btn = new Button("test");
        setContent(btn);
        
        btn.setOnAction(e -> {
            EditManager.openTestFormView();
        });
    }
}
