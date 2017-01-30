/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.toolbar;

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import java.awt.MouseInfo;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.tools.NumberValueTool;
import name.huliqing.editor.tools.Tool;
import name.huliqing.editor.tools.terrain.LowerTool;
import name.huliqing.editor.tools.terrain.RaiseTool;

/**
 * 地形编辑工具栏
 * @author huliqing
 */
public class TerrainToolbar extends EditToolbar<SimpleJmeEdit> {

//    private static final Logger LOG = Logger.getLogger(TerrainToolbar.class.getName());
    
    private RaiseTool raiseTool;
    private LowerTool lowerTool;
    private NumberValueTool radiusTool;
    private NumberValueTool weightTool;
    
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
        raiseTool = new RaiseTool(Manager.getRes(ResConstants.TOOL_TERRAIN_RAISE), Manager.getRes(ResConstants.TOOL_TERRAIN_RAISE_TIP)
                , AssetConstants.INTERFACE_TOOL_TERRAIN_RAISE);
        lowerTool = new LowerTool(Manager.getRes(ResConstants.TOOL_TERRAIN_LOWER), Manager.getRes(ResConstants.TOOL_TERRAIN_LOWER_TIP)
                , AssetConstants.INTERFACE_TOOL_TERRAIN_LOWER);
        radiusTool = new NumberValueTool(Manager.getRes(ResConstants.TOOL_TERRAIN_RADIUS), Manager.getRes(ResConstants.TOOL_TERRAIN_RADIUS_TIP), null);
        weightTool = new NumberValueTool(Manager.getRes(ResConstants.TOOL_TERRAIN_WEIGHT), Manager.getRes(ResConstants.TOOL_TERRAIN_WEIGHT_TIP), null);
        
        raiseTool.bindRaiseEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        
        radiusTool.setValue(10);
        radiusTool.bindDecreaseEvent().bindKey(KeyInput.KEY_LBRACKET, true);
        radiusTool.bindIncreaseEvent().bindKey(KeyInput.KEY_RBRACKET, true);
        
        weightTool.setValue(10);
        
        addToggleMapping(new ToggleMappingEvent(-1, raiseTool, new Tool[]{lowerTool}));
        addToggleMapping(new ToggleMappingEvent(-1, lowerTool, new Tool[]{raiseTool}));
        
        add(raiseTool);
        add(lowerTool);
        add(radiusTool);
        add(weightTool);
        
        setEnabled(raiseTool, true);
        setEnabled(lowerTool, true);
        setEnabled(radiusTool, true);
        setEnabled(weightTool, true);
        
        setActivated(radiusTool, true);
    }

    @Override
    public void cleanup() {
        removeAll();
        clearToggleMappings();
        super.cleanup(); 
    }

    public RaiseTool getRaiseTool() {
        return raiseTool;
    }

    public LowerTool getLowerTool() {
        return lowerTool;
    }

    public NumberValueTool getRadiusTool() {
        return radiusTool;
    }

    public NumberValueTool getWeightTool() {
        return weightTool;
    }

    public void setWeightTool(NumberValueTool weightTool) {
        this.weightTool = weightTool;
    }
    
}
