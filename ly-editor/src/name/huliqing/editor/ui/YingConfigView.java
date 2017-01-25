/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import name.huliqing.editor.manager.EditManager;

/**
 *
 * @author huliqing
 */
public class YingConfigView extends ScrollPane {
    
    private final VBox layout = new VBox();
    
    public YingConfigView() {
        setContent(layout);
        
        Button btn = new Button("test");
        btn.setOnAction(e -> {
            EditManager.openTestFormView();
        });
        
        Button te = new Button("TerrainEdit");
        te.setOnAction(e -> {
            EditManager.openTerrainEdit();
        });
        
        layout.getChildren().addAll(btn, te);
        
    }
}
