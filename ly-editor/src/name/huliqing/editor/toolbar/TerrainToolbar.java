/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.toolbar;

import com.jme3.input.KeyInput;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.tools.Tool;
import name.huliqing.editor.tools.terrain.LowerTool;
import name.huliqing.editor.tools.terrain.RaiseTool;

/**
 *
 * @author huliqing
 */
public class TerrainToolbar extends EditToolbar<SimpleJmeEdit> {
    
    public TerrainToolbar(SimpleJmeEdit edit) {
        super(edit);
    }

    @Override
    public String getName() {
        return "Terrain Toolbar";
    }
    
    @Override
    public void initialize() {
        super.initialize();
        RaiseTool raiseTool = new RaiseTool("Raise Terrain");
        LowerTool lowerTool = new LowerTool("Lower Terrain");
        
        add(raiseTool);
        add(lowerTool);
        
        setEnabled(raiseTool, true);
        setEnabled(lowerTool, true);
        
        addToggleMapping(new ToggleMappingEvent(KeyInput.KEY_0, raiseTool, new Tool[]{lowerTool}));
        addToggleMapping(new ToggleMappingEvent(KeyInput.KEY_0, lowerTool, new Tool[]{raiseTool}));
    }

    @Override
    public void cleanup() {
        removeAll();
        clearToggleMappings();
        super.cleanup(); 
    }
    
    
}
