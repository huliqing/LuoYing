/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.constants.AssetConstants;
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
public class JfxToolFactory {

    private static final Logger LOG = Logger.getLogger(JfxToolFactory.class.getName());
    
    private final static Map<Class<? extends Tool>, ResourceMapping> TOOL_MAPPING = new HashMap();
    
    static {
        // 基本工具
        TOOL_MAPPING.put(ModeTool.class, new ResourceMapping(JfxModeTool.class
                , ResConstants.TOOL_MODE, ResConstants.TOOL_MODE_TIP, null));
        
        TOOL_MAPPING.put(CameraTool.class, new ResourceMapping(JfxCameraTool.class
                , ResConstants.TOOL_CAMERA, ResConstants.TOOL_CAMERA_TIP, null));
        
        TOOL_MAPPING.put(GridTool.class, new ResourceMapping(JfxToggleTool.class
                , ResConstants.TOOL_GRID, ResConstants.TOOL_GRID_TIP, "/" + AssetConstants.INTERFACE_TOOL_GRID));
        
        TOOL_MAPPING.put(MoveTool.class, new ResourceMapping(JfxToggleTool.class
                , ResConstants.TOOL_MOVE, ResConstants.TOOL_MOVE_TIP, "/" + AssetConstants.INTERFACE_TOOL_MOVE));
        
        TOOL_MAPPING.put(RotationTool.class, new ResourceMapping(JfxToggleTool.class
                , ResConstants.TOOL_ROTATION, ResConstants.TOOL_ROTATION_TIP, "/" + AssetConstants.INTERFACE_TOOL_ROTATION));
        
        TOOL_MAPPING.put(ScaleTool.class, new ResourceMapping(JfxToggleTool.class
                , ResConstants.TOOL_SCALE, ResConstants.TOOL_SCALE_TIP, "/" + AssetConstants.INTERFACE_TOOL_SCALE));
        
    }
    
    public final static JfxTool createToolView(Tool tool, Toolbar toolbar) {
        try {
            ResourceMapping rm = TOOL_MAPPING.get(tool.getClass());
            if (rm != null) {
                JfxTool tv = rm.clazz.newInstance();
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
        private Class<? extends JfxTool> clazz;
        private final String nameKey;
        private final String tipKey;
        private final String icon;
        
        public ResourceMapping(Class<? extends JfxTool>clazz, String nameKey, String tipKey, String icon) {
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
