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
public class Title extends LinearLayout {

    public Title(float width, float height) {
        super(width, height);
        setBackground(UIFactory.getUIConfig().getBackground(), true);
        setBackgroundColor(UIFactory.getUIConfig().getTitleBgColor(), true);
        setLayout(Layout.horizontal);
    }
    
}
