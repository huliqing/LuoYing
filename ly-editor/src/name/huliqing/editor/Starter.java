/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import com.jme3.system.AppSettings;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import name.huliqing.editor.manager.EditManager;
import name.huliqing.editor.ui.AssetsView;
import name.huliqing.editor.ui.MenuView;
import name.huliqing.editor.ui.layout.MainLayout;
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
            // 创建Jfx主场景
            Pane jfxEditZone = initMainSceneInJfx(Jfx.getJfxRoot());
            // 初始化FormView
            Jfx.runOnJme(() -> {
                initFormViewInJme(jfxEditZone);
            });
        });
        
    }
    
    private Pane initMainSceneInJfx(Pane root) {
        MenuView menuView = new MenuView();
        AssetsView assetView = new AssetsView();
        VBox jfxEditZone = new VBox();
        jfxEditZone.setBackground(Background.EMPTY);
        
        MainLayout mainLayout = new MainLayout(root);
        mainLayout.setZones(menuView, assetView, jfxEditZone);
        root.getChildren().add(mainLayout);
        
        return jfxEditZone;
    }
    
    private void initFormViewInJme(Pane jfxEditZone) {
        EditManager.registerEditZone(jfxEditZone);
        EditManager.openTestFormView();
    }
 
}
