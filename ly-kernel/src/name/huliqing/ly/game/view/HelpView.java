/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.game.view;

import name.huliqing.core.LY;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.ui.UIFactory;
import name.huliqing.core.ui.Icon;
import name.huliqing.core.ui.LinearLayout;
import name.huliqing.core.ui.Text;
import name.huliqing.core.ui.UI;
import name.huliqing.core.ui.Window;
import name.huliqing.core.ui.state.UIState;

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
    
    private float winWidth = LY.getSettings().getWidth() * 0.5f;
    private float winHeight = LY.getSettings().getHeight() * 0.5f;
    
    /**
     * @param guiNode
     * @param text 帮助信息
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
            moreIcon = new Icon("Interface/icon/help.png");
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
        win.setTitle(ResourceManager.get("common.tip"));
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
