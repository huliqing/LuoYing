/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.scene;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import name.huliqing.editor.edit.JfxEditView;
import name.huliqing.editor.ui.ToolBarView;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class JfxSceneEditView extends JfxEditView<SceneEditForm>{

    private static final Logger LOG = Logger.getLogger(JfxSceneEditView.class.getName());

    private final Pane root;
    
    private final BorderPane layout = new BorderPane();
    private HBox propertyPanel; // left
    private VBox editPanel;   // center
    private VBox components; // right
    private ToolBarView toolbarView; // bottom
    
    public JfxSceneEditView(Pane root) {
        this.root = root;
        this.form = new SceneEditForm();
    }
    
    @Override
    protected void jfxInitialize() {
        root.getChildren().add(layout);
        propertyPanel = new HBox();
        editPanel = new VBox();
        components = new VBox();
        toolbarView = new ToolBarView(form);
        layout.setLeft(propertyPanel);
        layout.setCenter(editPanel);
        layout.setRight(components);
        layout.setBottom(toolbarView);
        
//        propertyPanel.getChildren().add(new Label("top"));
        propertyPanel.getChildren().add(new Button("left"));
        components.getChildren().add(new Button("right"));
        
        components.setPrefWidth(200);
        propertyPanel.setPrefWidth(100);
        toolbarView.setPrefHeight(30);
        layout.prefWidthProperty().bind(root.widthProperty());
        layout.prefHeightProperty().bind(root.heightProperty());
        editPanel.prefWidthProperty().bind(layout.widthProperty().subtract(propertyPanel.widthProperty()).subtract(components.widthProperty()));
        editPanel.prefHeightProperty().bind(layout.heightProperty().subtract(toolbarView.heightProperty()));
        
        Jfx.jfxForceUpdate();
        Jfx.jfxCanvasBind(editPanel);
    }
    
    @Override
    protected void jfxCleanup() {
        root.getChildren().remove(layout);
    }
    
    public void setScene(String sceneId) {
        Jfx.runOnJme(() -> {
            try {
                form.setScene(sceneId);
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Could not load scene! sceneId={0}", sceneId);
            }
        });
    }
}
