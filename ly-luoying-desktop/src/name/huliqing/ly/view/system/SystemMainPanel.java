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
package name.huliqing.ly.view.system;

import com.jme3.font.BitmapFont;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.LinearLayout.Layout;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.Window;
import name.huliqing.luoying.ui.tiles.Tab;

/**
 *
 * @author huliqing
 */
public class SystemMainPanel extends Window {
//    private final static Logger logger = Logger.getLogger(SystemMainPanel.class.getName());
    
    private Tab tab;
    
    // ==== tab
    // 声音设置
    private TabButton tabSound;
    // 快捷方式设置
    private TabButton tabUI;
    // 图形设置
    private TabButton tabPerformance;
    // Other:暂不开放，debug功能会导致关卡列表在debug模式下被自动打开。
    private TabButton tabOther;
    
    // ==== tab panel
    private SystemSoundPanel soundPanel;
    private SystemUIPanel shortcutPanel;
    private SystemOtherPanel otherPanel;
    private SystemPerformancePanel performancePanel;
    
    // ==== other
    // 当前激活的tab
    private int index = 0;
    // 每个tab的默认可显示行数
    private int globalSize = 5;

    public SystemMainPanel(float width, float height) {
        super(width, height);
        setTitle(ResourceManager.get("system.main"));
        setLayout(Layout.horizontal);
        
        init();
    }
    
    private void init() {
        // ==== Panels 
        soundPanel = new SystemSoundPanel(getContentWidth(), getContentHeight());
        soundPanel.setVisible(false);
        soundPanel.setPageSize(globalSize);
        
        shortcutPanel = new SystemUIPanel(getContentWidth(), getContentHeight());
        shortcutPanel.setVisible(false);
        shortcutPanel.setPageSize(globalSize);
        
        performancePanel = new SystemPerformancePanel(getContentWidth(), getContentHeight());
        performancePanel.setVisible(false);
        performancePanel.setPageSize(globalSize);
        
        // 暂不开放，debug功能会导致关卡列表在debug模式下被自动打开。
//        otherPanel = new SystemOtherPanel(getContentWidth(), getContentHeight());
//        otherPanel.setToCorner(Corner.CC);
//        otherPanel.setVisible(false);
//        otherPanel.setPageSize(globalSize);
//        bodyPanel.addView(otherPanel);
        
        // ==== tabs
        tabSound = new TabButton(ResourceManager.get("system.sound"), soundPanel);
        tabUI = new TabButton(ResourceManager.get("system.ui"), shortcutPanel);
        tabPerformance = new TabButton(ResourceManager.get("system.performance"), performancePanel);
//        tabOther = new TabButton(ResourceManager.get("system.other"), otherPanel);
        
        tab = new Tab(getContentWidth(), getContentHeight());
        tab.setLayout(Layout.horizontal);
        tab.addTab(tabSound, soundPanel);
        tab.addTab(tabUI, shortcutPanel);
        tab.addTab(tabPerformance, performancePanel);
        addView(tab);
    }
    
    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        tab.setWidth(getContentWidth());
        tab.setHeight(getContentHeight());
    }
    
    private class TabButton extends LinearLayout {
        
        private Text tabName;
        
        public TabButton(String text, UI contentPanel) {
            super();
            this.tabName = new Text(text);
            this.tabName.setAlignment(BitmapFont.Align.Center);
            this.tabName.setVerticalAlignment(BitmapFont.VAlign.Center);
            setBackground(UIFactory.getUIConfig().getBackground(), true);
            setBackgroundColor(UIFactory.getUIConfig().getBodyBgColor(), true);
            addView(tabName);
        }

        @Override
        public void updateViewChildren() {
            super.updateViewChildren();
            tabName.setWidth(width);
            tabName.setHeight(height);
        }
    }
}
