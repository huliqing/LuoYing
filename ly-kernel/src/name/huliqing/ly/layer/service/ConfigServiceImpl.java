/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.ly.Config;
import name.huliqing.ly.Factory;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.data.ConfigData;
import name.huliqing.ly.manager.ResManager;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.ly.object.sound.SoundManager;
//import name.huliqing.luoying.view.shortcut.ShortcutManager;
import name.huliqing.ly.xml.DataFactory;
import name.huliqing.ly.save.SaveConfig;
import name.huliqing.ly.save.SaveHelper;
import name.huliqing.ly.utils.MathUtils;

/**
 *
 * @author huliqing
 */
public class ConfigServiceImpl implements ConfigService {
    private SaveService saveService;
    
    // 用于标识游戏客户端唯一标识的ID的键。
    private final static String CLIENT_ID_KEY = "Client_ID";
    
    // 用于在本地多客户端测试,这个ID在每次启动时都重新生成，方便在桌面上测试时区别不同的客户端。
    private String clientIdForDebug;
    
    private ConfigData cd;
    
    private final List<ConfigListener> listeners = new ArrayList<ConfigListener>();

    @Override
    public void inject() {
        saveService = Factory.get(SaveService.class);
    }

    @Override
    public ConfigData getConfig() {
        return cd;
    }

    @Override
    public void loadGlobalConfig() {
        // 载入系统配置
        cd = DataFactory.createData(IdConstants.SYS_CONFIG);
//        Config.debug = cd.isDebug();
        
        // 载入用户保存的配置并整合到全局配置中，注：只处理部分保存的参数。
        SaveConfig sc = SaveHelper.loadConfig();
        if (sc != null && sc.getConfig() != null) {
            ConfigData scd = sc.getConfig();
            cd.setLocale(scd.getLocale());
            cd.setLocaleAll(scd.getLocaleAll());
            cd.setPort(scd.getPort());
            cd.setPortDiscoverClient(scd.getPortDiscoverClient());
            cd.setPortDiscoverServer(scd.getPortDiscoverServer());
            cd.setSoundEnabled(scd.isSoundEnabled());
            cd.setSoundVolume(scd.getSoundVolume());
            SoundManager.getInstance().setSoundEnabled(cd.isSoundEnabled());
            SoundManager.getInstance().setVolume(cd.getSoundVolume());
        }
    }
    
    @Override
    public String getGameName() {
        return cd.getGameName();
    }

    @Override
    public String getVersionName() {
        return cd.getVersionName();
    }

    @Override
    public int getVersionCode() {
        return cd.getVersionCode();
    }
    
    @Override
    public boolean isSoundEnabled() {
        return cd.isSoundEnabled();
    }

    @Override
    public void setSoundEnabled(boolean enabled) {
        cd.setSoundEnabled(enabled);
        SoundManager.getInstance().setSoundEnabled(enabled);
        notifyListtener();
    }

    @Override
    public float getSoundVolume() {
        return cd.getSoundVolume();
    }

    @Override
    public void setSoundVolume(float volume) {
        cd.setSoundVolume(MathUtils.clamp(volume, 0f, 1.0f));
        SoundManager.getInstance().setVolume(volume);
        notifyListtener();
    }

    @Override
    public String loadLocale() {
        cd.setLocale(detectLocale());
        return cd.getLocale();
    }

    @Override
    public void changeLocale(String locale) {
        cd.setLocale(locale);
        ResManager.setLocale(locale);
        
        // outdate
        ResourceManager.clearResources();
        
        // 保存配置
        SaveConfig saveConfig = new SaveConfig();
        saveConfig.setConfig(cd);
        SaveHelper.saveConfig(saveConfig);
        
        notifyListtener();
    }
    
    @Override
    public String getLocale() {
        if (cd == null) {
            loadGlobalConfig();
        }
        
        return cd.getLocale();
    }

    @Override
    public float getDropFactor() {
        return cd.getDropFactor();
    }

    @Override
    public int getPort() {
        return cd.getPort();
    }

    @Override
    public int getPortDiscoverServer() {
        return cd.getPortDiscoverServer();
    }

    @Override
    public int getPortDiscoverClient() {
        return cd.getPortDiscoverClient();
    }

    @Override
    public String[] getAllSupportedLocale() {
        String localAll = cd.getLocaleAll();
        if (localAll != null) {
            return localAll.split(",");
        }
        return null;
    }

//    @Override
//    public boolean isUseLight() {
//        return cd.isUseLight();
//    }
    
    // 检测及获取一个可用的语言环境。
    private String detectLocale() {
        String value = cd.getLocale();
        String[] localeAll = cd.getLocaleAll().split(",");
        String defLocale = localeAll[0];
        
        // 1.从配置和本地环境中查找，如果没有则使用默认配置
        if (value == null || value.equals("")) {
            value = Factory.get(SystemService.class).getLocale();
            if (value == null || value.equals("")) {
                return defLocale;
            }
        }
        
        // 2.优先从支持的语言环境中找出一个完全匹配的。
        for (String locale : localeAll) {
            if (value.equals(locale)) {
                return value;
            }
        }
        
        // 3.到这里，如果没有完全匹配的语言环境，则根据前缀匹配来查找环境
        String valuePrefix = value.split("_")[0];
        for (String locale : localeAll) {
            if (locale.startsWith(valuePrefix)) {
                return locale;
            }
        }
        
        // 4.前缀匹配也没有找到，则使用默认
        Logger.getLogger(ConfigServiceImpl.class.getName()).log(Level.WARNING
                , "Sorry, the locale {0} unsupported yet!"
                , new Object[] {value});
        return defLocale;
    }
    

    @Override
    public boolean isUseShadow() {
        return cd.isUseShadow();
    }

    @Override
    public void setUseShadow(boolean useShadow) {
        cd.setUseShadow(useShadow);
        notifyListtener();
    }

    @Override
    public String getClientId() {
        // 用于在本地单机多客户端测试,方便在桌面上测试时区别不同的客户端。
        if (Config.debug) {
            if (clientIdForDebug == null) {
                clientIdForDebug = UUID.randomUUID().toString();
            }
            return clientIdForDebug;
        }
        
        byte[] bytes = saveService.load(CLIENT_ID_KEY);
        // 首次获取不存在则自动生成一个唯一ID
        if (bytes == null) {
            bytes = UUID.randomUUID().toString().getBytes();
            saveService.save(CLIENT_ID_KEY, bytes);
        }
        return  new String(bytes);
    }

    @Override
    public void addConfigListener(ConfigListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public boolean removeConfigListener(ConfigListener listener) {
        return listeners.remove(listener);
    }
    
    private void notifyListtener() {
        if (listeners.isEmpty())
            return;
        for (ConfigListener cl : listeners) {
            cl.onConfigChanged();
        }
    }

    @Override
    public int getMaxLevel() {
        return cd.getMaxLevel();
    }

}
