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
package name.huliqing.ly.view;

import name.huliqing.luoying.LuoYing;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.Icon;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.Window;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.ly.constants.InterfaceConstants;
import name.huliqing.ly.constants.ResConstants;

/**
 *
 * @author huliqing
 */
public class HelpView extends LinearLayout {
    
    private String fullText;
    // 基本描述
    private Text textLabel;
    // "更多"按钮
    private Icon moreIcon;
    // "更多"内容
    private Window moreWin;
    
    private float winWidth = LuoYing.getSettings().getWidth() * 0.5f;
    private float winHeight = LuoYing.getSettings().getHeight() * 0.5f;
    
    /**
     * @param text 帮助信息
     * @param height
     * @param width 宽度
     */
    public HelpView(float width, float height, String text) {
        super(width, height);
        this.fullText = text;
        this.setLayout(Layout.horizontal);
        
        // 如果文字太多，超过给定宽度，则隐藏一些文字
        Text tempLabel = new Text(text, UIFactory.getUIConfig().getDesColor());
        float fullCharWidth = tempLabel.getWidth();
        if (fullCharWidth > width) {
            float singleCharWidth = fullCharWidth / text.length(); // 每字符宽度
            float helpWidth = height * 0.8f;                   // "?"图标的宽度64*64
            float moreWidth = singleCharWidth * 3;  // "..."的宽度
            float availableCharWidth = width - helpWidth;   // 可用的文字区宽度,含"..."
            int textSize = (int) ((availableCharWidth - moreWidth) / singleCharWidth); // 用显示的文字数
            String availableText = text.substring(0, textSize - 1);
            
            // 这里要重新生成Text，否则宽度存在问题
            textLabel = new Text(availableText + "...", UIFactory.getUIConfig().getDesColor());
            textLabel.setHeight(height);
            
            // 帮助图标 
            moreIcon = new Icon(InterfaceConstants.UI_HELP);
            moreIcon.setWidth(helpWidth);
            moreIcon.setHeight(helpWidth);
            moreIcon.setMargin(0, 0, 0, height * 0.1f);
            moreIcon.addClickListener(new Listener() {
                @Override
                public void onClick(UI ui, boolean isPress) {
                    if (isPress) return;
                    if (moreWin.getParent() != null) {
                        moreWin.removeFromParent();
                    } else {
                        UIState.getInstance().addUI(moreWin);
                    }
                }
            });
            
            // moreWin
            moreWin = createMoreWin();
            moreWin.setToCorner(Corner.CC);
            
            addView(textLabel);
            addView(moreIcon);
        } else {
            textLabel = tempLabel;
            textLabel.setHeight(height);
            addView(textLabel);
        }
        textLabel.setFontColor(UIFactory.getUIConfig().getDesColor());
        
    }
    
    private Window createMoreWin() {
        Window win = new Window(winWidth, winHeight);
        win.setTitle(ResourceManager.get(ResConstants.COMMON_TIP));
        win.setPadding(10, 10, 10, 10);
        win.setDragEnabled(true);
        win.setCloseable(true);
        
        Text text = new Text(fullText);
        text.setWidth(win.getContentWidth());
        win.addView(text);
        win.resize();
        win.setToCorner(Corner.CC);
        return win;
    }
}
