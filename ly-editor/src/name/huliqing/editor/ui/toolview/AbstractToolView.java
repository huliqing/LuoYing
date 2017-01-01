/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.toolview;

import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.tools.Tool;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public abstract class AbstractToolView implements ToolView {

    protected Toolbar toolbar;
    protected Tool tool;
    protected boolean activated;
    protected boolean enabled;
    
    public AbstractToolView() {}
    
    @Override
    public Tool getTool() {
        return tool;
    }
    
    @Override
    public void setActivated(boolean activated) {
        boolean changed = this.activated != activated;
        this.activated = activated;
        if (changed) {
            setViewActivated(activated);
            Jfx.runOnJme(() -> {
                toolbar.setActivated(tool, activated);
            });
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        boolean changed = this.enabled = enabled;
        this.enabled = enabled;
        if (changed) {
            setViewEnabled(enabled);
            Jfx.runOnJme(() -> {toolbar.setEnabled(tool, enabled);});
        }
    }

    @Override
    public void initialize(Tool tool, Toolbar toolbar, String name, String tooltip, String icon) {
        this.tool = tool;
        this.toolbar = toolbar;
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
