/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.editforms;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.formview.FormView;
import name.huliqing.editor.toolbar.Toolbar;

/**
 *
 * @author huliqing
 */
public abstract class AbstractForm implements EditForm {

    protected boolean initialized;
    protected FormView formView;    
    protected Toolbar toolbar;
    protected List<EditFormListener> listeners;

    @Override
    public void initialize(FormView formView) {
        if (initialized) {
            throw new IllegalStateException();
        }
        initialized = true; 
        this.formView = formView;
        if (toolbar != null && !toolbar.isInitialized()) {
            toolbar.initialize(formView);
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
    public FormView getFormView() {
        return formView;
    }

    @Override
    public void setToolbar(Toolbar newToolbar) {
        if (toolbar != null && toolbar.isInitialized()) {
            toolbar.cleanup();
        }
        toolbar = newToolbar;
        if (isInitialized() && !toolbar.isInitialized()) {
            toolbar.initialize(formView);
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
    public void addListener(EditFormListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<EditFormListener>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public boolean removeListener(EditFormListener listener) {
        return listeners != null && listeners.remove(listener);
    }

    
}
