/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.toolbar;

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.terrain.Terrain;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.edit.Mode;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.controls.ControlTile;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.tools.NumberValueTool; 
import name.huliqing.editor.tools.Tool;
import name.huliqing.editor.tools.terrain.LevelTool;
import name.huliqing.editor.tools.terrain.LowerTool;
import name.huliqing.editor.tools.terrain.PaintTool;
import name.huliqing.editor.tools.terrain.RaiseTool;
import name.huliqing.editor.tools.terrain.RoughTool;
import name.huliqing.editor.tools.terrain.SlopeTool;
import name.huliqing.editor.tools.terrain.SmoothTool;
import name.huliqing.editor.tools.terrain.TexLayerTool;
import name.huliqing.editor.edit.SimpleEditListener;
import name.huliqing.editor.tools.terrain.EraseTool;
import name.huliqing.editor.tools.terrain.LevelParamsTool;
import name.huliqing.editor.tools.terrain.RoughParamsTool;
import name.huliqing.editor.tools.terrain.SlopeParamsTool;

/**
 * 地形编辑工具栏
 * @author huliqing
 */
public class TerrainToolbar extends EditToolbar<SimpleJmeEdit> implements SimpleEditListener {
//    private static final Logger LOG = Logger.getLogger(TerrainToolbar.class.getName());
    
    private NumberValueTool sizeTool;
    private NumberValueTool weightTool;
    private RaiseTool raiseTool;
    private LowerTool lowerTool;
    
    private RoughTool roughTool;
    private RoughParamsTool roughParamsTool;
    
    private SmoothTool smoothTool;
    
    private LevelTool levelTool;
    private LevelParamsTool levelParamTool;
    
    private SlopeTool slopeTool;
    private SlopeParamsTool slopeParamsTool;
    
    private PaintTool paintTool;
    private EraseTool eraseTool;
    private TexLayerTool texLayerTool;
    
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
        sizeTool = new NumberValueTool(Manager.getRes(ResConstants.TOOL_TERRAIN_SIZE), Manager.getRes(ResConstants.TOOL_TERRAIN_SIZE_TIP), null);
        weightTool = new NumberValueTool(Manager.getRes(ResConstants.TOOL_TERRAIN_WEIGHT), Manager.getRes(ResConstants.TOOL_TERRAIN_WEIGHT_TIP), null);
        raiseTool = new RaiseTool(Manager.getRes(ResConstants.TOOL_TERRAIN_RAISE), Manager.getRes(ResConstants.TOOL_TERRAIN_RAISE_TIP)
                , AssetConstants.INTERFACE_TOOL_TERRAIN_RAISE);
        lowerTool = new LowerTool(Manager.getRes(ResConstants.TOOL_TERRAIN_LOWER), Manager.getRes(ResConstants.TOOL_TERRAIN_LOWER_TIP)
                , AssetConstants.INTERFACE_TOOL_TERRAIN_LOWER);
        
        roughTool = new RoughTool(Manager.getRes(ResConstants.TOOL_TERRAIN_ROUGH), Manager.getRes(ResConstants.TOOL_TERRAIN_ROUGH_TIP)
                , AssetConstants.INTERFACE_TOOL_TERRAIN_ROUGH);
        roughParamsTool = new RoughParamsTool("", null, null);
        
        smoothTool = new SmoothTool(Manager.getRes(ResConstants.TOOL_TERRAIN_SMOOTH), Manager.getRes(ResConstants.TOOL_TERRAIN_SMOOTH_TIP)
                , AssetConstants.INTERFACE_TOOL_TERRAIN_SMOOTH);
        
        levelTool = new LevelTool(Manager.getRes(ResConstants.TOOL_TERRAIN_LEVEL), Manager.getRes(ResConstants.TOOL_TERRAIN_LEVEL_TIP)
                , AssetConstants.INTERFACE_TOOL_TERRAIN_LEVEL); 
        levelParamTool = new LevelParamsTool("", null, null);
        
        slopeTool = new SlopeTool(Manager.getRes(ResConstants.TOOL_TERRAIN_SLOPE), Manager.getRes(ResConstants.TOOL_TERRAIN_SLOPE_TIP)
                , AssetConstants.INTERFACE_TOOL_TERRAIN_SLOPE);
        slopeParamsTool = new SlopeParamsTool("", null, null);
        
        paintTool = new PaintTool(Manager.getRes(ResConstants.TOOL_TERRAIN_PAINT), Manager.getRes(ResConstants.TOOL_TERRAIN_PAINT_TIP)
                , AssetConstants.INTERFACE_TOOL_TERRAIN_PAINT); 
        eraseTool = new EraseTool(Manager.getRes(ResConstants.TOOL_TERRAIN_ERASE), Manager.getRes(ResConstants.TOOL_TERRAIN_ERASE_TIP)
                , AssetConstants.INTERFACE_TOOL_TERRAIN_ERASE);
        texLayerTool = new TexLayerTool(Manager.getRes(ResConstants.TOOL_TERRAIN_TEXLAYER), Manager.getRes(ResConstants.TOOL_TERRAIN_TEXLAYER_TIP), null); 

        sizeTool.setMinValue(0).setValue(1);
        sizeTool.setStepAmount(0.1f);
        sizeTool.bindIncreaseEvent().bindKey(KeyInput.KEY_RBRACKET, true);
        sizeTool.bindDecreaseEvent().bindKey(KeyInput.KEY_LBRACKET, true);
        weightTool.setMinValue(-10).setValue(1);
        raiseTool.bindActionEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        lowerTool.bindActionEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        roughTool.bindActionEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        smoothTool.bindActionEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        levelTool.bindActionEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        slopeTool.bindActionEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        paintTool.bindActionEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        eraseTool.bindActionEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        
        Tool[] conflicts = new Tool[]{raiseTool, lowerTool, roughTool, smoothTool, levelTool, slopeTool, paintTool, eraseTool};
        addToggleMapping(new ToggleMappingEvent(-1, raiseTool).setConflicts(conflicts));
        addToggleMapping(new ToggleMappingEvent(-1, lowerTool).setConflicts(conflicts));
        addToggleMapping(new ToggleMappingEvent(-1, roughTool).setConflicts(conflicts).setRelations(roughParamsTool));
        addToggleMapping(new ToggleMappingEvent(-1, smoothTool).setConflicts(conflicts));
        addToggleMapping(new ToggleMappingEvent(-1, levelTool).setConflicts(conflicts).setRelations(levelParamTool));
        addToggleMapping(new ToggleMappingEvent(-1, slopeTool).setConflicts(conflicts).setRelations(slopeParamsTool));
        addToggleMapping(new ToggleMappingEvent(-1, paintTool).setConflicts(conflicts));
        addToggleMapping(new ToggleMappingEvent(-1, eraseTool).setConflicts(conflicts));
        
        add(sizeTool);
        add(weightTool);
        add(raiseTool);
        add(lowerTool);
        
        add(roughTool);
        add(roughParamsTool);
        
        add(smoothTool);
        
        add(levelTool);
        add(levelParamTool);
        
        add(slopeTool);
        add(slopeParamsTool);
        
        add(paintTool);
        add(eraseTool);
        add(texLayerTool);
        
        setEnabled(sizeTool, true);
        setEnabled(weightTool, true);
        setEnabled(texLayerTool, true);
        
        setEnabled(false);
        edit.addListener(this);
    }

    @Override
    public void cleanup() {
        edit.removeListener(this);
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

    public NumberValueTool getSizeTool() {
        return sizeTool;
    }

    public NumberValueTool getWeightTool() {
        return weightTool;
    }

    public RoughTool getRoughTool() {
        return roughTool;
    }

    public RoughParamsTool getRoughParamsTool() {
        return roughParamsTool;
    }

    public SmoothTool getSmoothTool() {
        return smoothTool;
    }

    public LevelTool getLevelTool() {
        return levelTool;
    }

    public LevelParamsTool getLevelParamTool() {
        return levelParamTool;
    }

    public SlopeTool getSlopeTool() {
        return slopeTool;
    }

    public SlopeParamsTool getSlopeParamsTool() {
        return slopeParamsTool;
    }
    
    public PaintTool getPaintTool() {
        return paintTool;
    }

    public EraseTool getEraseTool() {
        return eraseTool;
    }
    
    public TexLayerTool getTexLayerTool() {
        return texLayerTool;
    }

    @Override
    public void onModeChanged(Mode mode) {
        // ignore
    }
    
    @Override
    public void onSelected(ControlTile selectObj) {
        if (selectObj instanceof EntityControlTile) {
            if (((EntityControlTile) selectObj).getTarget().getSpatial() instanceof Terrain) {
                setEnabled(true);
                return;
            }
        }
        setEnabled(false);
    }
    
}
