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
import name.huliqing.editor.edit.EditView;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.luoying.LuoYing;

/**
 *
 * @author huliqing
 */
public class Editor extends SimpleApplication{
    
    private List<EditorListener> listeners;
    private EditView editView;
    private boolean initialized;
    
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
        
        LuoYing.initialize(this);
        
        initialized = true;
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        if (editView != null) {
            editView.update(tpf);
        }
    }
    
    @Override
    public void reshape(int w, int h){
        super.reshape(w, h);
        if (listeners != null && listeners.size() > 0) {
            listeners.forEach(t -> {t.onReshape(w, h);});
        }
    }
    
    public EditView getFormView() {
        return editView;
    }

    public void setFormView(EditView newEditView) {
        if (editView != null) {
            editView.cleanup();
        }
        editView = newEditView;
        if (initialized) {
            editView.initialize(this);
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
        Manager.cleanup();
        super.stop(); 
    }
    
}
