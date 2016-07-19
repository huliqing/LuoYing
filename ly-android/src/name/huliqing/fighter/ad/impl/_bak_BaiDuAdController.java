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
//import com.baidu.appx.BDBannerAd;
//import com.baidu.appx.BDInterstitialAd;
//import name.huliqing.fighter.Global;
//
///**
// * 百度广告
// * @author huliqing
// */
//public class BaiDuAdController extends AndroidAbstractAdController {
//
//    private final static String API_KEY = "QgzIQGG4YCLQccON3dD6udMo";
//    private final static String AD_ID_BANNER = "LtXGiOdppny8CVcpaNfimB4w";
//    private final static String AD_ID_INSERT = "1T3OPwTbb4wmTRW1gVIzI14G";
//    
//    private BDInterstitialAd insertAd;
//    
//    // remove
////    public BaiDuAdController(ViewGroup bannerContainer) {
////        super(bannerContainer);
////    }
//    
//    @Override
//    public void init() {
//    }
//
//    @Override
//    protected View createAdBanner() {
//        BDBannerAd bad = new BDBannerAd((Activity) Global.getContext(), API_KEY, AD_ID_BANNER);
//        bad.setAdSize(BDBannerAd.SIZE_FLEXIBLE);
//        return bad;
//    }
//
//    @Override
//    protected void destoryAdBanner(View adView) {
//        if (adView instanceof BDBannerAd) {
//            ((BDBannerAd) adView).destroy();
//        }
//    }
//
//    @Override
//    protected void loadAdInsert(final AdInsertListener adInsertListener) {
//        // 百度的插屏初始化一次就可以，不能重复实例化，否则第二次new BDInterstitialAd的广告看不到.
//        // 在实始化一次后，后续重新进行loadAd就可以。
//        if (insertAd == null) {
//            insertAd = new BDInterstitialAd((Activity) Global.getContext(), API_KEY, AD_ID_INSERT);
//            insertAd.setAdListener(new BDInterstitialAd.InterstitialAdListener() {
//                public void onAdvertisementDataDidLoadSuccess() {
//                    adInsertListener.notifyAdInsertLoadOK();
//                }
//                public void onAdvertisementDataDidLoadFailure() {
//                    adInsertListener.notifyAdInsertLoadFailure();
//                }
//                public void onAdvertisementViewDidHide() {
//                    adInsertListener.notifyAdInsertClosed();
//                }
//                public void onAdvertisementViewDidShow() {}
//                public void onAdvertisementViewDidClick() {}
//                public void onAdvertisementViewWillStartNewIntent() {}
//            });
//        }
//        insertAd.loadAd();
//    }
//
//    @Override
//    protected void showAdInsert() {
//        if (insertAd != null && insertAd.isLoaded()) {
//            insertAd.showAd();
//        }
//    }
//    
//}
