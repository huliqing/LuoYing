/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.ui;

import com.jme3.math.Vector3f;
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
    
    /**
     * 防止UI溢出屏幕外面，这个方法当UI有一部分溢出屏幕时会把UI尽可能的拉到屏幕内。如果整个UI已经都在屏幕内
     * ，则什么也不做。
     * @param ui
     * @param screenWidth
     * @param screenHeight 
     */
    public static void fixOverflowScreen(UI ui, float screenWidth, float screenHeight) {
        Vector3f pos = ui.getDisplay().getLocalTranslation();
        Vector3f wpos = ui.getDisplay().getWorldTranslation();
        float w = ui.getWidth();
        float h = ui.getHeight();
        if (wpos.x < 0) {
            pos.addLocal(-wpos.x, 0, 0);
        }
        if (wpos.x + w > screenWidth) {
            pos.addLocal(-(wpos.x + w - screenWidth), 0, 0);
        }
        if (wpos.y < 0) {
            pos.addLocal(0, -wpos.y, 0);
        }
        if (wpos.y + h > screenHeight) {
            pos.addLocal(0, -(wpos.y + h - screenHeight), 0);
        }
        ui.setPosition(pos.x, pos.y);
    }
}
