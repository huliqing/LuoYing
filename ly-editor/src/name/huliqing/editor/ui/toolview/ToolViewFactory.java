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
        tvMapping.put(GridTool.class, new ResourceMapping(ToggleToolView.class, "网格", "/name/huliqing/editor/ui/icon/grid.png", "打开/关闭网格"));
        tvMapping.put(MoveTool.class, new ResourceMapping(ToggleToolView.class, "移动", "/name/huliqing/editor/ui/icon/move.png", "打开移动功能(G)"));
        tvMapping.put(RotationTool.class, new ResourceMapping(ToggleToolView.class, "旋转", "/name/huliqing/editor/ui/icon/rotation.png", "打开旋转功能(R)"));
        tvMapping.put(ScaleTool.class, new ResourceMapping(ToggleToolView.class, "缩放", "/name/huliqing/editor/ui/icon/scale.png", "打开缩放功能(S)"));
        tvMapping.put(ModeTool.class, new ResourceMapping(ModeToolView.class, "模式", "", "切换模式(Tab)"));
        tvMapping.put(CameraTool.class, new ResourceMapping(CameraToolView.class, "相机视角", "", "切换相机视角"));
    }
    
    public final static ToolView createToolView(Tool tool, Toolbar toolbar) {
        try {
            ResourceMapping rm = tvMapping.get(tool.getClass());
            if (rm != null) {
                ToolView tv = rm.clazz.newInstance();
                tv.initialize(tool, toolbar, rm.displayName, rm.icon, rm.tooltip);
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
        public Class<? extends ToolView> clazz;
        public String displayName;
        public String icon;
        public String tooltip;
        
        public ResourceMapping(Class<? extends ToolView>clazz, String displayName, String icon, String tooltip) {
            this.clazz = clazz;
            this.displayName = displayName;
            this.icon = icon;
            this.tooltip = tooltip;
        }
    }
    
}
