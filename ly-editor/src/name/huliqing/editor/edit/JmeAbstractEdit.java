/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.Editor;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.undoredo.UndoRedo;
import name.huliqing.editor.undoredo.UndoRedoManager;

/**
 *
 * @author huliqing
 */
public abstract class JmeAbstractEdit implements JmeEdit {

    protected final UndoRedoManager undoRedoManager = new UndoRedoManager();
    
    protected boolean initialized;
    protected Toolbar toolbar;
    protected List<JmeEditListener> listeners;
    protected Editor editor;
    
    @Override
    public void initialize(Editor editor) {
        if (initialized) {
            throw new IllegalStateException();
        }
        initialized = true; 
        this.editor = editor;
        if (toolbar != null && !toolbar.isInitialized()) {
            toolbar.initialize(this);
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void update(float tpf) {
        if (toolbar != null) {
            toolbar.update(tpf);
        }
    }

    @Override
    public void cleanup() {
        if (toolbar != null && toolbar.isInitialized()) {
            toolbar.cleanup();
        }
        initialized = false;
    }

    @Override
    public void setToolbar(Toolbar newToolbar) {
        if (toolbar != null && toolbar.isInitialized()) {
            toolbar.cleanup();
        }
        toolbar = newToolbar;
        if (isInitialized() && !toolbar.isInitialized()) {
            toolbar.initialize(this);
        }
        if (listeners != null) {
            listeners.forEach(t -> {t.onToolbarChanged(this, newToolbar);});
        }
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void addListener(JmeEditListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<JmeEditListener>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public boolean removeListener(JmeEditListener listener) {
        return listeners != null && listeners.remove(listener);
    }

    @Override
    public Editor getEditor() {
        return editor;
    }
    
    @Override
    public UndoRedoManager getUndoRedoManager() {
        return undoRedoManager;
    }
    
    public void addUndoRedo(UndoRedo undoRedo) {
        undoRedoManager.add(undoRedo);
    }
}
