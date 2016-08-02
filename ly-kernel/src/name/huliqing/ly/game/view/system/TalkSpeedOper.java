/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.game.view.system;

import com.jme3.font.BitmapFont;
import java.text.DecimalFormat;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.InterfaceConstants;
import name.huliqing.core.game.service.ConfigService;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.utils.MathUtils;
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
public class TalkSpeedOper extends Window {
    private final ConfigService configService = Factory.get(ConfigService.class);
    private DecimalFormat decimalFormat;
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
        this.setSpeed((int) (1 / configService.getSpeakTimeWorld()));
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
        configService.setSpeakTimeWorld(1.0f / this.speed);
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
        private Icon icon;
        
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
