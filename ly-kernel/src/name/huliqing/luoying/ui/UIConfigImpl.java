/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.ui;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.constants.InterfaceConstants;

/**
 * UI的默认配置信息：统一UI的配置,如: border,button，font, size，sound
 * @author huliqing
 */
public class UIConfigImpl implements UIConfig {
    
    private AssetManager assetManager;
    
    private final ColorRGBA activeColor = new ColorRGBA(0, 1f, 0.5f, 0.85f);
    private final ColorRGBA titleBgColor = ColorRGBA.Gray.clone();
    private final ColorRGBA bodyBgColor = ColorRGBA.Black.mult(0.5f);
    private final ColorRGBA borderColor = ColorRGBA.LightGray.clone();
    private final ColorRGBA buttonBgColor = new ColorRGBA(0, 0.5f, 1, 1);
    private final ColorRGBA desColor = new ColorRGBA(0.5f, 0.5f, 0.5f, 1);
    
    private final String background = "LuoYing/Assets/Interface/UI/background.png";
    private final String buttonClose = "LuoYing/Assets/Interface/UI/button_close.png";
    private final String buttonConfirmOk = "LuoYing/Assets/Interface/UI/button_ok.png";
    private final String buttonConfirmCancel = "LuoYing/Assets/Interface/UI/button_cancel.png";
    
    private final String soundClick = "LuoYing/Assets/Interface/UI/sound_click.ogg";
    
    public UIConfigImpl(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    
    @Override
    public AssetManager getAssetManager() {
        return assetManager;
    }

    @Override
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    
    @Override
    public String getBackground() {
        return background;
    }
    
    @Override
    public ColorRGBA getTitleBgColor() {
        return titleBgColor;
    }

    @Override
    public ColorRGBA getBodyBgColor() {
        return bodyBgColor;
    }

    @Override
    public ColorRGBA getBorderColor() {
        return borderColor;
    }

    @Override
    public ColorRGBA getButtonBgColor() {
        return buttonBgColor;
    }
    
    @Override
    public ColorRGBA getScrollColor() {
        return ColorRGBA.LightGray.clone();
    }
    
    @Override
    public ColorRGBA getActiveColor() {
        return activeColor;
    }

    @Override
    public String getButtonClose() {
        return buttonClose;
    }

    @Override
    public String getButtonConfirmOk() {
        return buttonConfirmOk;
    }

    @Override
    public String getButtonConfirmCancel() {
        return buttonConfirmCancel;
    }
    
    /**
     * 获取统一的字体
     * @return 
     */
    @Override
    public BitmapFont getFont() {
        return LuoYing.getFont();
    }
    
    /**
     * 获取统一的title区域高度设置
     * @return 
     */
    @Override
    public float getTitleHeight() {
        return getScreenHeight() * 0.07f;
    }
    
    @Override
    public float getFooterHeight() {
        return getScreenHeight() * 0.06f;
    }

    @Override
    public ColorRGBA getFooterBgColor() {
        return ColorRGBA.LightGray;
    }
    
    /**
     * 获取默认的标题字体的大小
     * @return 
     */
    @Override
    public float getTitleSize() {
//        return getTitleHeight() * 0.75f;
        return getScreenHeight() * 0.04f;
    }
    
    /**
     * 获取默认的UI内容的字体大小
     * @return 
     */
    @Override
    public float getBodyFontSize() {
//        return getTitleSize() * 0.85f;
        return getScreenHeight() * 0.04f;
    }
    
    @Override
    public float getDesSize() {
//        return getBodyFontSize() * 0.75f;
        return getScreenHeight() * 0.035f;
    }

    @Override
    public ColorRGBA getDesColor() {
        return desColor;
    }
    
    @Override
    public ColorRGBA getButtonFontColor() {
        return ColorRGBA.White;
    }

    @Override
    public String getButtonBgFile() {
        return getBackground();
    }

    @Override
    public float getButtonWidth() {
        return 120;
    }

    @Override
    public float getButtonHeight() {
        return getScreenHeight() * 0.08f;
    }
    
    @Override
    public float getButtonFontSize() {
//        return getButtonHeight() * 0.65f;
        return getScreenHeight() * 0.045f;
    }

    @Override
    public boolean isSoundEnabled() {
        return true;
    }

    @Override
    public String getSoundClick() {
        return soundClick;
    }

    @Override
    public float getScreenWidth() {
        return LuoYing.getSettings().getWidth();
    }

    @Override
    public float getScreenHeight() {
        return LuoYing.getSettings().getHeight();
    }

    @Override
    public String getMissIcon() {
        return InterfaceConstants.UI_MISS;
    }

    @Override
    public float getListTitleHeight() {
//        return getTitleHeight() * 0.75f;
        return getScreenHeight() * 0.05f;
    }



}
