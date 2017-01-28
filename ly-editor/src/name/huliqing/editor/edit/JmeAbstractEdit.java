/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit;

import com.jme3.scene.Node;
import com.jme3.util.SafeArrayList;
import java.util.List;
import name.huliqing.editor.Editor;
import name.huliqing.editor.toolbar.Toolbar;

/**
 * @author huliqing
 */
public abstract class JmeAbstractEdit implements JmeEdit {

    protected final UndoRedoManager undoRedoManager = new UndoRedoManager();
    
    /**
     * 主编辑器APP
     */
    protected Editor editor;
    
    /**
     * 主工具栏
     */
    protected Toolbar toolbar;
    
    /**
     * 扩展工具栏列表
     */
    protected final SafeArrayList<Toolbar> extToolbars = new SafeArrayList<Toolbar>(Toolbar.class);
    
    /**
     * 编辑场景的根节点
     */
    protected Node editRoot;
    
    protected boolean initialized;
    
    @Override
    public void initialize(Editor editor) {
        if (initialized) {
            throw new IllegalStateException();
        }
        initialized = true; 
        this.editor = editor;
        this.editRoot = new Node();
        this.editor.getRootNode().attachChild(editRoot);
        this.toolbar = createToolbar();
        if (toolbar != null && !toolbar.isInitialized()) {
            toolbar.initialize();
        }
        
        List<Toolbar> tempExtToolbars = createExtToolbars();
        if (tempExtToolbars != null && !tempExtToolbars.isEmpty()) {
            extToolbars.addAll(tempExtToolbars);
            for (Toolbar t : extToolbars.getArray()) {
                if (!t.isInitialized()) {
                    t.initialize();
                }
            }
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void update(float tpf) {
        if (toolbar != null && toolbar.isEnabled()) {
            toolbar.update(tpf);
        }
        for (Toolbar t : extToolbars.getArray()) {
            if (t.isEnabled()) {
                t.update(tpf);
            }
        }
    }

    @Override
    public void cleanup() {
        for (Toolbar t : extToolbars.getArray()) {
            if (t.isInitialized()) {
                t.cleanup();
            }
        }
        extToolbars.clear();
        
        if (toolbar != null && toolbar.isInitialized()) {
            toolbar.cleanup();
            toolbar = null;
        }
        
        if (editRoot != null) {
            editRoot.removeFromParent();
            editRoot = null;
        }
        
        initialized = false;
    }

    @Override
    public void undo() {
        undoRedoManager.undo();
    }

    @Override
    public void redo() {
        undoRedoManager.redo();
    }
    
    @Override
    public void addUndoRedo(UndoRedo undoRedo) {
        undoRedoManager.add(undoRedo);
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public List<Toolbar> getExtToolbars() {
        return extToolbars;
    }

    @Override
    public Editor getEditor() {
        return editor;
    }
    
    /**
     * 创建主工具栏,如果没有主工具栏，则返回null即可
     * @return 
     */
    protected abstract Toolbar createToolbar();
    
    /**
     * 创建扩展工具栏,如果没有扩展工具栏，则可以返回null或者空列表
     * @return 
     */
    protected abstract List<Toolbar> createExtToolbars();
}
