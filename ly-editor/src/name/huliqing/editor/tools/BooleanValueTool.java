/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import name.huliqing.editor.events.Event;

/**
 *
 * @author huliqing
 */
public class BooleanValueTool extends AbstractValueTool<Boolean>{

    public BooleanValueTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    protected void onToolEvent(Event e) {
        // ignore
    }
    
}
