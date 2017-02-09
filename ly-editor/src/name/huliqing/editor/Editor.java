/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import name.huliqing.editor.manager.Manager;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.input.KeyInput;
import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.luoying.LuoYing;
import name.huliqing.editor.edit.JfxEdit;
import name.huliqing.editor.events.JmeEventAppState;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.EventListener;

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
        cam.setFrustumFar(100000);
        
        // 初始化
        Manager.initialize(this);
        
        // 注册事件
        JmeEventAppState jeas = JmeEvent.getJmeEventAppState();
        jeas.setInputManager(inputManager);
        stateManager.attach(jeas);
        
        // 落樱初始化
        LuoYing.initialize(this);
        
        // 快捷键保存,一般优先级应该比其它操作都要高
        EventListener el = (Event e) -> {
            if (e.isMatch()) {
                save();
                e.setConsumed(true);
            }
        };
        JmeEvent save1 = new JmeEvent("save1").bindKey(KeyInput.KEY_S, true).bindKey(KeyInput.KEY_LCONTROL, true).setPrior(9);
        JmeEvent save2 = new JmeEvent("save2").bindKey(KeyInput.KEY_S, true).bindKey(KeyInput.KEY_RCONTROL, true).setPrior(9);
        JmeEvent save3 = new JmeEvent("save3").bindKey(KeyInput.KEY_S, false).bindKey(KeyInput.KEY_LCONTROL, true).setPrior(9);
        JmeEvent save4 = new JmeEvent("save4").bindKey(KeyInput.KEY_S, false).bindKey(KeyInput.KEY_RCONTROL, true).setPrior(9);
        save1.addListener(el);
        save2.addListener(el);
        // save3,save4是空事件，为了避免和ScaleTool的按键冲突。
        // ScaleTool的开关及自由缩放键默认是绑定到bindKey(KeyInput.KEY_S, false/true).
        save3.addListener(e -> {
            if (e.isMatch()) e.setConsumed(true);
        });
        save4.addListener(e -> {
            if (e.isMatch()) e.setConsumed(true);
        });
        save1.initialize();
        save2.initialize();
        save3.initialize();
        save4.initialize();
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
    
    public void save() {
        jfxEdit.save();
        jfxEdit.setModified(false);
    }
    
    public void saveAll() {
        save();
    }
    
    public boolean isModified() {
        if (jfxEdit != null) {
            return jfxEdit.isModified();
        }
        return false;
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
    
    /**
     * 将资源的绝对路径转换到当前主资源文件夹下的路径
     * @param abstractFilePath
     * @return 
     */
    public final static String toAssetFilePath(String abstractFilePath) {
        String mainAssetDir = Manager.getConfigManager().getMainAssetDir();
        String fileOnAssets  = abstractFilePath.replace(mainAssetDir, "").replace("\\", "/");
        if (fileOnAssets.startsWith("/")) {
            fileOnAssets = fileOnAssets.substring(1);
        }
        return fileOnAssets;
    }
}
