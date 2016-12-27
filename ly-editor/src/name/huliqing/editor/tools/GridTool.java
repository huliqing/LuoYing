/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import name.huliqing.editor.events.Event;
import name.huliqing.editor.tiles.Grid;

/**
 * 在场景中产生一个在原点处的网格(xz平面上)
 * @author huliqing
 */
public class GridTool extends EditTool {
    
    // 网格
    private final Grid grid = new Grid();
  
    public GridTool(String name) {
        super(name);
    }

    @Override
    public void initialize() {
        super.initialize(); 
        toolbar.getForm().getEditRoot().attachChild(grid);
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
