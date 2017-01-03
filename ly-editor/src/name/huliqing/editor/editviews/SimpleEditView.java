/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.editviews;

import name.huliqing.editor.formview.FormView;

/**
 *
 * @author huliqing
 */
public class SimpleEditView implements EditView  {
    protected FormView formView;
    protected boolean initialized;

    @Override
    public void initialize(FormView formView) {
        if (initialized) {
            throw new IllegalArgumentException();
        }
        initialized = true; 
        this.formView = formView;
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

    @Override
    public FormView getFormView() {
        return formView;
    }
}
