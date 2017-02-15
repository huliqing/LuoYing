/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit;

import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import name.huliqing.editor.Editor;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.manager.UIManager;
import name.huliqing.fxswing.Jfx;

/**
 * 
 * @author huliqing
 * @param <T>
 */
public abstract class JfxAbstractEdit<T extends JmeEdit> implements JfxEdit<T>, EventHandler{

    private static final Logger LOG = Logger.getLogger(JfxAbstractEdit.class.getName());

    protected Editor editor;
    protected T jmeEdit;
    protected boolean editInitialized;
    protected boolean jfxInitialized;
    
    /**
     * 当前JFX编辑器的本地根节点
     */
    protected final VBox editRoot = new VBox();
    
    public JfxAbstractEdit() {
        editRoot.setPadding(Insets.EMPTY);
        editRoot.setBackground(Background.EMPTY);
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
                Jfx.getJfxRoot().addEventFilter(DragEvent.DRAG_ENTERED, this);
                Jfx.getJfxRoot().addEventFilter(DragEvent.DRAG_DONE, this);

                UIManager.ZONE_EDIT.getChildren().add(editRoot);
                editRoot.prefWidthProperty().bind(UIManager.ZONE_EDIT.widthProperty());
                editRoot.prefHeightProperty().bind(UIManager.ZONE_EDIT.heightProperty());
                
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
            UIManager.ZONE_EDIT.getChildren().remove(editRoot);
            Jfx.getJfxRoot().removeEventFilter(DragEvent.DRAG_ENTERED, this);
            Jfx.getJfxRoot().removeEventFilter(DragEvent.DRAG_DONE, this);
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
    public final void handle(Event event) {
        if (event.getEventType() == DragEvent.DRAG_ENTERED) {
            jfxOnDragStarted();
        } else if (event.getEventType() == DragEvent.DRAG_DONE) {
            jfxOnDragEnded();
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
    public boolean isModified() {
        return jmeEdit.isModified();
    }

    @Override
    public void setModified(boolean modified) {
        Jfx.runOnJme(() -> {
            jmeEdit.setModified(modified);
        });
    }
    
    @Override
    public void save() {
        Jfx.runOnJme(() -> {jmeEdit.save();});
    }
    
    @Override
    public Editor getEditor() {
        return editor;
    }

    @Override
    public Pane getEditRoot() {
        return editRoot;
    }
    
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
