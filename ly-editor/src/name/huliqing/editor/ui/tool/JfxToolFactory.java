/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.editor.ui.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.tools.BooleanValueTool;
import name.huliqing.editor.tools.ButtonTool;
import name.huliqing.editor.tools.EntityValueTool;
import name.huliqing.editor.tools.NumberValueTool;
import name.huliqing.editor.tools.ParamsTool;
import name.huliqing.editor.tools.StringValueTool;
import name.huliqing.editor.tools.ToggleTool;
import name.huliqing.editor.tools.base.GridTool;
import name.huliqing.editor.tools.base.ModeTool;
import name.huliqing.editor.tools.base.MoveTool;
import name.huliqing.editor.tools.base.RotationTool;
import name.huliqing.editor.tools.base.ScaleTool;
import name.huliqing.editor.tools.Tool;
import name.huliqing.editor.tools.Vector2fValueTool;
import name.huliqing.editor.tools.Vector3fValueTool;
import name.huliqing.editor.tools.base.CameraTool;
import name.huliqing.editor.tools.base.CameraViewTool;
import name.huliqing.editor.tools.entity.InstancedTool;
import name.huliqing.editor.tools.entity.SourceTool;
import name.huliqing.editor.tools.terrain.TexLayerTool;

/**
 *
 * @author huliqing
 */
public class JfxToolFactory {

    private static final Logger LOG = Logger.getLogger(JfxToolFactory.class.getName());
    
    private final static Map<Class<?>, Class<? extends JfxTool>> TOOL_MAPPING = new HashMap();
    
    static {
        // ---- 通用的编辑工具与JFX渲染工具配置
        TOOL_MAPPING.put(ParamsTool.class, JfxParamsTool.class);
        // 通用设置，如无特别指定，则所有ToggleTool类型的工具都渲染为JfxToggleTool
        TOOL_MAPPING.put(ToggleTool.class, JfxToggleTool.class);
        // 通用设置，如无特别指定，则所有ButtonTool类型的工具都渲染为JfxButtonTool
        TOOL_MAPPING.put(ButtonTool.class, JfxButtonTool.class);
        // 通用设置，如无特别指定，则所有NumberValueTool类型的工具都渲染为JfxNumberValueTool
        TOOL_MAPPING.put(NumberValueTool.class, JfxNumberValueTool.class);
        TOOL_MAPPING.put(StringValueTool.class, JfxStringValueTool.class);
        TOOL_MAPPING.put(BooleanValueTool.class, JfxBooleanValueTool.class);
        TOOL_MAPPING.put(Vector2fValueTool.class, JfxVector2fValueTool.class);
        TOOL_MAPPING.put(Vector3fValueTool.class, JfxVector3fValueTool.class);
        TOOL_MAPPING.put(EntityValueTool.class, JfxEntityValueTool.class);
        
        // ---- 基本工具
        // 模式切换工具
        TOOL_MAPPING.put(ModeTool.class, JfxModeTool.class);
        // 相机视角切换工具
        TOOL_MAPPING.put(CameraViewTool.class, JfxCameraViewTool.class);
        // 相机工具
        TOOL_MAPPING.put(CameraTool.class, JfxToggleTool.class);
        // 网格显示工具
        TOOL_MAPPING.put(GridTool.class, JfxToggleTool.class);
        // 移动工具
        TOOL_MAPPING.put(MoveTool.class, JfxToggleTool.class);
        // 旋转工具
        TOOL_MAPPING.put(RotationTool.class, JfxToggleTool.class);
        // 缩放放工具
        TOOL_MAPPING.put(ScaleTool.class, JfxToggleTool.class);
        
        // ----地形工具
        
        // 地形贴图图层工具
        TOOL_MAPPING.put(TexLayerTool.class, JfxTerrainTexLayerTool.class);
        
        // ---- 实体刷工具
        // 实体刷的实体源列表工具
        TOOL_MAPPING.put(SourceTool.class, JfxSourceTool.class);
        // 实体刷的选择Instanced实例的工具
        TOOL_MAPPING.put(InstancedTool.class, JfxInstancedTool.class);
        
//        // ---- Batch工具栏
//        TOOL_MAPPING.put(BatchSourceTool.class, JfxBatchSourceTool.class);
//        TOOL_MAPPING.put(BatchTargetTool.class, JfxBatchTargetTool.class);
        
    }
    
    public final static JfxTool createJfxTool(Tool tool, Toolbar toolbar) {
        try {
            // 优先通过完全匹配来查找渲染类
            Class<? extends JfxTool> jtCls = TOOL_MAPPING.get(tool.getClass());
            if (jtCls == null) {
                // 通过通用类型匹配来查找渲染类
                for (Entry<Class<?>, Class<? extends JfxTool>> et : TOOL_MAPPING.entrySet()) {
                    if (et.getKey().isAssignableFrom(tool.getClass())) {
                        jtCls = et.getValue();
                        break;
                    }
                }
            } 
            
            if (jtCls != null) {
                JfxTool tv = jtCls.newInstance();
                tv.setToolbar(toolbar);
                tv.setTool(tool);
                return tv;
            }else {
                LOG.log(Level.INFO, "Mapping not found for toolName={0}, toolClass={1}, skipped."
                        , new Object[] {tool.getName(), tool.getClass().getName()});
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
