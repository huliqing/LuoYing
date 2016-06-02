/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.ui.tiles;

import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import java.util.List;
import name.huliqing.fighter.ui.UIFactory;
import name.huliqing.fighter.ui.LinearLayout;
import name.huliqing.fighter.ui.Text;
import name.huliqing.fighter.ui.UI;

/**
 *
 * @author huliqing
 */
public class ColumnBody extends LinearLayout {
    
    private Text nameText;
    private Text desText;
    // 黄色
    private final ColorRGBA disableColor = new ColorRGBA(1f, 0.5f, 0, 1);
    
    public ColumnBody(float width, float height, String nameText, String desText) {
        super(width, height);
        this.nameText = new Text(nameText);
        this.desText = new Text(desText);
        this.desText.setFontColor(UIFactory.getUIConfig().getDesColor());
        this.desText.setFontSize(UIFactory.getUIConfig().getDesSize());
        this.addView(this.nameText);
        this.addView(this.desText);
    }
    
    public void setNameText(String name) {
        this.nameText.setText(name);
    }
    
    public void setDesText(String des) {
        this.desText.setText(des);
    }

    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        List<UI> cui = getViews();
        
        float textLimitHeight = height * 0.499f;
        float fontSize = UIFactory.getUIConfig().getBodyFontSize();
        for (UI child : cui) {
            Text text = (Text) child;
            text.setWidth(width * 0.999f);
            text.setHeight(textLimitHeight);
            
            text.setFontSize(fontSize > textLimitHeight ? textLimitHeight : fontSize);
            text.setVerticalAlignment(BitmapFont.VAlign.Center);
        }
    }
    
    public void setDisabled(boolean disabled) {
        if (disabled) {
            nameText.setFontColor(disableColor);
        } else {
            nameText.setFontColor(ColorRGBA.White);
        }
    }
    
}

