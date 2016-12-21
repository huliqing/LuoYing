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
public class SoundVolumeOper extends Window {
    private RowSimple soundVolume;
    private int volumeValue; // 0 ~ 10
    
    private Text volume;
    private WrapIcon add;
    private WrapIcon subtract;
    
    public SoundVolumeOper(float width, float height, RowSimple soundVolume) {
        super(width, height);
        setTitle(ResourceManager.get("system.soundVolume"));
        setLayout(Layout.horizontal);
        
        this.soundVolume = soundVolume;
        
        add = new WrapIcon(InterfaceConstants.UI_ADD, "SoundOper_btn_add");
        subtract = new WrapIcon(InterfaceConstants.UI_SUBTRACT, "SoundOper_btn_subtract");
        volume = new Text(volumeValue + "/10");
        
        addView(subtract);
        addView(volume);
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
        this.setVolume((int) (LyConfig.getSoundVolume() * 10));
    } 
    
    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        
        float w = getContentWidth();
        float h = getContentHeight();
        
        subtract.setWidth(w * 0.4f);
        subtract.setHeight(h);
        
        volume.setWidth(w * 0.2f);
        volume.setHeight(h);
        volume.setAlignment(BitmapFont.Align.Center);
        volume.setVerticalAlignment(BitmapFont.VAlign.Center);
        
        add.setWidth(w * 0.4f);
        add.setHeight(h);
        
    }
    
    /**
     * 设置声音大小 【0 - 10】
     * @param volume 
     */
    public final void setVolume(int volume) {
        if (volume < 0) volume = 0;
        if (volume > 10) volume = 10;
        this.volumeValue = volume;
        this.volume.setText(this.volumeValue + "");
        LyConfig.setSoundVolume(this.volumeValue * 0.1f);
        soundVolume.setRowDes(ResourceManager.get("system.soundVolume.des", new Object[] {this.volumeValue + "/10"}));
    }
    
    private void add() {
        volumeValue += 1;
        setVolume(volumeValue);
    }
    
    private void subtract() {
        volumeValue -= 1;
        setVolume(volumeValue);
    }
    
    private class WrapIcon extends FrameLayout {
        private Icon icon;
        
        public WrapIcon(String file, String name) {
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
