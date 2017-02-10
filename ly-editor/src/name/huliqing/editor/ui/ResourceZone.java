/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import javafx.geometry.Insets;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.ComponentManager;
import name.huliqing.editor.manager.Manager;

/**
 *
 * @author huliqing
 */
public class ResourceZone extends VBox{
    
    private final TitledPane assetsPanel = new TitledPane();
    private final TitledPane componentsPanel = new TitledPane();
    private final TitledPane testPanel = new TitledPane();
    
    public ResourceZone() {
        super();
        
        assetsPanel.setContent(new AssetsForm());
        assetsPanel.setText(Manager.getRes(ResConstants.FORM_ASSETS_TITLE));
        
        ComponentsForm cv = new ComponentsForm("Entities", ComponentManager.getComponents("Entity"));
        componentsPanel.setContent(cv);
        componentsPanel.setText(Manager.getRes(ResConstants.FORM_COMPONENTS_TITLE));
        
        testPanel.setContent(new TestForm());
        testPanel.setText("Test");
        testPanel.setExpanded(false);
        
        getChildren().addAll(assetsPanel, componentsPanel, testPanel);
        setPadding(Insets.EMPTY);
        setStyle("-fx-background-color: lightgray");
    }
    
}
