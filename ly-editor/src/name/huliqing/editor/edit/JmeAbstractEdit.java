/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit;

import com.jme3.scene.Node;
import com.jme3.util.SafeArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.Editor;
import name.huliqing.editor.toolbar.Toolbar;

/**
 * @author huliqing
 */
public abstract class JmeAbstractEdit implements JmeEdit {

    private static final Logger LOG = Logger.getLogger(JmeAbstractEdit.class.getName());

    /**
     * 历史记录列表
     */
    protected final UndoRedoManager undoRedoManager = new UndoRedoManager();
    
    /**
     * 标记编辑器是否经过编辑操作
     */
    protected boolean modified;
    
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
    
    protected final Set<SaveAction> saveActions = new LinkedHashSet();
    
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
    public final void save() {
        try {
            // 1.先保存一些特殊的"行为操作",一些特殊的编辑操作需要在编辑器保存之前进行优先保存
            // 比如地形的编辑操作
            if (!saveActions.isEmpty()) {
                for (SaveAction sa : saveActions) {
                    sa.doSave(editor);
                }
                // 不要清理,这些行为在每次编辑器保存的时候都应该被调用, 除非由外部自己去调用移除。
                // saveActions.clear();
            }
            
            // 2.编辑器保存操作
            doSaveEdit();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 添加一个”保存操作“行为到列表，这些行为会在每次编辑器保存编辑结果时（之前）被调用,
     * 用于优先保存一些操作行为结果, 一个”保存操作“行为一旦添加将不再移除,直到被移除出列表
     * {@link #removeSaveAction(name.huliqing.editor.edit.SaveAction) }<br>
     * 注：多次调用这个方法添加同一个行为不会导致重复添加.
     * @param saveAction 
     */
    public void addSaveAction(SaveAction saveAction) {
        saveActions.add(saveAction);
    }
    
    /**
     * 移除一个”保存操作“行为。
     * @param saveAction 
     * @return  
     */
    public boolean removeSaveAction(SaveAction saveAction) {
        return saveActions.remove(saveAction);
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

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean modified) {
        this.modified = modified;
    }
    
    /**
     * 保存编辑结果
     */
    protected abstract void doSaveEdit();
    
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
