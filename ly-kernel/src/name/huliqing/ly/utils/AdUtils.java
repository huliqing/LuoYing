/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.utils;

/**
 * 用于控制广告的显示或隐藏
 * @author huliqing
 */
public class AdUtils {
    
    /** 
     * 开始显示开启广告的时间,确保经过一段时间后关闭该广告,
     * 每次显示开屏广告的时候
     */ 
    public static long begin_show_openAD_time;
    
    private static AdController adController;
    
    /**
     * 注册一个广告控制器
     * @param adController 
     */
    public static void setAdController(AdController adController) {
        AdUtils.adController = adController;
    }
    
    /**
     * 显示广告
     * @param type
     */
    public static void showAd(AdType type) {
        if (adController != null) {
            adController.showAd(type);
        }
    }
    
    /**
     * 隐藏广告
     * @param types
     */
    public static void hideAd(AdType... types) {
        if (adController != null) {
            adController.hideAd(types);
        }
    }
}
