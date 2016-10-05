/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.ad;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import name.huliqing.fighter.ad.impl.AdMob;

/**
 *
 * @author huliqing
 */
public class AdManager {

    public enum Ad {
        BaiTong,
        AdMob,
        None;
    }
    private final static AdManager INSTANCE = new AdManager();
    private Context context;

    // -------------------------------------------------------------------------
    private AdManager() {
    }

    public static AdManager getInstance() {
        return INSTANCE;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    /**
     * 获取广告控制器，如果找不到支持的则返回null.
     * @param context
     * @param adType 广告类型
     * @param bannerContainer
     * @return 
     */
    public AndroidAbstractAdController createAdController(Context context, Ad adType, ViewGroup bannerContainer) {
        this.context = context;
        try {
            Class<? extends AndroidAbstractAdController> clz = getClass(adType);
            if (clz != null) {
                AndroidAbstractAdController adc = clz.newInstance();
                adc.setBannerContainer(bannerContainer);
                return adc;
            }
        } catch (Exception ex) {
            Log.e("AdFactory", "Could not createAdController by adType=" + adType, ex);
        }
        return null;
    }
    
    private Class<? extends AndroidAbstractAdController> getClass(Ad adType) {
        switch (adType) {
            case AdMob:
                return AdMob.class;
            case BaiTong:
//                return BaiTongAdController.class;
                return null;
            case None:
            default:
                return null;
        }
    }
}
