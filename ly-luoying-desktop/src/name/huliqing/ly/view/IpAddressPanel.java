/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view;

import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import java.util.List;
import name.huliqing.ly.constants.InterfaceConstants;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.ui.Button;
import name.huliqing.luoying.ui.Icon;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.Window;

/**
 * IP地址输入界面,用于手动连接服务器
 * @author huliqing
 */
public class IpAddressPanel extends Window {
    
    private IpPanel ipPanel;
    // 分隔线
    private Icon line;
    // 用于输入IP的数字面板
    private NumPanel numPanel;
    // 连接
    private Button confirm;
    
    public IpAddressPanel(float width, float height) {
        super(ResourceManager.get("lan.serverIp"), width, height);
        ipPanel = new IpPanel();
        line = new Icon(InterfaceConstants.UI_LINE_H);
        numPanel = new NumPanel();
        confirm = new Button(ResourceManager.get(ResConstants.COMMON_CONFIRM));
        confirm.setBackground(UIFactory.getUIConfig().getBackground(), true);
        confirm.setBackgroundColor(UIFactory.getUIConfig().getButtonBgColor(), true);
        addView(ipPanel);
        addView(line);
        addView(numPanel);
        addView(confirm);
    }
    
    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        float cw = getContentWidth();
        ipPanel.setWidth(cw);
        line.setWidth(cw - 10); line.setMargin(5, 0, 0, 0);
        numPanel.setWidth(cw);
        confirm.setWidth(cw);
        
        float lineHeight = 1;
        float ch = getContentHeight() - lineHeight;
        ipPanel.setHeight(ch * 0.15f);
        line.setHeight(lineHeight);
        numPanel.setHeight(ch * 0.7f);
        confirm.setHeight(ch * 0.15f);
    }
    
    /**
     * 设置确认按钮点击时的操作
     * @param listener 
     */
    public void setConfirmListener(Listener listener) {
        confirm.addClickListener(listener);
    }
    
    /**
     * 获取当前面板上的IP地址信息
     * @return 
     */
    public String getIpAddress() {
        return ipPanel.ipValue.getText();
    }
    
    // -------------------------------------------------------------------------
    
    private class NumPanel extends LinearLayout {
        private LinearLayout line1; // 1,2,3,4
        private LinearLayout line2; // 5,6,7,8
        private LinearLayout line3; // 9,0,.
        
        public NumPanel() {
            line1 = new LinearLayout();
            line1.setLayout(Layout.horizontal);
            line1.addView(new Digit("1"));
            line1.addView(new Digit("2"));
            line1.addView(new Digit("3"));
            line1.addView(new Digit("4"));
            line2 = new LinearLayout();
            line2.setLayout(Layout.horizontal);
            line2.addView(new Digit("5"));
            line2.addView(new Digit("6"));
            line2.addView(new Digit("7"));
            line2.addView(new Digit("8"));
            line3 = new LinearLayout();
            line3.setLayout(Layout.horizontal);
            line3.addView(new Digit("9"));
            line3.addView(new Digit("0"));
            line3.addView(new Digit("."));
            addView(line1);
            addView(line2);
            addView(line3);
        }

        @Override
        public void updateViewChildren() {
            super.updateViewChildren();
            line1.setWidth(width);
            line2.setWidth(width);
            line3.setWidth(width);
            
            line1.setHeight(height * 0.33f);
            line2.setHeight(height * 0.33f);
            line3.setHeight(height * 0.33f);
            
            updateDigitLayout(line1);
            updateDigitLayout(line2);
            updateDigitLayout(line3);
        }
        
        private void updateDigitLayout(LinearLayout line) {
            List<UI> cs = line.getViews();
            for (UI c : cs) {
                c.setWidth(width * 0.25f);
                c.setHeight(height * 0.33f);
            }
        }
        
    }
    
    private class Digit extends LinearLayout {
        private Text text;
        public Digit(String digit) {
            this.text = new Text(digit);
            this.text.setWidth(width);
            this.text.setHeight(height);
            this.text.setAlignment(BitmapFont.Align.Center);
            this.text.setVerticalAlignment(BitmapFont.VAlign.Center);
            addClickListener(new Listener() {
                @Override
                public void onClick(UI ui, boolean isPress) {
                    if (isPress) return;
                    ipPanel.ipValue.setText(ipPanel.ipValue.getText() + text.getText());
                }
            });
            setBackground(UIFactory.getUIConfig().getBackground(), true);
            setBackgroundColor(UIFactory.getUIConfig().getBodyBgColor(), true);
            addView(text);
        }

        @Override
        public void updateViewChildren() {
            super.updateViewChildren();
            text.setWidth(width);
            text.setHeight(height);
            text.setFontSize(height);
        }

        @Override
        protected void clickEffect(boolean isPress) {
            setBackgroundColor(isPress ? UIFactory.getUIConfig().getActiveColor() : UIFactory.getUIConfig().getBodyBgColor(), true);
        }

        @Override
        public void onRelease() {
            super.onRelease();
            setBackgroundColor(UIFactory.getUIConfig().getBodyBgColor(), true);
        }
        
    }
    
    private class IpPanel extends LinearLayout {
        private Text ipValue;
        private Icon delIcon;
        
        public IpPanel() {
            ipValue = new Text("");
            ipValue.setBackground(UIFactory.getUIConfig().getBackground(), true);
            ipValue.setBackgroundColor(ColorRGBA.DarkGray, true);
            delIcon = new Icon(InterfaceConstants.UI_BACK);
            delIcon.addClickListener(new Listener() {
                @Override
                public void onClick(UI ui, boolean isPress) {
                    if (isPress) return;
                    String text = ipValue.getText();
                    if (text.length() <= 0) {
                        return;
                    }
                    ipValue.setText(text.substring(0, text.length() - 1));
                }
            });
            setLayout(Layout.horizontal);
            addView(ipValue);
            addView(delIcon);
        }

        @Override
        public void updateViewChildren() {
            super.updateViewChildren();
            float iconW = height;
            float iconH = height;
            float wd = width - iconW;
            
            ipValue.setWidth(wd * 0.8f);
            delIcon.setWidth(iconW);
            
            ipValue.setHeight(height * 0.8f); 
            ipValue.setFontSize(height * 0.8f);
            delIcon.setHeight(iconH);
            
            ipValue.setMargin(wd * 0.05f, 0, 0, height * 0.1f);
        }
    }
}
