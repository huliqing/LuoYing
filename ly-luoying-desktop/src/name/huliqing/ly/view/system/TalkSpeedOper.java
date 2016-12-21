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
import name.huliqing.ly.constants.InterfaceConstants;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.luoying.ui.FrameLayout;
import name.huliqing.luoying.ui.Icon;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.Window;
import name.huliqing.ly.LyConfig;

/**
 * 游戏音量大小调整界面
 * @author huliqing
 * @since 1.3
 */
public class TalkSpeedOper extends Window {
    private RowSimple talkSpeed;
    
    // 阅读速度，这个值表示每秒钟能够读取多少个字。
    private int speed;
    private int speedMin = 1;
    private int speedMax = 40;
    
    private Text speedText;
    private WrapIcon add;
    private WrapIcon subtract;
    
    public TalkSpeedOper(float width, float height, RowSimple talkSpeed) {
        super(width, height);
        setTitle(ResourceManager.get("system.ui.talkSpeed"));
        setLayout(Layout.horizontal);
        
        this.talkSpeed = talkSpeed;
        
        add = new WrapIcon(InterfaceConstants.UI_ADD);
        subtract = new WrapIcon(InterfaceConstants.UI_SUBTRACT);
        speedText = new Text("");
        
        addView(subtract);
        addView(speedText);
        addView(add);
        
        this.add.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                add();
            }
        });
        
        this.subtract.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                subtract();
            }
        });
        
        this.setToCorner(Corner.CC);
        this.setCloseable(true);
        this.setDragEnabled(true);
        
        // 初始化
        this.setSpeed((int) (1 / LyConfig.getSpeakTimeWorld()));
    } 
    
    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        
        float w = getContentWidth();
        float h = getContentHeight();
        
        subtract.setWidth(w * 0.4f);
        subtract.setHeight(h);
        
        speedText.setWidth(w * 0.2f);
        speedText.setHeight(h);
        speedText.setAlignment(BitmapFont.Align.Center);
        speedText.setVerticalAlignment(BitmapFont.VAlign.Center);
        
        add.setWidth(w * 0.4f);
        add.setHeight(h);
        
    }
    
    /**
     * @param speed 
     */
    public final void setSpeed(int speed) {
        this.speed = MathUtils.clamp(speed, speedMin, speedMax);
        this.speedText.setText(this.speed + "");
        LyConfig.setSpeakTimeWorld(1.0f / this.speed);
        talkSpeed.setRowDes(ResourceManager.get("system.ui.talkSpeed.des", new Object[] {speedText.getText()}));
    }
    
    private void add() {
        speed += 1;
        setSpeed(speed);
    }
    
    private void subtract() {
        speed -= 1;
        setSpeed(speed);
    }
    
    private class WrapIcon extends FrameLayout {
        private final Icon icon;
        
        public WrapIcon(String file) {
            super();
            icon = new Icon(file);
            setBackground(UIFactory.getUIConfig().getBackground(), true);
            setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
            setBackgroundVisible(false);
            addView(icon);
        }

        @Override
        protected void clickEffect(boolean isPress) {
            setBackgroundVisible(isPress);
        }

        @Override
        protected void updateViewLayout() {
            super.updateViewLayout(); 
            icon.setToCorner(Corner.CC);
        }

        @Override
        public void onRelease() {
            super.onRelease();
            setBackgroundVisible(false);
        }
    }
}
