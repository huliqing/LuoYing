/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view;

import name.huliqing.core.ui.LinearLayout;
import name.huliqing.core.ui.UIFactory;

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
