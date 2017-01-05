/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.editviews;

import name.huliqing.editor.editforms.EditForm;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class SimpleEditView<T extends EditForm> implements EditView<T>  {
    protected T form;
    protected boolean initialized;

    @Override
    public void initialize(T form) {
        if (initialized) {
            throw new IllegalArgumentException();
        }
        initialized = true; 
        this.form = form;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void cleanup() {
        initialized = false;
    }

    public void onDragStarted() {
        // 由子类实现
    }
    
    public void onDragEnded() {
        // 由子类实现
    }
}
