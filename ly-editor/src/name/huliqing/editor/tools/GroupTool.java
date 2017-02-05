/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import com.jme3.util.SafeArrayList;
import java.util.List;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.toolbar.EditToolbar;

/**
 *
 * @author huliqing
 */
public class GroupTool extends AbstractTool {
    
    private final SafeArrayList<Tool> children = new SafeArrayList<Tool>(Tool.class);

    public GroupTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    public void initialize(SimpleJmeEdit edit, EditToolbar toolbar) {
        super.initialize(edit, toolbar);
        children.stream().filter(t -> !t.isInitialized()).forEach(t -> {t.initialize(edit, toolbar);});
    }

    @Override
    public void cleanup() {
        children.stream().filter(t -> t.isInitialized()).forEach(t -> {t.cleanup();});
        super.cleanup(); 
    }

    @Override
    protected void onToolEvent(Event e) {}
    
    public void addChild(Tool pt) {
        if (!children.contains(pt)) {
            children.add(pt);
            if (isInitialized() && !pt.isInitialized()) {
                pt.initialize(edit, toolbar);
            }
        }
    }
    
    public List<Tool> getChildren() {
        return children;
    }
    
    public boolean removeChild(Tool pt) {
        if (children.remove(pt)) {
            if (pt.isInitialized()) {
                pt.cleanup();
            }
            return true;
        }
        return false;
    }

    @Override
    public final void update(float tpf) {
        for (Tool t : children.getArray()) {
            t.update(tpf);
        }
    }
    
}
