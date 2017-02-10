/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;

/**
 *
 * @author huliqing
 */
public class OutputForm extends TextArea {
    
    
    public OutputForm() {
        ContextMenu cm = new ContextMenu();
        setContextMenu(cm);

        MenuItem clean = new MenuItem(Manager.getRes(ResConstants.POPUP_CLEAR));
        clean.setOnAction(e -> {
            setText("");
        });

        cm.getItems().add(clean);
        
    }
}
