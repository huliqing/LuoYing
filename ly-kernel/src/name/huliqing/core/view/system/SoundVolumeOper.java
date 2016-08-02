/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view.system;

import com.jme3.font.BitmapFont;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.InterfaceConstants;
import name.huliqing.core.mvc.service.ConfigService;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.ui.FrameLayout;
import name.huliqing.core.ui.Icon;
import name.huliqing.core.ui.Text;
import name.huliqing.core.ui.UIFactory;
import name.huliqing.core.ui.UI;
import name.huliqing.core.ui.Window;

/**
 * 游戏音量大小调整界面
 * @author huliqing
 * @since 1.3
 */
public class SoundVolumeOper extends Window {
    private ConfigService configService = Factory.get(ConfigService.class);
            
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
        this.setVolume((int) (configService.getSoundVolume() * 10));
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
        configService.setSoundVolume(this.volumeValue * 0.1f);
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
