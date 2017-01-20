/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.scene;

import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
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
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.ConverterManager;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.edit.Mode;
import name.huliqing.editor.ui.layout.SceneEditLayout;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.data.SceneData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.editor.edit.SimpleJmeEditListener;
import name.huliqing.editor.edit.select.EntitySelectObj;
import name.huliqing.editor.manager.Manager;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.scene.SceneListener;

/**
 *
 * @author huliqing
 */
public class JfxSceneEdit extends JfxAbstractEdit<SceneEdit> 
        implements SimpleJmeEditListener<EntitySelectObj>, SceneListener {

    private static final Logger LOG = Logger.getLogger(JfxSceneEdit.class.getName());

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
    
    // 用于显示删除按钮
    private final ContextMenu delPop = new ContextMenu();
    private final MenuItem delBtn = new MenuItem(Manager.getRes(ResConstants.POPUP_DELETE));
    private EntitySelectObj delTarget;
    
    public JfxSceneEdit() {
        this.form = new SceneEdit(this);
        this.form.addSimpleEditListener(this);
        
        delPop.getItems().addAll(delBtn);
        delBtn.setOnAction(e -> {
            if (delTarget != null) {
                removeEntity(delTarget.getObject().getData());
                delTarget = null;
                // 删除后重新将焦点定位到canvas上
                Jfx.requestFocusCanvas();
            }
        });
    }
    
    @Override
    protected void jfxInitialize() {
        editRoot.getChildren().add(layout);
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
        
        layout.prefWidthProperty().bind(editRoot.widthProperty());
        layout.prefHeightProperty().bind(editRoot.heightProperty());
        
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
                // 当拖动物体到3D场景的时候必须把JFX中的鼠标坐标转到到JME中的坐标.
                // 不能直接用jme场景中获取鼠标坐标的方式，因为JME是放在awt canvas上的，这时还未获得焦点，直接用
                // JME中的方式获取鼠标坐标会错位。
                addEntity(Loader.loadData(c.id), e.getX(), editPanel.getHeight() - e.getY());
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
        layout.prefWidthProperty().unbind();
        layout.prefHeightProperty().unbind();
        editRoot.getChildren().remove(layout);
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
                scene.addSceneListener(this);
                form.setScene(scene);
                
                Jfx.runOnJfx(() -> {
                    if (sceneDataConverter != null && sceneDataConverter.isInitialized()) {
                        sceneDataConverter.cleanup();
                    }
                    sceneDataConverter = ConverterManager.createConverter(this, scene.getData());
                    sceneDataConverter.initialize(null);
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
    public void onSelect(EntitySelectObj selectObj) {
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
    
    public Pane getPropertyPanel() {
        return propertyPanel;
    }
    
    public EntitySelectObj getSelected() {
        return form.getSelected();
    }
    
    public void setSelected(EntityData ed) {
        Jfx.runOnJme(() -> {
            form.setSelected(ed);
        });
    }
    
    public void reloadEntity(EntityData ed) {
        Jfx.runOnJme(() -> {
            form.reloadEntity(ed);
        });
    }
    
    private void addEntity(EntityData ed, double x, double y) {
        Jfx.runOnJme(() -> {
            form.addEntityOnCursor(ed, new Vector2f((float)x, (float)y));
        });
    }
    
    public void removeEntity(EntityData ed) {
        Jfx.runOnJme(() -> {
            form.removeEntity(ed);
        });
    }

    @Override
    public void onSceneLoaded(Scene scene) {
    }

    @Override
    public void onSceneEntityAdded(Scene scene, Entity entityAdded) {
        Jfx.runOnJfx(() -> {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onEntityAdded(entityAdded.getData());
            }
        });
    }

    @Override
    public void onSceneEntityRemoved(Scene scene, Entity entityRemoved) {
        Jfx.runOnJfx(() -> {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onEntityRemoved(entityRemoved.getData());
            }
        });
    }
    
    public void showDeleteConfirm(float x, float y, EntitySelectObj entityObj) {
        delTarget = entityObj;
        delPop.show(editPanel, Side.TOP, x - delPop.getWidth() * 0.25, y + delPop.getHeight() * 0.25);
        Jfx.requestFocus(delPop);
    }
}
