/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.toolbar;

import com.jme3.input.MouseInput;
import com.jme3.terrain.Terrain;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.edit.Mode;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.SimpleJmeEditListener;
import name.huliqing.editor.edit.controls.ControlTile;
import name.huliqing.editor.edit.select.EntitySelectObj;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.tools.NumberValueTool;
import name.huliqing.editor.tools.Tool;
import name.huliqing.editor.tools.terrain.LevelTool;
import name.huliqing.editor.tools.terrain.LowerTool;
import name.huliqing.editor.tools.terrain.RaiseTool;
import name.huliqing.editor.tools.terrain.RoughTool;
import name.huliqing.editor.tools.terrain.SmoothTool;

/**
 * 地形编辑工具栏
 * @author huliqing
 */
public class TerrainToolbar extends EditToolbar<SimpleJmeEdit> implements SimpleJmeEditListener {

//    private static final Logger LOG = Logger.getLogger(TerrainToolbar.class.getName());
    
    private NumberValueTool radiusTool;
    private NumberValueTool weightTool;
    private RaiseTool raiseTool;
    private LowerTool lowerTool;
    private RoughTool roughTool;
    private SmoothTool smoothTool;
    private LevelTool levelTool;
    
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
        radiusTool = new NumberValueTool(Manager.getRes(ResConstants.TOOL_TERRAIN_RADIUS), Manager.getRes(ResConstants.TOOL_TERRAIN_RADIUS_TIP), null);
        weightTool = new NumberValueTool(Manager.getRes(ResConstants.TOOL_TERRAIN_WEIGHT), Manager.getRes(ResConstants.TOOL_TERRAIN_WEIGHT_TIP), null);
        raiseTool = new RaiseTool(Manager.getRes(ResConstants.TOOL_TERRAIN_RAISE), Manager.getRes(ResConstants.TOOL_TERRAIN_RAISE_TIP)
                , AssetConstants.INTERFACE_TOOL_TERRAIN_RAISE);
        lowerTool = new LowerTool(Manager.getRes(ResConstants.TOOL_TERRAIN_LOWER), Manager.getRes(ResConstants.TOOL_TERRAIN_LOWER_TIP)
                , AssetConstants.INTERFACE_TOOL_TERRAIN_LOWER);
        roughTool = new RoughTool(Manager.getRes(ResConstants.TOOL_TERRAIN_ROUGH), Manager.getRes(ResConstants.TOOL_TERRAIN_ROUGH_TIP)
                , AssetConstants.INTERFACE_TOOL_TERRAIN_ROUGH);
        smoothTool = new SmoothTool(Manager.getRes(ResConstants.TOOL_TERRAIN_SMOOTH), Manager.getRes(ResConstants.TOOL_TERRAIN_SMOOTH_TIP)
                , AssetConstants.INTERFACE_TOOL_TERRAIN_SMOOTH);
        levelTool = new LevelTool(Manager.getRes(ResConstants.TOOL_TERRAIN_LEVEL), Manager.getRes(ResConstants.TOOL_TERRAIN_LEVEL_TIP)
                , AssetConstants.INTERFACE_TOOL_TERRAIN_LEVEL); 


        radiusTool.setMinValue(-10).setValue(1);
        weightTool.setMinValue(-10).setValue(1);
        raiseTool.bindRaiseEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        lowerTool.bindRaiseEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        roughTool.bindRoughEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        smoothTool.bindSmoothEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        levelTool.bindLevelEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        
        Tool[] conflicts = new Tool[]{raiseTool, lowerTool, roughTool, smoothTool, levelTool};
        addToggleMapping(new ToggleMappingEvent(-1, raiseTool, conflicts));
        addToggleMapping(new ToggleMappingEvent(-1, lowerTool, conflicts));
        addToggleMapping(new ToggleMappingEvent(-1, roughTool, conflicts));
        addToggleMapping(new ToggleMappingEvent(-1, smoothTool, conflicts));
        addToggleMapping(new ToggleMappingEvent(-1, levelTool, conflicts));
        
        add(radiusTool);
        add(weightTool);
        add(raiseTool);
        add(lowerTool);
        add(roughTool);
        add(smoothTool);
        add(levelTool);
        
        setEnabled(radiusTool, true);
        setEnabled(weightTool, true);
        setEnabled(raiseTool, true);
        setEnabled(lowerTool, true);
        setEnabled(roughTool, true);
        setEnabled(smoothTool, true);
        setEnabled(levelTool, true);
        
        setActivated(radiusTool, true);
        setActivated(weightTool, true);
        
        setEnabled(false);
        edit.addSimpleEditListener(this);
    }

    @Override
    public void cleanup() {
        edit.removeEditFormListener(this);
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

    public RoughTool getRoughTool() {
        return roughTool;
    }

    public void setRoughTool(RoughTool roughTool) {
        this.roughTool = roughTool;
    }

    public SmoothTool getSmoothTool() {
        return smoothTool;
    }

    public void setSmoothTool(SmoothTool smoothTool) {
        this.smoothTool = smoothTool;
    }

    public LevelTool getLevelTool() {
        return levelTool;
    }

    public void setLevelTool(LevelTool levelTool) {
        this.levelTool = levelTool;
    }

    @Override
    public void onModeChanged(Mode mode) {
        // ignore
    }
    
    @Override
    public void onSelect(ControlTile selectObj) {
        if (selectObj instanceof EntitySelectObj) {
            if (((EntitySelectObj) selectObj).getTarget().getSpatial() instanceof Terrain) {
                setEnabled(true);
                return;
            }
        }
        setEnabled(false);
    }
    
}
