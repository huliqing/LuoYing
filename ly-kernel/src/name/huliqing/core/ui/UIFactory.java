/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.ui;

import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;

/**
 *
 * @author huliqing
 */
public class UIFactory {
    
    private static UIConfig uiConfig;
    
    /**
     * 获取默认的UI配置
     * @return 
     */
    public static UIConfig getUIConfig() {
        if (uiConfig == null) {
            AssetManager assetManager = new DesktopAssetManager(
                    Thread.currentThread().getContextClassLoader().getResource("com/jme3/asset/Desktop.cfg"));
            uiConfig = new UIConfigImpl(assetManager);
        }
        return uiConfig;
    }
    
    /**
     * 替换默认的UI配置
     * @param uiConfig 
     */
    public static void registerUIConfig(UIConfig uiConfig) {
        UIFactory.uiConfig = uiConfig;
    }
}
