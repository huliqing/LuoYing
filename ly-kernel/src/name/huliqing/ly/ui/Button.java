/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.ui;

import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;

/**
 * 按钮
 * @author huliqing
 */
public class Button extends AbstractUI {

    private Text text;
    private boolean disabled;
    
    public Button(String text) {
        super(UIFactory.getUIConfig().getButtonWidth(), UIFactory.getUIConfig().getButtonHeight());
        createText(text);
    }

    public Button(String text, float width, float height) {
        super(width, height);
        createText(text);
    }
    
    private void createText(String str) {
        this.text = new Text(str);
        this.text.setFontColor(UIFactory.getUIConfig().getButtonFontColor());
        this.text.setFontSize(UIFactory.getUIConfig().getButtonFontSize());
        
        setBackground(UIFactory.getUIConfig().getButtonBgFile(), true);
        setBackgroundColor(UIFactory.getUIConfig().getButtonBgColor(), true);
        attachChild(this.text);
        
        if (this.text.getWidth() > width) {
            this.width = this.text.getWidth();
        }
        if (this.text.getHeight() > height) {
            this.height = this.text.getHeight();
        }
    }
    
    public void setFontSize(float fontSize) {
        this.text.setFontSize(fontSize);
        setNeedUpdate();
    }
    
    public void setFontColor(ColorRGBA color) {
        this.text.setFontColor(color);
        setNeedUpdate();
    }
    
    @Override
    public void updateView() {
        super.updateView();
       
        text.setWidth(width);
        text.setHeight(height);
        text.setVerticalAlignment(BitmapFont.VAlign.Center);
        text.setAlignment(BitmapFont.Align.Center);
        
        text.updateView();
        
    }
    
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean fireClick(boolean isPressed) {
        if (disabled) {
            return false;
        }
        return super.fireClick(isPressed); 
    }

    @Override
    public boolean fireDBClick(boolean isPressed) {
        if (disabled) {
            return false;
        }
        return super.fireDBClick(isPressed); 
    }

    @Override
    protected void clickEffect(boolean isPressed) {
        if (background != null) {
            background.setColor(isPressed ? 
                    UIFactory.getUIConfig().getActiveColor() : 
                    UIFactory.getUIConfig().getButtonBgColor());
        }
    }
    
}
