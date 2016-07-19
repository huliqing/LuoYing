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
    
    private final static String AD_UNIT_ID = "ca-app-pub-0155192758955646/5778923038";
    private AdView adView;

    @Override
    public void init() {
//        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(AdManager.getInstance().getContext(), AD_UNIT_ID);
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
        adView.setAdUnitId(AD_UNIT_ID);

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
    }

    @Override
    protected void showAdInsert() {
    }
    
}
