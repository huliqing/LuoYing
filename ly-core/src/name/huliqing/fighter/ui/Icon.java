/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.ui;

import com.jme3.material.MatParamTexture;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author huliqing
 */
public class Icon extends AbstractUI {
    
    public Icon() {
        super();
        if (background == null) {
            createBackground();
            background.setUseAlpha(true); // default alpha
        }
    }
    
    public Icon(String file) {
        this();
        setImage(file);
    }
    
    public final void setImage(String file) {
        background.setFile(file);
        
        // 如果没有指定初始宽高,则默认使用图片的宽高.
        if (width <= 0 && height <= 0) {
            MatParamTexture matParam = background.getMaterial().getTextureParam("Texture");
            com.jme3.texture.Image image = matParam.getTextureValue().getImage();
            width = image.getWidth();
            height = image.getHeight();
        }
        
        setNeedUpdate();
    }
    
    public final void setColor(ColorRGBA color) {
        background.setColor(color);
    }

    public final void setUseAlpha(boolean useAlpha) {
        background.setUseAlpha(useAlpha);
    }

    @Override
    protected void clickEffect(boolean isPressed) {
        if (background != null) {
            background.setColor(isPressed ? UIFactory.getUIConfig().getActiveColor() : ColorRGBA.White);
        }
    }

    public Material getMaterial() {
        return background.getMaterial();
    }
}
