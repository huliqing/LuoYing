/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.system;

import com.jme3.font.BitmapFont;
import name.huliqing.luoying.constants.InterfaceConstants;
import name.huliqing.luoying.manager.ResourceManager;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.luoying.ui.FrameLayout;
import name.huliqing.luoying.ui.Icon;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.Window;
import name.huliqing.ly.Config;

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
        this.setSpeed((int) (1 / Config.getSpeakTimeWorld()));
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
        Config.setSpeakTimeWorld(1.0f / this.speed);
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
