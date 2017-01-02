/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.toolview;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.tools.CameraTool;
import name.huliqing.editor.tools.GridTool;
import name.huliqing.editor.tools.ModeTool;
import name.huliqing.editor.tools.MoveTool;
import name.huliqing.editor.tools.RotationTool;
import name.huliqing.editor.tools.ScaleTool;
import name.huliqing.editor.tools.Tool;

/**
 *
 * @author huliqing
 */
public class ToolViewFactory {

    private static final Logger LOG = Logger.getLogger(ToolViewFactory.class.getName());
    
    private final static Map<Class<? extends Tool>, ResourceMapping> tvMapping // ToolViewMapping 
            = new HashMap<>();
    
    static {
        tvMapping.put(ModeTool.class, new ResourceMapping(ModeToolView.class
                , ResConstants.TOOL_MODE, ResConstants.TOOL_MODE_TIP, null));
        
        tvMapping.put(CameraTool.class, new ResourceMapping(CameraToolView.class
                , ResConstants.TOOL_CAMERA, ResConstants.TOOL_CAMERA_TIP, null));
        
        tvMapping.put(GridTool.class, new ResourceMapping(ToggleToolView.class
                , ResConstants.TOOL_GRID, ResConstants.TOOL_GRID_TIP, "/name/huliqing/editor/ui/icon/grid.png"));
        
        tvMapping.put(MoveTool.class, new ResourceMapping(ToggleToolView.class
                , ResConstants.TOOL_MOVE, ResConstants.TOOL_MOVE_TIP, "/name/huliqing/editor/ui/icon/move.png"));
        
        tvMapping.put(RotationTool.class, new ResourceMapping(ToggleToolView.class
                , ResConstants.TOOL_ROTATION, ResConstants.TOOL_ROTATION_TIP, "/name/huliqing/editor/ui/icon/rotation.png"));
        
        tvMapping.put(ScaleTool.class, new ResourceMapping(ToggleToolView.class
                , ResConstants.TOOL_SCALE, ResConstants.TOOL_SCALE_TIP, "/name/huliqing/editor/ui/icon/scale.png"));
        
    }
    
    public final static ToolView createToolView(Tool tool, Toolbar toolbar) {
        try {
            ResourceMapping rm = tvMapping.get(tool.getClass());
            if (rm != null) {
                ToolView tv = rm.clazz.newInstance();
                tv.initialize(tool, toolbar, rm.getName(), rm.getToolTip(), rm.getIcon());
                return tv;
            } else {
                LOG.log(Level.WARNING, "Mapping not found for toolName={0}, toolClass={1}, skipped."
                        , new Object[] {tool.getName(), tool.getClass().getName()});
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static class ResourceMapping {
        private Class<? extends ToolView> clazz;
        private String nameKey;
        private String tipKey;
        private String icon;
        
        public ResourceMapping(Class<? extends ToolView>clazz, String nameKey, String tipKey, String icon) {
            this.clazz = clazz;
            this.nameKey = nameKey;
            this.tipKey = tipKey;
            this.icon = icon;
        }
        
        public String getName() {
            return Manager.getResManager().get(nameKey);
        }
        public String getToolTip() {
            return Manager.getResManager().get(tipKey);
        }
        public String getIcon() {
            return icon;
        }
    }
    
}
