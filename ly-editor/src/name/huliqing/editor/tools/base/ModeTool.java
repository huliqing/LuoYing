/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.editor.tools.base;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.edit.Mode;
import name.huliqing.editor.tools.AbstractTool;

/**
 *
 * @author huliqing
 */
public class ModeTool extends AbstractTool {
    
    public interface ModeChangedListener {
        void onModeChanged(Mode newMode);
    }
    
    private List<ModeChangedListener> modeChangeListeners;

    public ModeTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }
    
    public Mode getMode() {
        return edit.getMode();
    }
    
    public void setMode(Mode mode) {
        edit.setMode(mode);
    }
    
    /**
     * 绑定一个按键，用来切换不同的模式
     * @return 
     */
    public JmeEvent bindModeEvent() {
        return bindEvent(name + "modeEvent");
    }

    @Override
    protected void onToolEvent(Event e) {
        if (e.isMatch()) {
            Mode[] ms = Mode.values();
            int idx = edit.getMode().ordinal();
            Mode mode;
            if (idx >= ms.length - 1) {
                mode = ms[0];
            } else {
                mode = ms[++idx];
            }
            edit.setMode(mode);
            if (modeChangeListeners != null) {
                modeChangeListeners.forEach(t -> {t.onModeChanged(mode);});
            }
        }
    }
    
    public void addModeChangedListener(ModeChangedListener listener) {
        if (modeChangeListeners == null) {
            modeChangeListeners = new ArrayList<>();
        }
        if (!modeChangeListeners.contains(listener)) {
            modeChangeListeners.add(listener);
        }
    }
    public boolean removeModeChangedListener(ModeChangedListener listener) {
        return modeChangeListeners != null &&  modeChangeListeners.remove(listener);
    }
    
    
}
