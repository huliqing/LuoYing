/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.toolbar;

import com.jme3.util.SafeArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.forms.Form;
import name.huliqing.editor.tools.Tool;

/**
 *
 * @author huliqing
 * @param <F> Form类型
 */
public abstract class AbstractToolbar<F extends Form> implements Toolbar<F> {

    private static final Logger LOG = Logger.getLogger(AbstractToolbar.class.getName());

    protected final SafeArrayList<ToolbarListener> listeners = new SafeArrayList<ToolbarListener>(ToolbarListener.class);
    
    /**
     * 所有添加到工具栏中的工具
     */
    protected final SafeArrayList<Tool> tools = new SafeArrayList<Tool>(Tool.class);
    
    /**
     * 所有可用的工具,是tools的子集
     */
    protected final SafeArrayList<Tool> toolsEnabled = new SafeArrayList<Tool>(Tool.class);
    
    /**
     * 当前处于激活状态的工具栏, 是toolsValid的子集
     */
    protected final SafeArrayList<Tool> toolsActivated = new SafeArrayList<Tool>(Tool.class);
    
    protected F form;
    protected boolean initialized;

    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException();
        }
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public void update(float tpf) {
        for (Tool t : toolsActivated.getArray()) {
            t.update(tpf);
        }
    }
    
    @Override
    public void cleanup() {
        toolsActivated.forEach(t -> {t.cleanup();});
        initialized = false;
    }
    
    @Override
    public <T extends Toolbar> T add(Tool tool) {
        if (!tools.contains(tool)) {
            tools.add(tool);
            for (ToolbarListener tl : listeners.getArray()) {
                tl.onToolAdded(tool);
            }
        }
        return (T) this;
    }
    
    @Override
    public boolean remove(Tool tool) {
        boolean result = tools.remove(tool);
        toolsActivated.remove(tool);
        toolsEnabled.remove(tool);
        if (tool.isInitialized()) {
            tool.cleanup();
        }
        if (result) {
            for (ToolbarListener tl : listeners.getArray()) {
                tl.onToolRemoved(tool);
            }
        }
        return result;
    }

    @Override
    public Tool getTool(String tool) {
        for (Tool t : tools.getArray()) {
            if (tool.equals(t.getName())) {
               return t;
            }
        }
        return null;
    }
    
    @Override
    public <T extends Toolbar> T setActivated(Tool tool, boolean activated) {
        if (activated) {
            toolEnabled(tool);
            toolActivated(tool);
        } else {
            toolDeactivated(tool);
        }
        return (T) this;
    }
    
//    @Override
//    public <T extends Toolbar> T setActivated(String tool, boolean activated) {
//        for (Tool t : tools.getArray()) {
//            if (t.getName().equals(tool)) {
//                setActivated(t, activated);
//                break;
//            }
//        }
//        return (T) this;
//    }
    
    @Override
    public <T extends Toolbar> T setEnabled(Tool tool, boolean enabled) {
        if (enabled) {
            toolEnabled(tool);
        } else {
            toolDeactivated(tool);
            toolDisabled(tool);
        }
        return (T) this;
    }

//    @Override
//    public <T extends Toolbar> T setEnabled(String tool, boolean enabled) {
//        for (Tool t : tools.getArray()) {
//            if (t.getName().equals(tool)) {
//                setEnabled(t, enabled);
//                break;
//            }
//        }
//        return (T) this;
//    }
    
    private void toolActivated(Tool t) {
        if (!toolsActivated.contains(t)) {
            toolsActivated.add(t);
            if (!t.isInitialized()) {
                t.setToolbar(this);
                t.initialize();
            }
            for (ToolbarListener tl : listeners.getArray()) {
                tl.onToolActivated(t);
            }
            LOG.log(Level.INFO, "toolActivated, tool={0}", t.getName());
        }
    }
    
    private void toolDeactivated(Tool t) {
        if (toolsActivated.remove(t)) {
            if (t.isInitialized()) {
                t.cleanup();
            }
            for (ToolbarListener tl : listeners.getArray()) {
                tl.onToolDeactivated(t);
            }
            LOG.log(Level.INFO, "toolDeactivated, tool={0}", t.getName());
        }
    }
    
    private void toolEnabled(Tool t) {
        if (toolsEnabled.contains(t)) {
            return;
        }
        toolsEnabled.add(t);
        for (ToolbarListener l : listeners.getArray()) {
            l.onToolEnabled(t);
        }
        LOG.log(Level.INFO, "toolEnabled, tool={0}", t.getName());
    }
    
    private void toolDisabled(Tool t) {
        if (toolsEnabled.remove(t)) {
            for (ToolbarListener l : listeners.getArray()) {
                l.onToolDisabled(t);
            }
            LOG.log(Level.INFO, "toolDisabled, tool={0}", t.getName());
        }
    }

    @Override
    public void addListener(ToolbarListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public boolean removeListener(ToolbarListener listener) {
        return listeners.remove(listener);
    }

    @Override
    public List<Tool> getTools() {
        return tools;
    }

    /**
     * 获取所有可用的工具
     * @return 
     */
    public SafeArrayList<Tool> getToolsEnabled() {
        return toolsEnabled;
    }

    /**
     * 获取所有激活中的工具
     * @return 
     */
    public SafeArrayList<Tool> getToolsActivated() {
        return toolsActivated;
    }

    @Override
    public F getForm() {
        return form;
    }

    @Override
    public void setForm(F form) {
        this.form = form;
    }
    
}
