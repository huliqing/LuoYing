/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.ui.tiles;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapFont.Align;
import name.huliqing.ly.ui.FrameLayout;
import name.huliqing.ly.ui.Text;
import name.huliqing.ly.ui.UIFactory;

/**
 *
 * @author huliqing
 */
public class ColumnText extends FrameLayout {
    
    private Text text;
    
    public ColumnText(float width, float height, String text) {
        super(width, height);
        this.text = new Text(text);
        this.text.setWidth(width);
        this.text.setHeight(height);
        this.text.setAlignment(BitmapFont.Align.Center);
        this.text.setVerticalAlignment(BitmapFont.VAlign.Center);
        this.addView(this.text);
    }
    
    public void setText(String text) {
        this.text.setText(text);
    }
    
    public void setAlignment(Align aligh) {
        this.text.setAlignment(aligh);
    }
    
    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        this.text.setWidth(width);
        this.text.setHeight(height);
        float fontSize = UIFactory.getUIConfig().getBodyFontSize();
        if (fontSize > height) {
            this.text.setFontSize(height * 0.4f);
        } else {
            this.text.setFontSize(fontSize);
        }
    }
    
}
