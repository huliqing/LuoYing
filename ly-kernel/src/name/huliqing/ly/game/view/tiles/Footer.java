/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.game.view.tiles;

import name.huliqing.core.ui.LinearLayout;
import name.huliqing.core.ui.UIFactory;

/**
 *
 * @author huliqing
 */
public class Footer extends LinearLayout {
    
    public Footer(float width, float height) {
        super(width, height);
        setBackground(UIFactory.getUIConfig().getBackground(), false);
        setBackgroundColor(UIFactory.getUIConfig().getFooterBgColor(), false);
    }
}
