/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.formview;

import name.huliqing.editor.Editor;
import name.huliqing.editor.editforms.EditForm;
import name.huliqing.editor.editviews.EditView;

/**
 * @author huliqing
 * @param <F>
 * @param <V>
 */
public abstract class AbstractFormView<F extends EditForm, V extends EditView> implements FormView<F,V>{

    protected Editor editor;
    protected boolean initialized;
    
    protected F form;
    protected V view;
    
    @Override
    public void initialize(Editor editor) {
        if (initialized) {
            throw new IllegalStateException();
        }
        initialized = true; 
        this.editor = editor;
        // 先初始化form，这个方法在jme线程上，然后再初始化view.
        // 因UI界面的初始化在其UI线程上,一般view依赖于form。
        if (form != null) {
            form.initialize(this);
        }
        // 初始化UI界面
        if (view != null) {
            view.initialize(this);
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void update(float tpf) {
        if (form != null) {
            form.update(tpf);
        }
        if (view != null) {
            view.update(tpf);
        }
    }

    @Override
    public void cleanup() {
        if (view != null && view.isInitialized()) {
            view.cleanup();
        }
        if (form != null && form.isInitialized()) {
            form.cleanup();
        }
        initialized = false;
    }

    @Override
    public Editor getEditor() {
        return editor;
    }

    @Override
    public F getEditForm() {
        return form;
    }

    @Override
    public void setEditForm(F newForm) {
        if (form != null && form.isInitialized()) {
            form.cleanup();
        }
        form = newForm;
        if (isInitialized() && !form.isInitialized()) {
            form.initialize(this);
        }
    }
    
    @Override
    public V getEditView() {
        return view;
    }
    
    @Override
    public void setEditView(V newView) {
        if (view != null && view.isInitialized()) {
            view.cleanup();
        }
        view = newView;
        if (isInitialized() && !view.isInitialized()) {
            view.initialize(this);
        }
    }
}
