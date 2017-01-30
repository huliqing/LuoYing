/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import name.huliqing.editor.events.Event;

/**
 * 地形降低工具
 * @author huliqing
 */
public class LowerTool extends AbstractTerrainTool {

    public LowerTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    protected void onToolEvent(Event e) {
    }
    
}
