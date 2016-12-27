/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import name.huliqing.editor.AbstractToolbar;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.EventListener;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.forms.EditForm;

/**
 * @author huliqing
 */
public class EditToolbar extends AbstractToolbar<EditForm> implements EventListener {

    private static final Logger LOG = Logger.getLogger(EditToolbar.class.getName());
    private final List<ToggleEvent> toolToggleMapping = new ArrayList<ToggleEvent>();
    
    private GridTool gridTool;
    private CameraTool cameraTool;
    private PickTool pickTool;
    private MoveTool moveTool;
    
    @Override
    public void initialize() {
        super.initialize();
        
        gridTool = new GridTool("gridTool");
        cameraTool = new CameraTool("CameraTool");
        pickTool = new PickTool("pickTool");
        moveTool = new MoveTool("moveTool");
        
        cameraTool.bindDragEvent().bindButton(MouseInput.BUTTON_MIDDLE, true).bindKey(KeyInput.KEY_LSHIFT, true);
        pickTool.bindPickEvent().bindButton(MouseInput.BUTTON_RIGHT, true);
        moveTool.bindMoveEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        
        add(gridTool);
        add(cameraTool);
        add(pickTool);
        add(moveTool);
        
        setActivated(gridTool.getName(), true);
        setActivated(cameraTool.getName(), true); 
        setActivated(pickTool.getName(), true);
        addToggleMapping(KeyInput.KEY_G, moveTool);
    }
    
    @Override
    public void cleanup() {
        toolToggleMapping.stream().filter(t -> t.isInitialized()).forEach(t -> {t.cleanup();});
        super.cleanup(); 
    }
    
    private void addToggleMapping(int keyInput, Tool tool) {
        ToggleEvent tem = new ToggleEvent(tool);
        tem.bindKey(keyInput, false);
        tem.addListener(this);
        tem.initialize();
        toolToggleMapping.add(tem);
    }

    @Override
    public void onEvent(Event e) {
        if (!e.isMatch()) 
            return;
        Tool tool = ((ToggleEvent) e).tool;
        setActivated(tool.getName(), true);
    }
    
    private class ToggleEvent extends JmeEvent {
        public Tool tool;
        
        public ToggleEvent(Tool tool) {
            super("toggleEvent" + tool.getName());
            this.tool = tool;
        }
    }
    
}
