/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.view.tiles;

import name.huliqing.fighter.ui.LinearLayout;
import name.huliqing.fighter.ui.UIFactory;

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
