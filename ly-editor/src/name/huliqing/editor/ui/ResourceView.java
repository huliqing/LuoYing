/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 *
 * @author huliqing
 */
public class ResourceView extends TabPane {
    
    private final Tab assetsTab = new Tab();
    private final Tab luoYingTab = new Tab();
    
    public ResourceView() {
        super();
        
        assetsTab.setContent(new AssetsView());
        assetsTab.setClosable(false);
        assetsTab.setText("Assets");
        luoYingTab.setContent(new YingConfigView());
        luoYingTab.setClosable(false);
        luoYingTab.setText("Config");
        getTabs().addAll(assetsTab, luoYingTab);
        
    }
    
}
