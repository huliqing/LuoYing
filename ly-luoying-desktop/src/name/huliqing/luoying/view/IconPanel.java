/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.view;

import name.huliqing.ly.ui.Icon;
import name.huliqing.ly.ui.LinearLayout;
import name.huliqing.ly.ui.UIFactory;

/**
 *
 * @author huliqing
 */
public class IconPanel extends LinearLayout {
    
    private Icon tabIcon;
    private float factor;
    public IconPanel(float width, float height, String icon) {
        this(width, height, icon, 0.75f);
    }
    
    public IconPanel(float width, float height, String icon, float iconFactor) {
        super(width, height);
        this.factor = iconFactor;
        this.tabIcon = new Icon(icon);
        setBackground(UIFactory.getUIConfig().getBackground(), true);
        addView(tabIcon);
    }

    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        tabIcon.setWidth(height * factor);
        tabIcon.setHeight(height * factor);
    }

    @Override
    protected void updateViewLayout() {
        super.updateViewLayout();
        tabIcon.setToCorner(Corner.CC);
    }
}
