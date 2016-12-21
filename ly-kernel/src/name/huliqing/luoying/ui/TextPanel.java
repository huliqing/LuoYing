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
package name.huliqing.luoying.ui;

import java.util.List;

/**
 * 文字信息面板
 * @author huliqing
 */
public class TextPanel extends Window {
    
    private boolean needResize;
    
    public TextPanel(String title, float width, float height) {
        super(width, height);
        setTitle(title);
        setPadding(10, 10, 10, 10);
    }

    @Override
    public void updateLogicalState(float tpf) {
        if (needResize) {
            resize();
            needResize = false;
        }
        super.updateLogicalState(tpf); 
    }

    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        float cw = getContentWidth();
        
        if (body != null && !body.getViews().isEmpty()) {
            List<UI> cvs = body.getViews();
            for (UI v : cvs) {
                v.setWidth(cw);
            }
        }
        
        if (footer != null && !footer.getViews().isEmpty()) {
            
            List<UI> cvs = footer.getViews();
            float fBtnWidth = width / cvs.size();
            for (UI v : cvs) {
                v.setWidth(fBtnWidth);
                v.setHeight(footerHeight);
            }
        }
    }
    
    public void addText(String text) {
        Text t = new Text(text);
        t.setWidth(getContentWidth());
        addView(t);
        needResize = true;
    }
    
    public void addText(Text text) {
        text.setWidth(getContentWidth());
        addView(text);
        needResize = true;
    }
    
    public void addButton(UI btn) {
        addFooter(btn);
        needResize = true;
    }
    
    public void addButton(String button, Listener listener) {
        Button btn = new Button(button);
        btn.addClickListener(listener);
        addFooter(btn);
        needResize = true;
    }

    @Override
    public void resize() {
        super.resize();
        needResize = false;
    }
    
}
