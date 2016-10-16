/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.view;

import java.util.ArrayList;
import name.huliqing.luoying.ui.Button;
import name.huliqing.luoying.ui.LinearLayout;

/**
 * 水平按钮组
 * @author huliqing
 */
public class ButtonPanel extends LinearLayout {

    private ArrayList<Button> btns;
    
    public ButtonPanel(float width, float height, String[] buttons) {
        super(width, height);
        setLayout(Layout.horizontal);
        btns = new ArrayList<Button>(buttons.length);
        for (int i = 0; i < buttons.length; i++) {
            Button button = new Button(buttons[i]);
            addView(button);
            btns.add(button);
        }
    }

    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        float btnWidth = width / btns.size();
        for (Button btn : btns) {
            btn.setWidth(btnWidth);
            btn.setHeight(height);
        }
    }

    /**
     * 给指定的按钮添加事件
     * @param buttonIndex
     * @param listener 
     */
    public void addClickListener(int buttonIndex, Listener listener) {
        btns.get(buttonIndex).addClickListener(listener);
    }
    
    
}
