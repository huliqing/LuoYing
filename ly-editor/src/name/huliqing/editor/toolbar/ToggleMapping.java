/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.toolbar;

import name.huliqing.editor.tools.Tool;

/**
 * 当打开一些工具时，要关闭另一些工具
 * @author huliqing
 */
public class ToggleMapping {
    
    private final Tool tool;
    // 冲突工具列表，当tool打开时，这些工具要关闭
    private Tool[] conflicts;

    public ToggleMapping(Tool tool) {
        this.tool = tool;
    }
    
    public Tool getTool() {
        return tool;
    }

    public Tool[] getConflicts() {
        return conflicts;
    }
    
    public ToggleMapping setConflicts(Tool... conflicts) {
        this.conflicts = conflicts;
        return this;
    }
}
