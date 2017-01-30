/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.base;

import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.tiles.Grid;
import name.huliqing.editor.toolbar.EditToolbar;
import name.huliqing.editor.tools.EditTool;

/**
 * 在场景中产生一个在原点处的网格(xz平面上)
 * @author huliqing
 */
public class GridTool extends EditTool {
    
    // 网格
    private final Grid grid = new Grid();

    public GridTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    public void initialize(SimpleJmeEdit jmeEdit, EditToolbar toolbar) {
        super.initialize(jmeEdit, toolbar);
        edit.getEditRoot().getParent().attachChild(grid); // 放在editRoot的父节点，这样不会被场景选择到。
    }

    @Override
    public void cleanup() {
        grid.removeFromParent();
        super.cleanup(); 
    }

    @Override
    protected void onToolEvent(Event e) {
        // ignore
    }
 
  
}
