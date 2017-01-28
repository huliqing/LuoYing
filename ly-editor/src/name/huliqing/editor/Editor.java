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
import name.huliqing.luoying.LuoYing;
import name.huliqing.editor.edit.JfxEdit;
import name.huliqing.editor.events.JmeEventAppState;
import name.huliqing.editor.edit.UndoRedo;

/**
 *
 * @author huliqing
 */
public class Editor extends SimpleApplication{
    
    private List<EditorListener> listeners;
    private JfxEdit jfxEdit;
    private boolean initialized;
    
    @Override
    public void simpleInitApp() {
        StatsAppState stats = this.stateManager.getState(StatsAppState.class);
        if (stats != null) {
            stateManager.detach(stats);
        }
        stateManager.attach(new EditorStatsAppState());
        inputManager.setCursorVisible(true);
        viewPort.setBackgroundColor(ColorRGBA.DarkGray);
        flyCam.setEnabled(false);
        cam.setFrustumFar(10000);
        
        // 初始化
        Manager.initialize(this);
        
        // 注册事件
        JmeEventAppState jeas = JmeEvent.getJmeEventAppState();
        jeas.setInputManager(inputManager);
        stateManager.attach(jeas);
        
        LuoYing.initialize(this);
        
        initialized = true;
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        if (jfxEdit != null) {
            jfxEdit.update(tpf);
        }
    }
    
    @Override
    public void reshape(int w, int h){
        super.reshape(w, h);
        if (listeners != null && listeners.size() > 0) {
            listeners.forEach(t -> {t.onReshape(w, h);});
        }
    }
    
    public JfxEdit getJfxEdit() {
        return jfxEdit;
    }

    public void setJfxEdit(JfxEdit newJfxEdit) {
        if (jfxEdit != null) {
            jfxEdit.cleanup();
        }
        jfxEdit = newJfxEdit;
        if (initialized) {
            jfxEdit.initialize(this);
        }
    }
    
    public void undo() {
        if (jfxEdit != null) {
            jfxEdit.undo();
        }
    }
    
    public void redo() {
        if (jfxEdit != null) {
            jfxEdit.redo();
        }
    }
    
    public void addUndoRedo(UndoRedo ur) {
        if (jfxEdit != null) {
            jfxEdit.addUndoRedo(ur);
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
