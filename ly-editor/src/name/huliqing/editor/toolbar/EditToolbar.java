/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.toolbar;

import java.util.HashMap;
import java.util.Map;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.EventListener;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.tools.Tool;

/**
 * @author huliqing
 */
public abstract class EditToolbar extends AbstractToolbar<SimpleJmeEdit> implements EventListener {
//    private static final Logger LOG = Logger.getLogger(EditToolbar.class.getName());
    protected final Map<Tool, ToggleMappingEvent> toggleMapping = new HashMap<Tool, ToggleMappingEvent>();
    
    @Override
    public void initialize(SimpleJmeEdit jmeEdit) {
        super.initialize(jmeEdit);
    }
    
    @Override
    public void cleanup() {
        super.cleanup(); 
    }
    
    protected void addToggleMapping(int keyInput, ToggleMappingEvent tme) {
        tme.bindKey(keyInput, true);
        tme.addListener(this);
        tme.initialize();
        toggleMapping.put(tme.tool, tme);
    }
    
    protected boolean removeToggleMapping(Tool tool) {
        ToggleMappingEvent tme = toggleMapping.remove(tool);
        if (tme != null) {
            tme.removeListener(this);
            if (tme.isInitialized()) {
                tme.cleanup();
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void onEvent(Event e) {
        if (!e.isMatch()) 
            return;
        ToggleMappingEvent te = (ToggleMappingEvent) e;
        setActivated(te.tool, true);
    }
    
    @Override
    public <T extends Toolbar> T setActivated(Tool tool, boolean activated) {
        // 取消冲突的工具
        ToggleMappingEvent tme = toggleMapping.get(tool);
        if (activated && tme != null && tme.conflicts != null) {
            for (Tool conflict : tme.conflicts) {
                if (conflict == tool) {
                    continue; // 不能关闭自身, 很重要: 兼容JFX界面的时候避免递归错误
                }
                setActivated(conflict, false);
            }
        }
        super.setActivated(tool, activated);
        return (T)this;
    }
    
    public class ToggleMappingEvent extends JmeEvent {
        public final Tool tool;
        // 冲突工具列表，当tool打开时，这些工具要关闭
        public Tool[] conflicts;
        
        public ToggleMappingEvent(Tool tool, Tool[] conflicts) {
            super("toggleEvent" + tool.getName());
            this.tool = tool;
            this.conflicts = conflicts;
        }
    }
    
    
    
}
