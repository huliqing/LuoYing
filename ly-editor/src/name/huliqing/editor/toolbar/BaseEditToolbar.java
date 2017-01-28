/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.toolbar;

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.tools.CameraTool;
import name.huliqing.editor.tools.GridTool;
import name.huliqing.editor.tools.ModeTool;
import name.huliqing.editor.tools.MoveTool;
import name.huliqing.editor.tools.PickTool;
import name.huliqing.editor.tools.RotationTool;
import name.huliqing.editor.tools.ScaleTool;
import name.huliqing.editor.tools.Tool;
import name.huliqing.editor.tools.UndoRedoTool;

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
        
        undoRedoTool = new UndoRedoTool("undoRedoTool");
        modeTool = new ModeTool("modeTool");
        gridTool = new GridTool("gridTool");
        pickTool = new PickTool("pickTool");
        moveTool = new MoveTool("moveTool");
        rotationTool = new RotationTool("rotationTool");
        scaleTool = new ScaleTool("scaleTool");
        
        undoRedoTool.bindUndoRedoEvent().bindKey(KeyInput.KEY_Z, true);
        
        cameraTool = new CameraTool("CameraTool");
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
        
        add(undoRedoTool);
        add(cameraTool);
        add(modeTool);
        add(gridTool);
        add(pickTool);
        add(moveTool);
        add(rotationTool);
        add(scaleTool);
        
        Tool[] conflicts = new Tool[]{moveTool, scaleTool, rotationTool};
        addToggleMapping(new ToggleMappingEvent(KeyInput.KEY_G, moveTool, conflicts));
        addToggleMapping(new ToggleMappingEvent(KeyInput.KEY_S, scaleTool, conflicts));
        addToggleMapping(new ToggleMappingEvent(KeyInput.KEY_R, rotationTool, conflicts));
        
        setActivated(undoRedoTool, true);
        setActivated(cameraTool, true);
        setActivated(modeTool, true);
        setActivated(gridTool, true);
        setActivated(pickTool, true);
        setActivated(moveTool, true);
        
        setEnabled(rotationTool, true);
        setEnabled(scaleTool, true);
    }

    @Override
    public void cleanup() {
        clearToggleMappings();
        removeAll();
        super.cleanup(); 
    }
    
}
