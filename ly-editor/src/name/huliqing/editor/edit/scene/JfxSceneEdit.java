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
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import name.huliqing.editor.components.Component;
import name.huliqing.editor.constants.DataFormatConstants;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.ConverterManager;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.edit.JfxSimpleEdit;
import name.huliqing.editor.edit.Mode;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.data.SceneData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.editor.edit.SimpleJmeEditListener;
import name.huliqing.editor.edit.controls.ControlTile;
import name.huliqing.editor.edit.select.EntitySelectObj;
import name.huliqing.editor.manager.Manager;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.scene.SceneListener;

/**
 * 场景编辑器
 * @author huliqing
 */
public class JfxSceneEdit extends JfxSimpleEdit<SceneEdit> 
        implements SimpleJmeEditListener<ControlTile>, SceneListener {

    private static final Logger LOG = Logger.getLogger(JfxSceneEdit.class.getName());

    // 场景属性面板的容器
    private final TitledPane scenePropertyPanel = new TitledPane();
    private DataConverter sceneDataConverter;
    
    private String sceneId;
    private final List<JfxSceneEditListener<ControlTile>> listeners = new ArrayList();
    // 当前选择中的物体
    private ControlTile entitySelectObj;
    
    // 用于显示删除按钮
    private final ContextMenu delPop = new ContextMenu();
    private final MenuItem delBtn = new MenuItem(Manager.getRes(ResConstants.POPUP_DELETE));
    private EntitySelectObj<Entity> delTarget;
    
    // 鼠标最近一次拖放到editPanel上的坐标位置
    private double lastDragXPos;
    private double lastDragYPos;
    
    public JfxSceneEdit() {
        this.jmeEdit = new SceneEdit(this);
        this.jmeEdit.addSimpleEditListener(this);
        
        delPop.getItems().addAll(delBtn);
        delBtn.setOnAction(e -> {
            if (delTarget != null) {
                removeEntity(delTarget.getTarget().getData());
                delTarget = null;
                // 删除后重新将焦点定位到canvas上
                Jfx.requestFocusCanvas();
            }
        });
    }
    
    @Override
    protected void jfxInitialize() {
        super.jfxInitialize();
        propertyPanel.getChildren().add(scenePropertyPanel);
        
        if (sceneId != null) {
            loadScene(sceneId);
        }
    } 
    
    @Override
    protected void jfxCleanup() {
        propertyPanel.getChildren().remove(scenePropertyPanel);
        super.jfxCleanup();
    }
    
    @Override
    protected void onDragOver(DragEvent e) {
        Dragboard db = e.getDragboard();
        if (db.hasContent(DataFormatConstants.COMPONENT_ENTITY)) {
            e.acceptTransferModes(TransferMode.ANY);
        }
        e.consume();
    }

    @Override
    protected void onDragDropped(DragEvent e) {
        Dragboard db = e.getDragboard();
        if (db.hasContent(DataFormatConstants.COMPONENT_ENTITY)) {
            Component c = (Component) db.getContent(DataFormatConstants.COMPONENT_ENTITY);
            e.setDropCompleted(true);
            e.consume();

            // 当拖动物体到3D场景的时候必须把JFX中的鼠标坐标转到到JME中的坐标.
            // 不能直接用jme场景中获取鼠标坐标的方式，因为JME是放在awt canvas上的，这时还未获得焦点，直接用
            // JME中的方式获取鼠标坐标会错位。
            lastDragXPos = e.getX();
            lastDragYPos = e.getY();

            // 必须隔一帧，即等拖放操作完成执行后再弹窗.
            // 这里不能直接执行CustomDialog,因为当CustomDialog为模态时会锁住窗口，这会导致当第二次拖放操作时发生:
            // Exception in thread "JavaFX Application Thread" java.lang.IllegalArgumentException: 
            // Key already associated with a running event loop: com.sun.javafx.tk.quantum.EmbeddedSceneDnD@4535965c
            Jfx.runOnJfx(() -> {
                c.create(this);
            });
        }
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
                jmeEdit.setScene(scene);
                
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
    public void onSelect(ControlTile selectObj) {
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
        return jmeEdit.getSelected();
    }
    
    public void setSelected(EntityData ed) {
        Jfx.runOnJme(() -> {
            jmeEdit.setSelected(ed);
        });
    }
    
    public void reloadEntity(EntityData ed) {
        Jfx.runOnJme(() -> {
            jmeEdit.reloadEntity(ed);
        });
    }
    
    public void addEntity(EntityData ed) {
        Jfx.runOnJme(() -> {
            Vector2f pos = new Vector2f((float)lastDragXPos, (float)(editPanel.getHeight() - lastDragYPos));
            jmeEdit.addEntityOnCursor(ed, pos);
        });
    }
    
    public void removeEntity(EntityData ed) {
        Jfx.runOnJme(() -> {
            jmeEdit.removeEntity(ed);
        });
    }

    @Override
    public void onSceneLoaded(Scene scene) {
        // ignore
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
