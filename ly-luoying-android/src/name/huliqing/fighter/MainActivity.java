package name.huliqing.fighter;
 
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import com.jme3.app.AndroidHarnessFragment;
import com.jme3.input.event.TouchEvent;
import java.util.logging.Level;
import java.util.logging.LogManager;
import name.huliqing.fighter.ad.AdManager;
import name.huliqing.fighter.ad.AdManager.Ad;
import name.huliqing.fighter.ad.AndroidAbstractAdController;
import name.huliqing.fighter.android.AndroidConfigServiceImpl;
import name.huliqing.fighter.android.AndroidSystemServiceImpl;
import name.huliqing.luoying.layer.service.ConfigService;
import name.huliqing.luoying.layer.service.SystemService;
import name.huliqing.luoying.utils.AdController;
import name.huliqing.luoying.utils.AdUtils;
import name.huliqing.luoying.Factory;
import name.huliqing.vc.VersionChecker;
 
public class MainActivity extends Activity {
    /*
     * Note that you can ignore the errors displayed in this file,
     * the android project will build regardless.
     * Install the 'Android' plugin under Tools->Plugins->Available Plugins
     * to get error checks and code completion for the Android project files.
     */
 
    public MainActivity(){
        // Set the default logging level (default=Level.INFO, Level.ALL=All Debug Info)
        LogManager.getLogManager().getLogger("").setLevel(Level.WARNING);
    }
    
    /**
     * 这个方法是在调用SimpleApplication.start()方法<b>之前</b>调用。
     */
    private void preStart() {
        // 重要配置Start =======================================================
        
        // ==== 设置全局对于Context的引用
        Global.setContext(MainActivity.this);
        
        // ==== 关于android特定环境相关。
        // 注：其它所有Service的注册替换都必须统一放在这里,避免混乱。
        // 重要说明：这里必须放在任何调用Service之前进行注册替换，不能放在layoutDisplay中再注册
        Factory.register(SystemService.class, AndroidSystemServiceImpl.class);
//        Factory.register(SaveService.class, AndroidSaveServiceImpl.class);
        Factory.register(ConfigService.class, AndroidConfigServiceImpl.class);
        
        // ==== 允许android进行udp广播和接收广播
        // 申请广播开启,这句不能重复调用，关闭时用lock.release(),这必须成对出现。
        // 这里不需要release是因为只开启一次，并且在应用关闭时让它自动释放就可以。
        WifiManager manager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);  
        WifiManager.MulticastLock lock= manager.createMulticastLock("ly3d-wifi");
        lock.acquire(); 
        
        // 检查查新版本
        checkNewVersion();
    }
    
    /**
     * 这个方法是在调用SimpleApplication.start()方法<b>之后</b>调用。
     */
    private void afterStart() {
        loadAd();
    }
    
    private void checkNewVersion() {
        // ==== Check new Version 
        // 注意：version.xml要以utf-8无BOM格式编码进行保存，否则xml解析会报错。可使用notepad进行转换格式。
        String dowloadDir = Environment.getExternalStorageDirectory()  + "/ly3d";
        VersionChecker vc = new VersionChecker();
        // 从1.6.0开始，version.xml文件移到ly3d目录下和adconfig一起
        vc.setVersionFile("http://app.huliqing.name/ly3d/version.xml");
        vc.checkVersion("ly3d", dowloadDir, this, false);
    }
    
    private void loadAd() {
        try {
            AdManager.getInstance().setContext(this);
            
            ViewGroup bannerContainer = (ViewGroup) findViewById(R.id.ad_container);
            AndroidAbstractAdController adc = AdManager.getInstance().createAdController(this, Ad.AdMob, bannerContainer);
            if (adc != null) {
                // 提前加载插屏
                adc.preloadBannerAd();
                adc.preloadViewInsertAd();
//                adc.setDebug(true);
                // 注册广告控制器
                AdUtils.setAdController((AdController) adc);
            }
        } catch (Throwable e) {
            Log.d(MainActivity.class.getSimpleName(), "!!!error!!!", e);
        }
    }
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        preStart();
        
        // Set window fullscreen and remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
 
        // find the fragment
        FragmentManager fm = getFragmentManager();
        AndroidHarnessFragment jmeFragment =
                (AndroidHarnessFragment) fm.findFragmentById(R.id.jmeFragment);
 
        // uncomment the next line to add the default android profiler to the project
//        jmeFragment.getJmeApplication().setAppProfiler(new DefaultAndroidProfiler());
        
        afterStart();
    }
 
 
    public static class JmeFragment extends AndroidHarnessFragment {
        public JmeFragment() {
            // Set main project class (fully qualified path)
            appClass = "name.huliqing.ly.Start";
 
            // Set the desired EGL configuration
            eglBitsPerPixel = 24;
            eglAlphaBits = 0;
            eglDepthBits = 16;
            eglSamples = 0;
            eglStencilBits = 0;
 
            // Set the maximum framerate
            // (default = -1 for unlimited)
            frameRate = -1;
 
            // Set the maximum resolution dimension
            // (the smaller side, height or width, is set automatically
            // to maintain the original device screen aspect ratio)
            // (default = -1 to match device screen resolution)
            maxResolutionDimension = -1;
 
            // Set input configuration settings
            joystickEventsEnabled = false;
            keyEventsEnabled = true;
            mouseEventsEnabled = true;
 
            // Set application exit settings
            finishOnAppStop = true;
            handleExitHook = true;
            exitDialogTitle = "Do you want to exit?";
            exitDialogMessage = "Use your home key to bring this app into the background or exit to terminate it.";
 
            // Set splash screen resource id, if used
            // (default = 0, no splash screen)
            // For example, if the image file name is "splash"...
            //     splashPicID = R.drawable.splash;
            splashPicID = 0;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState); 
        }

        @Override
        public void onTouch(String name, TouchEvent evt, float tpf) {
            if (name.equals("TouchEscape")) {
                // ignore: 不处理"返回键"的响应,退出时使用游戏中的“退出”
                return;
            }
            
            super.onTouch(name, evt, tpf);
        }
        
        
    }

    
}
