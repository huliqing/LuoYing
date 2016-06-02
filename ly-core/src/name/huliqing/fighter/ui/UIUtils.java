/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.ui;

import name.huliqing.fighter.Common;
import name.huliqing.fighter.ui.UI.Corner;
import name.huliqing.fighter.ui.UI.Listener;
import name.huliqing.fighter.ui.state.UIState;

/**
 *
 * @author huliqing
 */
public class UIUtils {
    
    /**
     * @deprecated 
     * @param pic
     * @return 
     */
    public static Icon createIcon(String pic) {
        float size = Common.getSettings().getHeight() * 0.1f;
        Icon icon = new Icon(pic);
        icon.setWidth(size);
        icon.setHeight(size);
        return icon;
    }
    
    public static UI createMultView(float width, float height, String foreground, String background) {
        LinearLayout layout = new LinearLayout(width, height);
        layout.setBackground(background, true);
        
        name.huliqing.fighter.ui.Icon child = new name.huliqing.fighter.ui.Icon();
        child.setWidth(width * 0.7f);
        child.setHeight(height * 0.7f);
        child.setImage(foreground);
        child.setUseAlpha(true);
        child.setMargin(width * 0.15f, height * 0.15f, 0, 0);
        
        layout.addView(child);
        return layout;
    }
    
    public static void showAlert(String title, String message, String confirm) {
        Text text = new Text(message);
        float textWidth = text.getWidth();
        final TextPanel tp = new TextPanel(title, textWidth, 0);
        tp.addText(text);
        tp.addButton(confirm, new Listener() {
            @Override
            public void onClick(UI view, boolean isPressed) {
                if (isPressed) return;
                tp.removeFromParent();
            }
        });
        tp.resize();
        tp.setToCorner(Corner.CC);
        UIState.getInstance().addUI(tp);
    }
}
