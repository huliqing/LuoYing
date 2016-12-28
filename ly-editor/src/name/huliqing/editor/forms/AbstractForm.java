/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.forms;

import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.Editor;

/**
 *
 * @author huliqing
 */
public abstract class AbstractForm implements Form {

    protected Editor editor;
    protected boolean initialized;
    protected Toolbar toolbar;

    @Override
    public void initialize(Editor editor) {
        if (initialized) {
            throw new IllegalArgumentException();
        }
        initialized = true; 
        this.editor = editor;
        if (toolbar != null && !toolbar.isInitialized()) {
            toolbar.setForm(this);
            toolbar.initialize();
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
    public Editor getEditor() {
        return editor;
    }

    @Override
    public void setToolbar(Toolbar newToolbar) {
        if (toolbar != null && toolbar.isInitialized()) {
            toolbar.cleanup();
        }
        toolbar = newToolbar;
        if (isInitialized() && !toolbar.isInitialized()) {
            toolbar.setForm(this);
            toolbar.initialize();
        }
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

}
