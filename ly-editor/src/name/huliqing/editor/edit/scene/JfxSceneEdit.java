/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TitledPane;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.DataFormatConstants;
import name.huliqing.editor.edit.JfxAbstractEdit;
import name.huliqing.editor.manager.ComponentManager.Component;
import name.huliqing.editor.ui.ToolBarView;
import name.huliqing.editor.components.EntityComponents;
import name.huliqing.editor.manager.ConverterManager;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.edit.Mode;
import name.huliqing.editor.ui.layout.SceneEditLayout;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.data.SceneData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.editor.edit.SimpleJmeEditListener;
import name.huliqing.editor.edit.select.EntitySelectObj;

/**
 *
 * @author huliqing
 */
public class JfxSceneEdit extends JfxAbstractEdit<SceneEdit> implements SimpleJmeEditListener<EntitySelectObj> {

    private static final Logger LOG = Logger.getLogger(JfxSceneEdit.class.getName());

    private final Pane root;
    private final SceneEditLayout layout = new SceneEditLayout();
    
    private HBox propertyPanel;
    private VBox editPanel;
    private VBox components;
    private ToolBarView toolbarView;
    
    // 场景属性面板的容器
    private final TitledPane scenePropertyPanel = new TitledPane();
    private DataConverter sceneDataConverter;
    
    // editPanel不能完全透明，完全透明则响应不了事件，在响应事件时还需要设置为visible=true
    private final static String STYLE_EDIT_PANEL = "-fx-background-color:rgba(0,0,0,0.01)";
    
    private String sceneId;
    private final List<JfxSceneEditListener<EntitySelectObj>> listeners = new ArrayList();
    // 当前选择中的物体
    private EntitySelectObj entitySelectObj;
    
    public JfxSceneEdit(Pane root) {
        this.root = root;
        this.form = new SceneEdit();
        this.form.addSimpleEditListener(this);
    }
    
    @Override
    protected void jfxInitialize() {
        root.getChildren().add(layout);
        propertyPanel = new HBox();
        editPanel = new VBox();
        components = new VBox();
        toolbarView = new ToolBarView(form);
        propertyPanel.getChildren().add(scenePropertyPanel);
        
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
        
        Jfx.jfxForceUpdate();
        Jfx.jfxCanvasBind(editPanel);
        
        if (sceneId != null) {
            loadScene(sceneId);
        }
    }
    
    @Override
    protected void jfxCleanup() {
        root.getChildren().remove(layout);
    }
    
    public void setScene(String sceneId) {
        this.sceneId = sceneId;
        if (jfxInitialized) {
            loadScene(sceneId);
        }
    }
    
    private void loadScene(String sceneId) {
        Jfx.runOnJme(() -> {
            try {
                SceneData sd = Loader.loadData(sceneId);
                Scene scene = Loader.load(sd);
                form.setScene(scene);
                
                Jfx.runOnJfx(() -> {
                    if (sceneDataConverter != null && sceneDataConverter.isInitialized()) {
                        sceneDataConverter.cleanup();
                    }
                    sceneDataConverter = ConverterManager.createConverter(this, scene.getData(), null);
                    scenePropertyPanel.setText(scene.getData().getId());
                    scenePropertyPanel.setContent(sceneDataConverter.getLayout());
                });
                
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
    
    public void addListener(JfxSceneEditListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public boolean removeListener(JfxSceneEditListener listener) {
        return listeners.remove(listener);
    }
    
    @Override
    public void onModeChanged(Mode mode) {
        Jfx.runOnJfx(() -> {
            listeners.forEach(t -> {
                t.onModeChanged(mode);
            });
        });
    }

    @Override
    public void onSelectChanged(EntitySelectObj selectObj) {
        if (selectObj == this.entitySelectObj) {
            return;
        }
        entitySelectObj = selectObj;
        Jfx.runOnJfx(() -> {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onSelectChanged(entitySelectObj);
            }
        });
    }
    
    public EntitySelectObj getSelected() {
        return form.getSelected();
    }
    
    public void setSelected(EntityData entityData) {
        Jfx.runOnJme(() -> {
            form.setSelected(entityData);
        });
    }
    
    public void reloadEntity(EntityData entityData) {
        Jfx.runOnJme(() -> {
            form.reloadEntity(entityData);
        });
    }
    
    public Pane getPropertyPanel() {
        return propertyPanel;
    }
}
