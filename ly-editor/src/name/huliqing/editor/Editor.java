/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import name.huliqing.editor.manager.Manager;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.formview.FormView;
import name.huliqing.editor.formview.SimpleFormView;

/**
 *
 * @author huliqing
 */
public class Editor extends SimpleApplication{
    
    private List<EditorListener> listeners;
    private FormView formView;
    
    @Override
    public void simpleInitApp() {
        StatsAppState stats = this.stateManager.getState(StatsAppState.class);
        if (stats != null) {
            stateManager.detach(stats);
        }
        stateManager.attach(new EditorStatsAppState());
        flyCam.setEnabled(false);
        inputManager.setCursorVisible(true);
        viewPort.setBackgroundColor(ColorRGBA.DarkGray);
        
        // 初始化
        Manager.initialize(this);
        // 注册InputManager
        JmeEvent.registerInputManager(inputManager);
        
        setFormView(new SimpleFormView());
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        formView.update(tpf);
    }
    
    @Override
    public void reshape(int w, int h){
        super.reshape(w, h);
        if (listeners != null) {
            listeners.forEach(t -> {t.onReshape(w, h);});
        }
    }
    
    public FormView getFormView() {
        return formView;
    }

    public void setFormView(FormView newFormView) {
        if (formView != null && formView.isInitialized()) {
            formView.cleanup();
        }
        formView = newFormView;
        if (!formView.isInitialized()) {
            formView.initialize(this);
        }
        if (listeners != null) {
            listeners.forEach(t -> {t.onFormChanged(this, formView);});
        }
    }
    
    public void addListener(EditorListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<EditorListener>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public boolean removeListener(EditorListener listener) {
        return listeners != null && listeners.remove(listener);
    }

    @Override
    public void stop() {
        Manager.saveOnQuick();
        super.stop(); 
    }
    
    
}
