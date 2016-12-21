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
package name.huliqing.ly.state.lan;

import com.jme3.math.ColorRGBA;
import java.util.List;
import name.huliqing.ly.view.SimpleBtn;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.UI;

/**
 * @author huliqing
 */
public class LanStateBtnPanel extends LinearLayout {
    private LanState lanState;
    private SimpleBtn btnEnter;
    private SimpleBtn btnNew;
    private SimpleBtn btnBack;
    private SimpleBtn btnManual;
    
    public LanStateBtnPanel(float width, float height, LanState _lanState) {
        super();
        this.width = width;
        this.height = height;
        this.lanState = _lanState;
        setBackground(UIFactory.getUIConfig().getBackground(), true);
        setBackgroundColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f), true);
        setLayout(Layout.horizontal);

        btnEnter = new SimpleBtn(ResourceManager.get("lan.enterRoom"));
        btnNew = new SimpleBtn(ResourceManager.get("lan.newRoom"));
        btnBack = new SimpleBtn(ResourceManager.get("lan.back"));
        btnManual = new SimpleBtn(ResourceManager.get("lan.manual"));

        addView(btnBack);
        addView(btnNew);
        addView(btnEnter);
        addView(btnManual);
        
        btnEnter.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                lanState.enterRoom();
            }
        });

        // 新建房间
        btnNew.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) {return;}
                lanState.createRoom();
            }
        });

        // 返回主界面
        btnBack.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) {return;}
                lanState.backToStart(); 
            }
        });
        
        btnManual.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                lanState.showManualPanel();
            }
        });
    }

    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        float btnHeight = height;
        float mgTop = btnHeight * 0.1f;
        float mgLeft = 20;
        List<UI> cuis = getViews();

        for (UI ui : cuis) {
            ui.setHeight(btnHeight - mgTop * 2);
            ui.setMargin(mgLeft, mgTop, 0, 0);
        }
    }

    
}
