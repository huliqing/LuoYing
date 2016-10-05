/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.ad.impl;

import android.view.View;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.*;
import name.huliqing.fighter.ad.AdInsertListener;
import name.huliqing.fighter.ad.AdManager;
import name.huliqing.fighter.ad.AndroidAbstractAdController;

/**
 *
 * @author huliqing
 */
public class AdMob extends AndroidAbstractAdController{
    
    // 广告应用ID
    // see => https://apps.admob.com/#account/appmgmt:
    private final static String APP_ID = "ca-app-pub-0155192758955646~3482679833";
    
    // 广告：横幅广告ID
    // see => https://apps.admob.com/#monetize/app:view/id=3482679833
    private final static String AD_UNIT_ID_BANNER = "ca-app-pub-0155192758955646/5778923038";
    
    // 广告：插屏广告ID
    // see => https://apps.admob.com/#monetize/app:view/id=3482679833
    private final static String AD_UNIT_ID_INSERT = "ca-app-pub-0155192758955646/8592788634";
    
    private AdView adView;

    @Override
    public void init() {
//        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(AdManager.getInstance().getContext(), APP_ID);
    }

    @Override
    protected View createAdBanner() {
        
        if (adView != null) {
            return adView;
        }
        
        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        adView = new AdView(AdManager.getInstance().getContext());
        // see => https://developers.google.com/android/reference/com/google/android/gms/ads/AdSize
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(AD_UNIT_ID_BANNER);

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        if (debug) {
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
            adView.loadAd(adRequest);
        } else {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        return adView;
    }

    @Override
    protected void destoryAdBanner(View view) {
        // 不要销毁，因为每次载入都不容易
//        if (adView != null) {
//            adView.destroy();
//            adView = null;
//        }
    }

    @Override
    protected void loadAdInsert(AdInsertListener al) {
        debug("暂不支持插屏");
    }

    @Override
    protected void showAdInsert() {
        debug("暂不支持插屏");
    }
    
}
