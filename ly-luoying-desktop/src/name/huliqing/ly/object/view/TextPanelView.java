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
package name.huliqing.ly.object.view;

import name.huliqing.luoying.data.EntityData;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.ui.Window;

/**
 * 用于显示文字信息的界面组件
 * @author huliqing
 */
public class TextPanelView extends TextView {
    
    private String title;
    private Window win;

    @Override
    public void setData(EntityData data) {
        super.setData(data); 
        title = data.getAsString("title");
        if (title == null) {
            title = ResourceManager.getObjectName(data);
        }
        
        win = new Window(viewRoot.getWidth(), viewRoot.getHeight());
        win.setTitle(title);
        win.setCloseable(false);
        win.setPadding(10, 10, 10, 10);
        
        viewRoot.removeView(textUI);
        win.addView(textUI);
        viewRoot.addView(win);
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("title", title);
    }
    
    @Override
    protected void doViewInit() {
        super.doViewInit();
        textUI.setWidth(win.getContentWidth());
        textUI.setHeight(win.getContentHeight());
        
        // remove20160420,会导致拖动不了
        // FIX bug，当存在win时不能再拖动viewRoot,应该只拖动win就可以,否则会有一些
        // 拖动时的跳跃问题，影响操作体验
//        viewRoot.setDragEnabled(false);
//        if (dragEnabled) {
//            win.setDragEnabled(dragEnabled);
//        }
        
        viewRoot.setDragEnabled(dragEnabled);
        
    }
    
    public void setTitle(String title) {
        this.title = title;
        win.setTitle(title);
        data.setAttribute("title", title);
    }
    
   
    
}
