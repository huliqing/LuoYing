/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.toolbar;

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.tools.base.CameraTool;
import name.huliqing.editor.tools.base.GridTool;
import name.huliqing.editor.tools.base.ModeTool;
import name.huliqing.editor.tools.base.MoveTool;
import name.huliqing.editor.tools.base.PickTool;
import name.huliqing.editor.tools.base.RotationTool;
import name.huliqing.editor.tools.base.ScaleTool;
import name.huliqing.editor.tools.Tool;
import name.huliqing.editor.tools.base.UndoRedoTool;

/**
 * 最基本的3D编辑工具栏
 * @author huliqing
 */
public class BaseEditToolbar extends EditToolbar<SimpleJmeEdit> {
    
    private UndoRedoTool undoRedoTool;
    private CameraTool cameraTool;
    private ModeTool modeTool;
    private GridTool gridTool;
    private PickTool pickTool;
    
    private MoveTool moveTool;
    private RotationTool rotationTool;
    private ScaleTool scaleTool;
    
    public BaseEditToolbar(SimpleJmeEdit edit) {
        super(edit);
    }

    @Override
    public String getName() {
        return "Base Edit";
    }
    
    @Override
    public void initialize() {
        super.initialize();
        
        undoRedoTool = new UndoRedoTool("undoRedoTool", Manager.getRes(ResConstants.TOOL_MODE_TIP), null);
        cameraTool = new CameraTool(Manager.getRes(ResConstants.TOOL_CAMERA), Manager.getRes(ResConstants.TOOL_CAMERA_TIP), null);
        modeTool = new ModeTool(Manager.getRes(ResConstants.TOOL_MODE), Manager.getRes(ResConstants.TOOL_MODE_TIP), null);
        gridTool = new GridTool(Manager.getRes(ResConstants.TOOL_GRID), Manager.getRes(ResConstants.TOOL_GRID_TIP), AssetConstants.INTERFACE_TOOL_GRID);
        pickTool = new PickTool("pickTool", null, null);
        moveTool = new MoveTool(Manager.getRes(ResConstants.TOOL_MOVE), Manager.getRes(ResConstants.TOOL_MOVE_TIP),  AssetConstants.INTERFACE_TOOL_MOVE);
        rotationTool = new RotationTool(Manager.getRes(ResConstants.TOOL_ROTATION), Manager.getRes(ResConstants.TOOL_ROTATION_TIP), AssetConstants.INTERFACE_TOOL_ROTATION);
        scaleTool = new ScaleTool(Manager.getRes(ResConstants.TOOL_SCALE), Manager.getRes(ResConstants.TOOL_SCALE_TIP), AssetConstants.INTERFACE_TOOL_SCALE);
        
        undoRedoTool.bindUndoRedoEvent().bindKey(KeyInput.KEY_Z, true);
        
        cameraTool.bindDragEvent().bindButton(MouseInput.BUTTON_MIDDLE, true).bindKey(KeyInput.KEY_LSHIFT, true);
        cameraTool.bindToggleRotateEvent().bindButton(MouseInput.BUTTON_MIDDLE, true);
        cameraTool.bindZoomInEvent().bindAxis(MouseInput.AXIS_WHEEL, false, false);
        cameraTool.bindZoomOutEvent().bindAxis(MouseInput.AXIS_WHEEL, true, false);
        cameraTool.bindRechaseEvent().bindKey(KeyInput.KEY_DECIMAL, true);
        cameraTool.bindResetEvent().bindKey(KeyInput.KEY_LSHIFT, true).bindKey(KeyInput.KEY_C, true);
        cameraTool.bindViewFrontEvent().bindKey(KeyInput.KEY_NUMPAD1, true);
        cameraTool.bindViewRightEvent().bindKey(KeyInput.KEY_NUMPAD3, true);
        cameraTool.bindViewTopEvent().bindKey(KeyInput.KEY_NUMPAD7, true);
        cameraTool.bindViewOrthoPerspEvent().bindKey(KeyInput.KEY_NUMPAD5, true);

        modeTool.bindModeEvent().bindKey(KeyInput.KEY_TAB, true);
        pickTool.bindPickEvent().bindButton(MouseInput.BUTTON_RIGHT, true);
        
        moveTool.bindMoveEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        moveTool.bindFreeMoveStartEvent().bindKey(KeyInput.KEY_G, false);
        moveTool.bindFreeMoveCancelEvent().setPrior(1).bindButton(MouseInput.BUTTON_RIGHT, true);
        
        scaleTool.bindScaleEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        scaleTool.bindFreeScaleStartEvent().bindKey(KeyInput.KEY_S, false);
        scaleTool.bindFreeScaleCancelEvent().setPrior(1).bindButton(MouseInput.BUTTON_RIGHT, true);
        
        rotationTool.bindRotationEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        rotationTool.bindFreeRotationStartEvent().bindKey(KeyInput.KEY_R, false);
        rotationTool.bindFreeRotationCancelEvent().setPrior(1).bindButton(MouseInput.BUTTON_RIGHT, true); 
        
        Tool[] conflicts = new Tool[]{moveTool, scaleTool, rotationTool};
        addToggleMapping(new ToggleMappingEvent(KeyInput.KEY_G, moveTool, conflicts));
        addToggleMapping(new ToggleMappingEvent(KeyInput.KEY_S, scaleTool, conflicts));
        addToggleMapping(new ToggleMappingEvent(KeyInput.KEY_R, rotationTool, conflicts));
        
        add(undoRedoTool);
        add(cameraTool);
        add(modeTool);
        add(gridTool);
        add(pickTool);
        add(moveTool);
        add(rotationTool);
        add(scaleTool);
        
        setEnabled(undoRedoTool, true);
        setEnabled(cameraTool, true);
        setEnabled(modeTool, true);
        setEnabled(gridTool, true);
        setEnabled(pickTool, true);
        setEnabled(moveTool, true);
        setEnabled(rotationTool, true);
        setEnabled(scaleTool, true);
        
        setActivated(undoRedoTool, true);
        setActivated(cameraTool, true);
        setActivated(modeTool, true);
        setActivated(gridTool, true);
        setActivated(pickTool, true);
        setActivated(moveTool, true);
    }

    @Override
    public void cleanup() {
        clearToggleMappings();
        removeAll();
        super.cleanup(); 
    }
    
}
