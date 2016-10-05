/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.ad;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import name.huliqing.fighter.utils.AdController;
import name.huliqing.fighter.utils.AdType;

/**
 *
 * @author huliqing
 */
public abstract class AndroidAbstractAdController implements AdController,AdInsertListener{
    protected Handler handler = new Handler();
    private boolean init;
    
    // ==== 横幅广告，可能在隐藏时需要销毁
    private ViewGroup viewBanner;
    
    // ==== 插屏广告
    // 是否提请了显示插屏广告的请求,只有viewInsertAdNeedShow为true时才应该显示
    // 插屏广告，这时候如果还没有准备好广告(viewInsertAdReady=false)，则应该先
    // 下载广告,等待下载好后再根据viewInsertAdNeedShow判断是否显示
    private boolean viewInsertAdNeedShow;
    // 插屏广告是否已经准备好(即是否已经下载好)
    private boolean viewInsertAdReady;
    // 是否正在加载插屏广告
    private boolean viewInsertAdLoading;
    
    // -------
    protected boolean debug;
    
    public void setBannerContainer(ViewGroup bannerContainer) {
        this.viewBanner = bannerContainer;
    }
    
    public void setDebug(boolean bool) {
        this.debug = bool;
    }
    
    protected void debug(final String message) {
        if (debug) {
            Toast.makeText(AdManager.getInstance().getContext(), getClass().getSimpleName() + ":" + message, Toast.LENGTH_SHORT).show();
        }
    }
    
    private void checkInit() {
        if (!init) {
            init();
            init = true;
        }
    }
    
    @Override
    public void showAd(final AdType... types) {
        if (types == null || types.length <= 0) {
            debug("types could not be null!");
            return;
        }
        checkInit();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    for (AdType type : types) {
                        if (type == AdType.banner) {
                            showAdBanner();
                        } else if (type == AdType.insert) {
                            wantToShowAdInsert();
                        } else if (type == AdType.open) {
//                            showAdOpen();
                        }
                    }
                } catch (Throwable e) {
                    Log.d(getClass().getSimpleName(), "Error when show ad!", e);
                }
            }
        });
    }
    
    @Override
    public void hideAd(final AdType... types) {
        if (types == null || types.length <= 0) {
            debug("types could not be null!");
            return;
        }
        checkInit();
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (AdType type : types) {
                    if (type == AdType.banner) {
                        hideAdBanner();
                    } else if (type == AdType.insert) {
                        wantToHideAdInsert();
                    } else if (type == AdType.open) {
//                        hideAdOpen();
                    } 
                }
            }
        });
    }
     
    /**
     * 提前载入插屏广告，但不显示，除非指定了要显示。
     */
    public final void preloadViewInsertAd() {
        checkInit();
        if (!viewInsertAdReady && !viewInsertAdLoading) {
            debug("Preload viewInsert Start!");
            viewInsertAdLoading = true;
            loadAdInsert(this);
        }
    }
    
    /**
     * 提前在后台载入banner,但不立即显示。
     */
    public final void preloadBannerAd() {
        if (viewBanner == null) {
            debug("No ViewBanner set! could not to preloadBannerAd!!!");
            return;
        }
        if (viewBanner.getChildCount() <= 0) {
            debug("preload BannerAd...");
            View adView = createAdBanner();
            if (adView != null) {
                viewBanner.addView(adView);
            }
        }
    }
    
    // banner在退出时要主动毁灭
    private void showAdBanner() {
        if (viewBanner == null) {
            debug("No ViewBa set!");
            return;
        }
        debug("Start show showAdBanner");
        if (viewBanner.getChildCount() <= 0) {
            View adView = createAdBanner();
            if (adView != null) {
                viewBanner.addView(adView);
            }
        }
        viewBanner.setVisibility(View.VISIBLE);
    }
    
    private void hideAdBanner() {
        if (viewBanner == null) {
            return;
        }
        int childCount = viewBanner.getChildCount();
        for (int i = 0; i < childCount; i++) {
            destoryAdBanner(viewBanner.getChildAt(i));
        }
        viewBanner.removeAllViews();
        viewBanner.setVisibility(View.GONE);
    }
    
    private void wantToShowAdInsert() {
        debug("Need to show viewInsert");
        // 标记为需要显示
        viewInsertAdNeedShow = true;
        if (viewInsertAdReady) {
            showAdInsert();
            debug("Show viewInsert ok.");
        } else {
            // 预加载
            preloadViewInsertAd();
        }
    }
    
    private void wantToHideAdInsert() {
        debug("viewInsertNeedShow to false.");
        // 这里只要标记“不需要显示”就行，不需要关闭view或销毁广告
        // 关闭或销毁由用户手动操作
        viewInsertAdNeedShow = false;
    }
    
    /**
     * 通知插屏广告已经加载完毕
     */
    @Override
    public void notifyAdInsertLoadOK() {
        viewInsertAdLoading = false;
        viewInsertAdReady = true;
        if (viewInsertAdNeedShow) {
            showAdInsert();
        }
    }
    
    /**
     * 通知插屏广告loading时发生错误，以便重新加载新的。
     */
    @Override
    public void notifyAdInsertLoadFailure() {
        viewInsertAdReady = false;
        viewInsertAdLoading = false;
        // 重新预加载新的插屏
        preloadViewInsertAd();
    }
    
    /**
     * 当用户关闭了插屏广告之后要调用这个方法来通知。
     */
    @Override
    public void notifyAdInsertClosed() {
        viewInsertAdReady = false;
        viewInsertAdLoading = false;
        viewInsertAdNeedShow = false;
        // 重新预加载新的插屏
        preloadViewInsertAd();
    }
   
    /**
     * 初始化
     */
    public abstract void init();
    
    /**
     * 创建横幅广告
     * @return 
     */
    protected abstract View createAdBanner();
    
    /**
     * 消毁横幅广告
     * @param adView 
     */
    protected abstract void destoryAdBanner(View adView);
    
    /**
     * 加载插屏广告，注：插屏广告加载过程中需要主动通知侦听器（调用以下几个方法）：<br />
     * notifyAdInsertLoadOK, notifyAdInsertLoadError, notifyAdInsertClosed
     * @param adInsertListener 侦听器
     */
    protected abstract void loadAdInsert(AdInsertListener adInsertListener);
    
    /**
     * 显示插屏广告，这个时间的插屏广告应该是已经准备好的了
     */
    protected abstract void showAdInsert();

}
