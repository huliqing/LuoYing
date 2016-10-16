/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.view;

import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import name.huliqing.luoying.ui.Checkbox;
import name.huliqing.luoying.ui.Checkbox.ChangeListener;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UI;

/**
 *
 * @author huliqing
 */
public class SimpleCheckbox extends LinearLayout {

    private Checkbox checkbox; // 如果为true，则过滤掉已经完成的任务
    private Text label;
    
    public SimpleCheckbox(String labelText) {
        super(128, 32);
        checkbox = new Checkbox();
        label = new Text(labelText);
        label.setVerticalAlignment(BitmapFont.VAlign.Center);
        label.addClickListener(new Listener() {
            @Override
            public void onClick(UI view, boolean isPressed) {
                if (isPressed) return;
                checkbox.setChecked(!checkbox.isChecked());
            }
        });
        setLayout(Layout.horizontal);
        addView(checkbox);
        addView(label);
    }
    
    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        checkbox.setWidth(height);
        checkbox.setHeight(height);
        label.setHeight(height);
        label.setMargin(5, 0, 0, 0);
    }
    
    public void setLabel(String label) {
        this.label.setText(label);
    }
    
    public boolean isChecked() {
        return checkbox.isChecked();
    }
    
    public void setChecked(boolean checked) {
        checkbox.setChecked(checked);
    }
    
    public void setFontColor(ColorRGBA color) {
        label.setFontColor(color);
    }
    
    public void setFontSize(float size) {
        label.setFontSize(size);
    }
    
    public void addChangeListener(ChangeListener listener) {
        checkbox.addChangeListener(listener);
    }
    
    public boolean removeChangeListener(Checkbox.ChangeListener listener) {
        return checkbox.removeChangeListener(listener);
    }
}
