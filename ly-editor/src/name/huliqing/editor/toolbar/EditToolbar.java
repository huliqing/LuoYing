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
 * @param <E>
 */
public abstract class EditToolbar<E extends SimpleJmeEdit> extends AbstractToolbar<E> implements EventListener {
//    private static final Logger LOG = Logger.getLogger(EditToolbar.class.getName());
    protected final Map<Tool, ToggleMappingEvent> toggleMapping = new HashMap<Tool, ToggleMappingEvent>();
    
    public EditToolbar(E edit) {
        super(edit);
    }
    
    protected void addToggleMapping(ToggleMappingEvent tme) {
        if (tme.toggleKey >= 0) {
            tme.bindKey(tme.toggleKey, true);
        }
        tme.addListener(this);
        tme.initialize();
        toggleMapping.put(tme.tool, tme);
    }
    
    protected boolean removeToggleMapping(ToggleMappingEvent tme) {
        ToggleMappingEvent tmeRemoved = toggleMapping.remove(tme.tool);
        if (tmeRemoved != null) {
            tmeRemoved.removeListener(this);
            if (tmeRemoved.isInitialized()) {
                tmeRemoved.cleanup();
            }
            return true;
        }
        return false;
    }
    
    protected void clearToggleMappings() {
        toggleMapping.values().forEach(t -> {
            t.removeListener(this);
            if (t.isInitialized()) {
                t.cleanup();
            }
        });
        toggleMapping.clear();
    }
    
    @Override
    public void onEvent(Event e) {
        // 当工具栏没有激活时则不应该响应任何事件，这很重要，因为工具栏可能很多，并且快捷快重覆或重叠的情况可能存在，
        // 所以这里应该尽量避免不同的工具栏之间的快捷快产生冲突。
        if (!isEnabled())
            return;
        
        // 快捷键不匹配的时候也不应该执行
        if (!e.isMatch()) 
            return;
        
        ToggleMappingEvent te = (ToggleMappingEvent) e;
        setEnabled(te.tool, true);
    }
    
    @Override
    public <T extends Toolbar> T setEnabled(Tool tool, boolean enabled) {
        ToggleMappingEvent tme = toggleMapping.get(tool);
        // 关闭的时候相关联的要一起关闭，但不去关闭冲突的
        if (!enabled) {
            // 会让工具位置变来变去，不友好
//            if (tme != null && tme.relations != null) {
//                for (Tool t : tme.relations) {
//                    super.setEnabled(t, false);
//                }
//            }
            super.setEnabled(tool, false);
            return (T) this;
        }
        
        if (enabled) {
            if (tme != null) {
                if (tme.conflicts != null) {
                    for (Tool dt : tme.conflicts) {
                        if (dt == tool) {
                            continue; // 不能关闭自身, 很重要: 兼容JFX界面的时候避免递归错误
                        }
                        super.setEnabled(dt, false);
                    }                
                }
                if (tme.relations != null) {
                    for (Tool et : tme.relations) {
                        if (et == tool) {
                            continue;
                        }
                        super.setEnabled(et, true);
                    }
                }
            }
        }
        super.setEnabled(tool, true);
        return (T) this;
    }
    
    public class ToggleMappingEvent extends JmeEvent {
        // 激活工具的快捷键
        public int toggleKey;
        // 绑定的工具
        public final Tool tool;
        // 冲突工具列表，当工具激活时，这些工具要关闭
        public Tool[] conflicts;
        // 相关联的工具列表，当工具激活时，这些工具要一起激活,或关闭
        public Tool[] relations;
        
        public ToggleMappingEvent(int toggleKey, Tool tool) {
            super("toggleEvent" + tool.getName());
            this.toggleKey = toggleKey;
            this.tool = tool;
        }
        
        public ToggleMappingEvent setConflicts(Tool... conflicts) {
            this.conflicts = conflicts;
            return this;
        }
        
        public ToggleMappingEvent setRelations(Tool... relations) {
            this.relations = relations;
            return this;
        }
    }
    
    
    
}
