/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.ui;

import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapFont.VAlign;

/**
 *
 * @author huliqing
 */
public class SimpleRow<T> extends Row<T> {
    
    protected Text text;
    
    public SimpleRow() {
        super();
        text = new Text("");
        text.setWidth(width);
        text.setHeight(height);
        addView(text);
    }
    
    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        text.setWidth(width);
        text.setHeight(height);
    }
    
    public void setAlignment(Align align) {
        text.setAlignment(align);
        setNeedUpdate();
    }
    
    public void setVerticalAlignment(VAlign align) {
        text.setVerticalAlignment(align);
        setNeedUpdate();
    }

    @Override
    public void displayRow(T data) {
//        if (datas.size() <= rowIndex) 
//            return;
        Object o = data;
        if (o == null)
            return;
        text.setText(o.toString());
    }
    
}
