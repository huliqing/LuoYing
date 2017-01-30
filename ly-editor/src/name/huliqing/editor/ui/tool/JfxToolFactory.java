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
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.tools.NumberValueTool;
import name.huliqing.editor.tools.base.CameraTool;
import name.huliqing.editor.tools.base.GridTool;
import name.huliqing.editor.tools.base.ModeTool;
import name.huliqing.editor.tools.base.MoveTool;
import name.huliqing.editor.tools.base.RotationTool;
import name.huliqing.editor.tools.base.ScaleTool;
import name.huliqing.editor.tools.Tool;
import name.huliqing.editor.tools.terrain.LowerTool;
import name.huliqing.editor.tools.terrain.RaiseTool;

/**
 *
 * @author huliqing
 */
public class JfxToolFactory {

    private static final Logger LOG = Logger.getLogger(JfxToolFactory.class.getName());
    
    private final static Map<Class<? extends Tool>, Class<? extends JfxTool>> TOOL_MAPPING = new HashMap();
    
    static {
        // 通用形
        TOOL_MAPPING.put(NumberValueTool.class, JfxNumberValueTool.class);
        
        // ---- 基本工具
        // 模式切换工具
        TOOL_MAPPING.put(ModeTool.class, JfxModeTool.class);
        // 相机工具
        TOOL_MAPPING.put(CameraTool.class, JfxCameraTool.class);
        // 网格显示工具
        TOOL_MAPPING.put(GridTool.class, JfxToggleTool.class);
        // 移动工具
        TOOL_MAPPING.put(MoveTool.class, JfxToggleTool.class);
        // 旋转工具
        TOOL_MAPPING.put(RotationTool.class, JfxToggleTool.class);
        // 缩放放工具
        TOOL_MAPPING.put(ScaleTool.class, JfxToggleTool.class);
        
        // ----地形工具
        
        // 地形上升工具
        TOOL_MAPPING.put(RaiseTool.class, JfxToggleTool.class);
        
        // 地形降低工具
        TOOL_MAPPING.put(LowerTool.class, JfxToggleTool.class);
    }
    
    public final static JfxTool createJfxTool(Tool tool, Toolbar toolbar) {
        try {
            // 优先通过完全匹配来查找渲染类
            Class<? extends JfxTool> jtCls = TOOL_MAPPING.get(tool.getClass());
            if (jtCls == null) {
                // 通过通用类型匹配来查找渲染类
                for (Class c : TOOL_MAPPING.values()) {
                    if (c.isAssignableFrom(tool.getClass())) {
                        jtCls = c;
                        break;
                    }
                }
            } 
            
            if (jtCls != null) {
                JfxTool tv = jtCls.newInstance();
                tv.setToolbar(toolbar);
                tv.setTool(tool);
                tv.initialize();
                return tv;
            }else {
                LOG.log(Level.WARNING, "Mapping not found for toolName={0}, toolClass={1}, skipped."
                        , new Object[] {tool.getName(), tool.getClass().getName()});
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
//    private static class ResourceMapping {
//        private Class<? extends JfxTool> clazz;
//        private final String nameKey;
//        private final String tipKey;
//        private final String icon;
//        
//        public ResourceMapping(Class<? extends JfxTool>clazz, String nameKey, String tipKey, String icon) {
//            this.clazz = clazz;
//            this.nameKey = nameKey;
//            this.tipKey = tipKey;
//            this.icon = icon;
//        }
//        
//        public String getName() {
//            return Manager.getResManager().get(nameKey);
//        }
//        public String getToolTip() {
//            return Manager.getResManager().get(tipKey);
//        }
//        public String getIcon() {
//            return icon;
//        }
//    }
    
}
