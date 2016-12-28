/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.toolbar;

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.EventListener;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.forms.EditForm;
import name.huliqing.editor.tools.CameraTool;
import name.huliqing.editor.tools.GridTool;
import name.huliqing.editor.tools.ModeTool;
import name.huliqing.editor.tools.MoveTool;
import name.huliqing.editor.tools.PickTool;
import name.huliqing.editor.tools.RotationTool;
import name.huliqing.editor.tools.ScaleTool;
import name.huliqing.editor.tools.Tool;

/**
 * @author huliqing
 */
public class EditToolbar extends AbstractToolbar<EditForm> implements EventListener {

    private static final Logger LOG = Logger.getLogger(EditToolbar.class.getName());
    private final List<ToggleEvent> toolToggleMapping = new ArrayList<ToggleEvent>();
    
    private GridTool gridTool;
    private CameraTool cameraTool;
    private PickTool pickTool;
    private ModeTool modeTool;
    private MoveTool moveTool;
    private ScaleTool scaleTool;
    private RotationTool rotationTool;
    
    @Override
    public void initialize() {
        super.initialize();
        
        gridTool = new GridTool("gridTool");
        cameraTool = new CameraTool("CameraTool");
        pickTool = new PickTool("pickTool");
        modeTool = new ModeTool("modeTool");
        moveTool = new MoveTool("moveTool");
        scaleTool = new ScaleTool("scaleTool");
        rotationTool = new RotationTool("rotationTool");
        
        cameraTool.bindDragEvent().bindButton(MouseInput.BUTTON_MIDDLE, true).bindKey(KeyInput.KEY_LSHIFT, true);
        cameraTool.bindToggleRotateEvent().bindButton(MouseInput.BUTTON_MIDDLE, true);
        cameraTool.bindZoomInEvent().bindAxis(MouseInput.AXIS_WHEEL, false, false);
        cameraTool.bindZoomOutEvent().bindAxis(MouseInput.AXIS_WHEEL, true, false);
        cameraTool.bindRechaseEvent().bindKey(KeyInput.KEY_DECIMAL, false);
        cameraTool.bindResetEvent().bindKey(KeyInput.KEY_C, false).bindKey(KeyInput.KEY_LSHIFT, true);
        modeTool.bindModeEvent().bindKey(KeyInput.KEY_TAB, false);
        pickTool.bindPickEvent().bindButton(MouseInput.BUTTON_RIGHT, true);
        moveTool.bindMoveEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        scaleTool.bindScaleEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        rotationTool.bindRotationEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        
        add(gridTool);
        add(cameraTool);
        add(pickTool);
        add(modeTool);
        add(moveTool);
        add(scaleTool);
        add(rotationTool);
        
        Tool[] conflicts = new Tool[]{moveTool, scaleTool, rotationTool};
        addToggleMapping(KeyInput.KEY_G, new ToggleMapping(moveTool).setConflicts(conflicts));
        addToggleMapping(KeyInput.KEY_S, new ToggleMapping(scaleTool).setConflicts(conflicts));
        addToggleMapping(KeyInput.KEY_R, new ToggleMapping(rotationTool).setConflicts(conflicts));
        setActivated(gridTool, true);
        setActivated(cameraTool, true); 
        setActivated(pickTool, true);
        setActivated(modeTool, true);
        setActivated(moveTool, true);
    }
    
    @Override
    public void cleanup() {
        toolToggleMapping.stream().filter(t -> t.isInitialized()).forEach(t -> {t.cleanup();});
        super.cleanup(); 
    }
    
    private void addToggleMapping(int keyInput, ToggleMapping tm) {
        ToggleEvent tem = new ToggleEvent(tm);
        tem.bindKey(keyInput, false);
        tem.addListener(this);
        tem.initialize();
        toolToggleMapping.add(tem);
    }

    @Override
    public void onEvent(Event e) {
        if (!e.isMatch()) 
            return;
        ToggleEvent te = (ToggleEvent) e;
        Tool tool = te.toggleMapping.getTool();
        Tool[] conflicts = te.toggleMapping.getConflicts();
        for (Tool conflict : conflicts) {
            setActivated(conflict, false);
        }
        setActivated(tool, true);
    }
    
    private class ToggleEvent extends JmeEvent {
        public ToggleMapping toggleMapping;
        
        public ToggleEvent(ToggleMapping toggleMapping) {
            super("toggleEvent" + toggleMapping.getTool().getName());
            this.toggleMapping = toggleMapping;
        }
    }
    
    
    
}
