package name.huliqing.fighter.ad.impl;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.android.ad;
//
//import android.app.Activity;
//import android.view.View;
//import android.view.ViewGroup;
//import com.purplebrain.adbuddiz.sdk.AdBuddiz;
//import com.purplebrain.adbuddiz.sdk.AdBuddizDelegate;
//import com.purplebrain.adbuddiz.sdk.AdBuddizError;
//import com.purplebrain.adbuddiz.sdk.AdBuddizLogLevel;
//import name.huliqing.fighter.Global;
//
///**
// *
// * @author huliqing
// */
//public class AdBuddizAdController extends AndroidAbstractAdController {
//
//    private final static String PUBLISH_KEY = "25c4afe2-989b-470e-9977-e929ef065e68";
//    
//    public AdBuddizAdController(ViewGroup bannerContainer) {
//        super(bannerContainer);
//    }
//
//    @Override
//    public void init() {
//        AdBuddiz.setLogLevel(AdBuddizLogLevel.Silent);  // log level
//        AdBuddiz.setPublisherKey(PUBLISH_KEY);          // replace with your app publisher key
//        AdBuddiz.setDelegate(new AdBuddizDelegate() {
//            @Override
//            public void didCacheAd() {
//                notifyAdInsertLoadOK();
//            }
//            @Override
//            public void didShowAd() {
//            }
//            @Override
//            public void didFailToShowAd(AdBuddizError error) {
//                notifyAdInsertLoadFailure();
//            }
//            @Override
//            public void didClick() {
//            }
//            @Override
//            public void didHideAd() {
//                notifyAdInsertClosed();
//            }
//        });
//        // 不放在这里
////        AdBuddiz.cacheAds(context);                     // start caching ads
//    }
//
//    @Override
//    protected View createAdBanner() {
//        // 不要用banner,AdBuddiz没有banner，只有插屏，而且插屏太大
////        if (AdBuddiz.isReadyToShowAd(context)) {
////            AdBuddiz.showAd(context);
////        }
//        return null;
//    }
//
//    @Override
//    protected void destoryAdBanner(View adView) {
//        // donothing
//    }
//
//    @Override
//    protected void loadAdInsert(AdInsertListener adInsertListener) {
//        AdBuddiz.cacheAds((Activity) Global.getContext());
//    }
//
//    @Override
//    protected void showAdInsert() {
//        AdBuddiz.showAd((Activity) Global.getContext());
//    }
//    
//}
