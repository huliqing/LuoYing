/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.forms.Mode;

/**
 *
 * @author huliqing
 */
public class ModeTool extends EditTool {
    
    public ModeTool(String name) {
        super(name);
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
            int idx = form.getMode().ordinal();
            Mode mode;
            if (idx >= ms.length - 1) {
                mode = ms[0];
            } else {
                mode = ms[++idx];
            }
            form.setMode(mode);
        }
    }
    
}
