/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import name.huliqing.editor.manager.ComponentManager;

/**
 *
 * @author huliqing
 */
public class ResourceView extends TabPane {
    
    private final Tab assetsTab = new Tab();
    private final Tab luoYingTab = new Tab();
    private final Tab components = new Tab();
    
    public ResourceView() {
        super();
        
        assetsTab.setContent(new AssetsView());
        assetsTab.setClosable(false);
        assetsTab.setText("Assets");
        
        ComponentsView cv = new ComponentsView("Entities", ComponentManager.getComponents("Entity"));
        components.setContent(cv);
        components.setClosable(false);
        components.setText("Components");
        
        luoYingTab.setContent(new YingConfigView());
        luoYingTab.setClosable(false);
        luoYingTab.setText("Config");
        
        getTabs().add(assetsTab);
        getTabs().add(components);
        getTabs().add(luoYingTab);
        
    }
    
}
