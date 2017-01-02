/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import com.jme3.system.AppSettings;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import name.huliqing.editor.ui.AssetsView;
import name.huliqing.editor.ui.MenuView;
import name.huliqing.editor.ui.ToolBarView;
import name.huliqing.editor.ui.layout.SimpleLayout;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class Starter {
    
    private Scene scene;
    
    public static void main(String[] args) {
        new Starter().start();
    }
    
    private void start() {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1280, 720);
        settings.setFrameRate(60);
        settings.setSamples(4);
        
        Jfx.create(Editor.class.getName(), settings);
        Jfx.getMainFrame().setLocationRelativeTo(null);
        Jfx.getMainFrame().setVisible(true);
        Jfx.runOnJfx(() -> {
            Jfx.getJfxRoot().getChildren().add(createScene());
        });
        
    }
    
    private Node createScene() {
        MenuView menuView = new MenuView();
        AssetsView assetView = new AssetsView();
        
        VBox editPane = new VBox();
        editPane.setBackground(Background.EMPTY);

        ToolBarView toolbarView = new ToolBarView();
        
        SimpleLayout layout = new SimpleLayout(Jfx.getJfxRoot());
        layout.setZones(menuView, assetView, editPane, toolbarView);
        
        Jfx.getBindingController().bindCanvasToJfxRegion(Jfx.getJmeCanvas(), editPane);
        
        return layout;
    }
 
}
