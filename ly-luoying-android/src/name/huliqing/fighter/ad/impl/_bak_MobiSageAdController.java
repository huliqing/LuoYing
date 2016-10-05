package name.huliqing.fighter.ad.impl;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.android.ad;
//
//import android.view.View;
//import android.view.ViewGroup;
//import com.mobisage.android.MobiSageAdBanner;
//import com.mobisage.android.MobiSageAdPoster;
//import com.mobisage.android.MobiSageAdPosterListener;
//import com.mobisage.android.MobiSageManager;
//import name.huliqing.fighter.Global;
//
///**
// *
// * @author huliqing
// */
//public class MobiSageAdController extends AndroidAbstractAdController {
//
//    private final static String PUBLISH_ID = "KSgp+ka9pDMZNgBzrg==";
//    private final static String TOKEN_BANNER = "z87PHKBYQtX/0OaVSFwU8kPp";
//    private final static String TOKEN_OPEN = "wsPCEa1VT9jy3euYRe/Y/07m";
//    private final static String TOKEN_INSERT = "y8rLGKRcRtH71OKRTJnR9kfs";
//    
//    private MobiSageAdBanner banner;
//    private MobiSageAdPoster insert;
//    
//    public MobiSageAdController(ViewGroup bannerContainer) {
//        super(bannerContainer);
//    }
//
//    @Override
//    public void init() {
//        MobiSageManager.getInstance().initMobiSageManager(Global.getContext(), PUBLISH_ID);
//    }
//
//    @Override
//    protected View createAdBanner() {
//        banner = new MobiSageAdBanner(Global.getContext(), TOKEN_BANNER);
//        return banner.getAdView();
//    }
//
//    @Override
//    protected void destoryAdBanner(View adView) {
//        if (banner != null) {
//            banner.destroyAdView();
//            banner = null;
//        }
//    }
//
//    @Override
//    protected void loadAdInsert(final AdInsertListener adInsertListener) {
//        insert = new MobiSageAdPoster(Global.getContext(), TOKEN_INSERT, MobiSageAdPoster.SIZE_300X250);
//        insert.setMobiSageAdPosterListener(new MobiSageAdPosterListener() {
//            public void onMobiSagePosterPreShow() {
//                adInsertListener.notifyAdInsertLoadOK();
//            }
//            public void onMobiSagePosterError(String string) {
//                adInsertListener.notifyAdInsertLoadFailure();
//            }
//            public void onMobiSagePosterClose() {
//                // 艾德插屏广告在用户关闭后必须销毁，以便下次重新请求
//                insert.destroyAdView();
//                adInsertListener.notifyAdInsertClosed();
//            }
//            public void onMobiSagePosterClick() {}
//        });
//    }
//
//    @Override
//    protected void showAdInsert() {
//        if (insert != null) {
//            insert.show();
//        }
//    }
//    
//}
