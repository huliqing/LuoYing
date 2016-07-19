package name.huliqing.fighter.ad.impl;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.ad.impl;
//
//import android.view.View;
//import com.baidu.ops.appunion.sdk.AppUnionSDK;
//import com.baidu.ops.appunion.sdk.InterstitialAdListener;
//import com.baidu.ops.appunion.sdk.banner.BaiduBanner;
//import com.baidu.ops.appunion.sdk.banner.BannerType;
//import name.huliqing.fighter.ad.AdInsertListener;
//import name.huliqing.fighter.ad.AdManager;
//import name.huliqing.fighter.ad.AndroidAbstractAdController;
//
///**
// *
// * @author huliqing
// */
//public class BaiTongAdController extends AndroidAbstractAdController{
//    
//    @Override
//    public void init() {
//        AppUnionSDK.getInstance(AdManager.getInstance().getContext()) .initSdk();
//    }
//
//    @Override
//    protected View createAdBanner() {
//        BaiduBanner banner = new BaiduBanner(AdManager.getInstance().getContext());
//        banner.setBannerType(BannerType.IMAGE_TEXT);
//        return banner;
//        
////        MainActivity ma = (MainActivity) Global.getContext();
////        ma.addContentView(banner, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
////        return null;
//    }
//
//    @Override
//    protected void destoryAdBanner(View adView) {
//        // ignore
//    }
//
//    @Override
//    protected void loadAdInsert(final AdInsertListener adInsertListener) {
//        AppUnionSDK.getInstance(AdManager.getInstance().getContext()).loadInterstitialAd(AdManager.getInstance().getContext()
//                , new InterstitialAdListener() {
//
//            @Override
//            public void onAdReady() {
//                adInsertListener.notifyAdInsertLoadOK();
//            }
//
//            @Override
//            public void onAdPresent() {
//                // ignore
//            }
//
//            @Override
//            public void onAdDismissed() {
//                adInsertListener.notifyAdInsertClosed();
//            }
//
//            @Override
//            public void onAdFailed(String string) {
//                adInsertListener.notifyAdInsertLoadFailure();
//            }
//        }, true);
//        
//    }
//
//    @Override
//    protected void showAdInsert() {
//        AppUnionSDK.getInstance(AdManager.getInstance().getContext()).showInterstitialAd();
//    }
//    
//}
