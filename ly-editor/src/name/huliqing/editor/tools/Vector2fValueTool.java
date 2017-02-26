/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import com.jme3.math.Vector2f;
import name.huliqing.editor.events.Event;

/**
 *
 * @author huliqing
 */
public class Vector2fValueTool extends AbstractValueTool<Vector2f> {

    public Vector2fValueTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    protected void onToolEvent(Event e) {}
    
}
