/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.tool;

import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.tools.Tool;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class JfxAbstractTool<T extends Tool> implements JfxTool<T> {

    protected Toolbar toolbar;
    protected T tool;
    protected boolean activated;
    protected boolean enabled;
    
    public JfxAbstractTool() {}

    @Override
    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    @Override
    public void setTool(T tool) {
        this.tool = tool;
    }
    
    @Override
    public void setActivated(boolean activated) {
        boolean changed = this.activated != activated;
        this.activated = activated;
        if (changed) {
            setViewActivated(activated);
            Jfx.runOnJme(() -> toolbar.setActivated(tool, activated));
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        boolean changed = this.enabled = enabled;
        this.enabled = enabled;
        if (changed) {
            setViewEnabled(enabled);
            Jfx.runOnJme(() -> toolbar.setEnabled(tool, enabled));
        }
    }

    /**
     * 激活/取消激活UI
     * @param activated 
     */
    protected abstract void setViewActivated(boolean activated);

    /**
     * 打开UI、关闭UI
     * @param enabled 
     */
    protected abstract void setViewEnabled(boolean enabled);
    
}
