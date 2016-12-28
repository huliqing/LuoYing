/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import com.jme3.collision.CollisionResults;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import name.huliqing.editor.Editor;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.select.SpatialSelectObj;
import name.huliqing.luoying.manager.PickManager;

/**
 * 选择物体的工具
 * @author huliqing
 */
public class PickTool extends EditTool {
    
    private final CollisionResults pickResults = new CollisionResults();
    
    public PickTool(String name) {
        super(name);
    }
    
    public JmeEvent bindPickEvent() {
        return bindEvent("pickEvent");
    }

    @Override
    public void onToolEvent(Event e) {
        if (e.isMatch()) {
            doPick();
        }
    }

    private void doPick() {
        Node editRoot = form.getEditRoot();
        pickResults.clear();
        PickManager.pick(editor.getCamera(), editor.getInputManager().getCursorPosition(), editRoot, pickResults);
        if (pickResults.size() > 0) {
            Spatial picked = pickResults.getClosestCollision().getGeometry();
            toolbar.getForm().setSelected(new SpatialSelectObj(picked));
        }
    }
    
}
