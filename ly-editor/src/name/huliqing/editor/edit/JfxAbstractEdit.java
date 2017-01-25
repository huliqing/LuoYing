/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit;

import java.util.logging.Logger;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import name.huliqing.editor.Editor;
import name.huliqing.editor.manager.EditManager;
import name.huliqing.fxswing.Jfx;

/**
 * 
 * @author huliqing
 * @param <T>
 */
public abstract class JfxAbstractEdit<T extends JmeEdit> implements JfxEdit<T> {

    private static final Logger LOG = Logger.getLogger(JfxAbstractEdit.class.getName());

    protected Editor editor;
    protected T jmeEdit;
    protected boolean editInitialized;
    protected boolean jfxInitialized;
    
    // 全局JFX编辑器根节点root
    private Pane jfxEditZone; 
    // 当前JFX编辑器的根节点
    protected final Pane editRoot = new VBox();
    
    public JfxAbstractEdit() {
        editRoot.addEventFilter(DragEvent.DRAG_ENTERED, (DragEvent event) -> {
            jfxOnDragStarted();
        });
        editRoot.addEventFilter(DragEvent.DRAG_DONE, (DragEvent event) -> {
            jfxOnDragEnded();
        });
        editRoot.setStyle("-fx-padding:0;");
    }

    @Override
    public final void initialize(Editor editor) {
        if (editInitialized || jfxInitialized) {
            throw new IllegalArgumentException();
        }
        this.editor = editor;
        // 先初始化3D编辑器,再初始化UI
        Jfx.runOnJme(() -> {
            if (jmeEdit != null) {
                jmeEdit.initialize(editor);
                editInitialized = true;
                LOG.info("<<<<JmeEditInitialized");
            }
            Jfx.runOnJfx(() -> {
                jfxEditZone = EditManager.getJfxEditZone();
                jfxEditZone.getChildren().add(editRoot);
                editRoot.prefWidthProperty().bind(jfxEditZone.widthProperty());
                editRoot.prefHeightProperty().bind(jfxEditZone.heightProperty());
                jfxInitialize();
                jfxInitialized = true;
                LOG.info("<<<<JFXEditInitialized");
            });
        });
    }

    @Override
    public void update(float tpf) {
        if (editInitialized) {
            jmeEdit.update(tpf);
        }
    }

    @Override
    public final void cleanup() {
        // 先清理jfx
        if (jfxInitialized) {
            jfxCleanup();
            editRoot.prefWidthProperty().unbind();
            editRoot.prefHeightProperty().unbind();
            jfxEditZone.getChildren().remove(editRoot);
            LOG.info(">>>>----JFXEditCleanup");
        }
        // 再清理3d编辑器
        if (editInitialized) {
            Jfx.runOnJme(() -> {
                jmeEdit.cleanup();
                LOG.info(">>>>----JmeEditCleanup");
            });            
        }
    }

    @Override
    public void undo() {
        if (jmeEdit != null) {
            Jfx.runOnJme(() -> {
                jmeEdit.undo();
            });
        }
    }

    @Override
    public void redo() {
        if (jmeEdit != null) {
            Jfx.runOnJme(() -> {
                jmeEdit.redo();
            });
        }
    }
    
    @Override
    public void addUndoRedo(UndoRedo ur) {
        if (jmeEdit != null) {
            Jfx.runOnJme(() -> {
                jmeEdit.addUndoRedo(ur);
            });
        }
    }
    
    @Override
    public Editor getEditor() {
        return editor;
    }    
    
    /**
     * 获取属性面板
     * @return 
     */
    public abstract Pane getPropertyPanel();
    
    /**
     * 当监听到有鼠标拖放事件时该方法被调用
     */
    protected void jfxOnDragStarted() {
//        System.out.println("jfxOnDragStarted");
        // 由子类实现
    }
    
    /**
     * 当监听到鼠标拖放事件结束时该方法被调用。
     */
    protected void jfxOnDragEnded() {
        // 由子类实现
//        System.out.println("jfxOnDragEnded");
    }
    
    /**
     * 初始化JFX界面
     */
    protected abstract void jfxInitialize();
    
    /**
     * 清理JFX界面
     */
    protected abstract void jfxCleanup();
    
}
