/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.forms.Form;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.forms.SimpleEditForm;

/**
 *
 * @author huliqing
 */
public class Editor extends SimpleApplication{
    
    private List<EditorListener> listeners;
    private Form form;
    
    @Override
    public void simpleInitApp() {
        StatsAppState stats = this.stateManager.getState(StatsAppState.class);
        if (stats != null) {
            stateManager.detach(stats);
        }
        stateManager.attach(new EditorStatsAppState());
        
        // 初始化
        Manager.initialize(this);
        // 注册InputManager
        JmeEvent.registerInputManager(inputManager);
        
        setForm(new SimpleEditForm());
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        form.update(tpf);
    }
    
    @Override
    public void reshape(int w, int h){
        super.reshape(w, h);
        if (listeners != null) {
            listeners.forEach(t -> {t.onReshape(w, h);});
        }
    }
    
    public Form getForm() {
        return form;
    }

    public void setForm(Form newForm) {
        if (form != null) {
            form.cleanup();
        }
        form = newForm;
        if (!form.isInitialized()) {
            form.initialize(this);
        }
        if (listeners != null) {
            listeners.forEach(t -> {t.onFormChanged(this, newForm);});
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
