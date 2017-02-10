/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import javafx.geometry.Insets;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import name.huliqing.editor.manager.ComponentManager;

/**
 *
 * @author huliqing
 */
public class ResourceView extends VBox{
    
    private final TitledPane assetsPanel = new TitledPane();
    private final TitledPane componentsPanel = new TitledPane();
    private final TitledPane testPanel = new TitledPane();
    
    public ResourceView() {
        super();
        
        assetsPanel.setContent(new AssetsView());
        assetsPanel.setText("Assets");
        
        ComponentsView cv = new ComponentsView("Entities", ComponentManager.getComponents("Entity"));
        componentsPanel.setContent(cv);
        componentsPanel.setText("Components");
        
        testPanel.setContent(new TestView());
        testPanel.setText("Test");
        testPanel.setExpanded(false);
        
        getChildren().addAll(assetsPanel, componentsPanel, testPanel);
        setPadding(Insets.EMPTY);
        setStyle("-fx-background-color: lightgray");
    }
    
}
