/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.base;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.edit.Mode;
import name.huliqing.editor.tools.AbstractTool;

/**
 *
 * @author huliqing
 */
public class ModeTool extends AbstractTool {
    
    public interface ModeChangedListener {
        void onModeChanged(Mode newMode);
    }
    
    private List<ModeChangedListener> modeChangeListeners;

    public ModeTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }
    
    public Mode getMode() {
        return edit.getMode();
    }
    
    public void setMode(Mode mode) {
        edit.setMode(mode);
    }
    
    /**
     * 绑定一个按键，用来切换不同的模式
     * @return 
     */
    public JmeEvent bindModeEvent() {
        return bindEvent(name + "modeEvent");
    }

    @Override
    protected void onToolEvent(Event e) {
        if (e.isMatch()) {
            Mode[] ms = Mode.values();
            int idx = edit.getMode().ordinal();
            Mode mode;
            if (idx >= ms.length - 1) {
                mode = ms[0];
            } else {
                mode = ms[++idx];
            }
            edit.setMode(mode);
            if (modeChangeListeners != null) {
                modeChangeListeners.forEach(t -> {t.onModeChanged(mode);});
            }
        }
    }
    
    public void addModeChangedListener(ModeChangedListener listener) {
        if (modeChangeListeners == null) {
            modeChangeListeners = new ArrayList<>();
        }
        if (!modeChangeListeners.contains(listener)) {
            modeChangeListeners.add(listener);
        }
    }
    public boolean removeModeChangedListener(ModeChangedListener listener) {
        return modeChangeListeners != null &&  modeChangeListeners.remove(listener);
    }
    
    
}
