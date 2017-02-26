/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.entity;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.events.Event;
import name.huliqing.luoying.data.EntityData;

/**
 * 实体刷的笔刷源列表,笔刷使用这些源来克隆到场景中
 * @author huliqing
 */
public class SourceTool extends AbstractEntityBrushTool {
    
    private final List<EntityData> sources = new ArrayList();

    public SourceTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    protected void onToolEvent(Event e) {
        // ignore
    }
    
    public void addSource(EntityData ed) {
        if (!sources.contains(ed)) {
            sources.add(ed);
        }
    }
    
    public boolean removeSource(EntityData ed) {
        return sources.remove(ed);
    }
    
    public List<EntityData> getSources() {
        return sources;
    }
}
