/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view;

import com.jme3.font.BitmapFont;
import name.huliqing.core.constants.InterfaceConstants;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.ui.AbstractUI;
import name.huliqing.core.ui.Button;
import name.huliqing.core.ui.LinearLayout;
import name.huliqing.core.ui.Text;
import name.huliqing.core.ui.UI;
import name.huliqing.core.ui.UI.Listener;
import name.huliqing.core.ui.UIFactory;
import name.huliqing.core.ui.Window;
import name.huliqing.core.utils.MathUtils;

/**
 * 用于控制数量
 * @author huliqing
 */
public class NumPanel extends Window implements Listener {
    
    public interface NumConfirmListener {
        void onConfirm(NumPanel numPanel);
    }
    
    private LinearLayout bodyPanel;
    private IconPanel min;
    private IconPanel sub;
    private Text text;
    private IconPanel add;
    private IconPanel max;
    
    private Button button;
    
    // 数值范围限制
    private int minLimit = 0;
    private int maxLimit = Integer.MAX_VALUE;
    private int value;
    private NumConfirmListener nclistener;

    public NumPanel(float width, float height) {
        super(width, height);
        setCloseable(true);
        
        float cw = getContentWidth();
        float ch = getContentHeight();
        float btnHeight = UIFactory.getUIConfig().getButtonHeight();
        float bodyWidth = cw;
        float bodyHeight = ch - btnHeight;
        float iconWidth = bodyWidth * 0.2f;
        
        min = new IconPanel(iconWidth, bodyHeight, InterfaceConstants.UI_ARROW_LEFT2, 0.3f);
        sub = new IconPanel(iconWidth, bodyHeight, InterfaceConstants.UI_ARROW_LEFT1, 0.3f);
        add = new IconPanel(iconWidth, bodyHeight, InterfaceConstants.UI_ARROW_RIGHT1, 0.3f);
        max = new IconPanel(iconWidth, bodyHeight, InterfaceConstants.UI_ARROW_RIGHT2, 0.3f);
        
        text = new Text(value + "");
        text.setWidth(iconWidth);
        text.setHeight(bodyHeight);
        text.setBackgroundColor(UIFactory.getUIConfig().getDesColor(), false);
        text.setVerticalAlignment(BitmapFont.VAlign.Center);
        text.setAlignment(BitmapFont.Align.Center);
        // 给text添加一个空和事件,防止点击text时发生事件穿透
        text.addClickListener(AbstractUI.EMPTY_LISTENER);
        text.setEffectEnabled(false);
        
        bodyPanel = new LinearLayout(bodyWidth, bodyHeight);
        bodyPanel.setLayout(Layout.horizontal);
        bodyPanel.addView(min);
        bodyPanel.addView(sub);
        bodyPanel.addView(text);
        bodyPanel.addView(add);
        bodyPanel.addView(max);
        
        button = new Button(ResourceManager.get(ResConstants.COMMON_CONFIRM));
        button.setWidth(cw);
        button.setHeight(btnHeight);
        button.setFontSize(UIFactory.getUIConfig().getButtonFontSize());
        
        min.addClickListener(this);
        sub.addClickListener(this);
        add.addClickListener(this);
        max.addClickListener(this);
        button.addClickListener(this);
        
        addView(bodyPanel);
        addView(button);
    }

    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        
        String textValue = value + "";
        if (maxLimit != Integer.MAX_VALUE) {
            textValue += "/" + maxLimit;
        }
        text.setText(textValue);
    }

    @Override
    public void onClick(UI view, boolean isPressed) {
        if (isPressed) return;
        if (view == min) {
            min();
        } else if (view == sub) {
            sub();
        } else if (view == add) {
            add();
        } else if (view == max) {
            max();
        } else if (view == button) {
            // 触发确认侦听
            if (nclistener != null) {
                nclistener.onConfirm(this);
            }
        }
    }
    
    public void sub() {
        value -= 1;
        if (value < minLimit) {
            value = minLimit;
        }
        setNeedUpdate();
    }
    
    public void add() {
        value += 1;
        if (value > maxLimit) {
            value = maxLimit;
        }
        setNeedUpdate();
    }
    
    public void min() {
        // 如果没有设置过最小值限制则不处理
        if (minLimit == Integer.MIN_VALUE)
            return;
        
        value = minLimit;
        setNeedUpdate();
    }
    
    public void max() {
        if (maxLimit == Integer.MAX_VALUE)
            return;
        
        value = maxLimit;
        setNeedUpdate();
    }

    public void setMinLimit(int minLimit) {
        this.minLimit = minLimit;
        setNeedUpdate();
    }
    
    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
        setNeedUpdate();
    }
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = MathUtils.clamp(value, minLimit, maxLimit);
    }
    
    public void setNumConfirmListener(NumConfirmListener ncListener) {
        this.nclistener = ncListener;
    }
}
