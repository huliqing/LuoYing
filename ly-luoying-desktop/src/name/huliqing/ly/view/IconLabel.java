/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view;

import com.jme3.font.BitmapFont;
import name.huliqing.luoying.ui.Icon;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.Text;

/**
 * 图标和Label
 * @author huliqing
 */
public class IconLabel<T> extends LinearLayout {
    // 标识
    private T id;
    
    private Icon icon;
    private Text label;
    
    public IconLabel(T id, String picFile, String text) {
        super(32, 32);
        this.id = id;
        icon = new Icon(picFile);
        label = new Text(text);
        label.setVerticalAlignment(BitmapFont.VAlign.Center);
        setLayout(Layout.horizontal);
        addView(icon);
        addView(label);
//        setDebug(true);
    }

    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        float iconWidth = height * 0.75f;
        float margin = 5;
        icon.setWidth(iconWidth);
        icon.setHeight(iconWidth);
        icon.setMargin(0, height * 0.125f, 0, 0);
        label.setWidth(width - iconWidth - margin);
        label.setHeight(height);
        label.setMargin(margin, 0, 0, 0);
    }
    
    public void setIcon(String iconFile) {
        icon.setImage(iconFile);
    }
    
    public void setLabel(String text) {
        label.setText(text);
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
    
}
