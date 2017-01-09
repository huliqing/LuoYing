/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.scene;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.DataFormatConstants;
import name.huliqing.editor.edit.JfxEditView;
import name.huliqing.editor.manager.ComponentManager.Component;
import name.huliqing.editor.ui.ToolBarView;
import name.huliqing.editor.components.EntityComponents;
import name.huliqing.editor.manager.ConverterManager;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.ui.layout.SceneEditLayout;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public class JfxSceneEditView extends JfxEditView<SceneEditForm>{

    private static final Logger LOG = Logger.getLogger(JfxSceneEditView.class.getName());

    private final Pane root;
    private final SceneEditLayout layout = new SceneEditLayout();
    
    private HBox propertyPanel;
    private VBox editPanel;
    private VBox components;
    private ToolBarView toolbarView;
    
    // editPanel不能完全透明，完全透明则响应不了事件，在响应事件时还需要设置为visible=true
    private final static String STYLE_EDIT_PANEL = "-fx-background-color:rgba(0,0,0,0.01)";
    
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
        
        layout.setZoneComponents(components);
        layout.setZoneEdit(editPanel);
        layout.setZoneProperty(propertyPanel);
        layout.setZoneToolbar(toolbarView);
        layout.buildLayout();
        
        layout.prefWidthProperty().bind(root.widthProperty());
        layout.prefHeightProperty().bind(root.heightProperty());
        
        
        editPanel.setVisible(false);
        editPanel.setStyle(STYLE_EDIT_PANEL);
        editPanel.setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            if (db.hasContent(DataFormatConstants.COMPONENT_ENTITY)) {
                e.acceptTransferModes(TransferMode.ANY);
            }
            e.consume();
        });
        editPanel.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            if (db.hasContent(DataFormatConstants.COMPONENT_ENTITY)) {
                Component c = (Component) db.getContent(DataFormatConstants.COMPONENT_ENTITY);
                e.setDropCompleted(true);
                Jfx.runOnJme(() -> {
                    Entity entity = Loader.load(c.id);
                    form.addEntity(entity);
                });
            }
            e.consume();
        });
        
        EntityComponents ec = new EntityComponents();
        components.getChildren().add(ec.getNode());
        
        DataConverter dc = ConverterManager.createConverter(form.getScene().getData(), null);
        propertyPanel.getChildren().add(dc.getNode());
        
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
    
    @Override
    public void jfxOnDragStarted() {
        super.jfxOnDragStarted();
        editPanel.setVisible(true);
    }

    @Override
    public void jfxOnDragEnded() {
        super.jfxOnDragEnded(); 
        editPanel.setVisible(false);
    }
}
