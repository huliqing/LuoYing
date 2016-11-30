/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.Inject;

/**
 *
 * @author huliqing
 */
public interface ConfigService extends Inject {
    
//    /**
//     * 用于监听配置变更.
//     */
//    public interface ConfigListener {
//        
//        /**
//         * 当配置发生变化时调用该方法。
//         */
//        void onConfigChanged();
//        
//    }
    
//    /**
//     * 返回系统配置
//     * @return 
//     */
//    ConfigData getConfig();
    
//    /**
//     * 载入全局配置
//     */
//    void loadGlobalConfig();
    
//    /**
//     * 获取游戏名字
//     * @return 
//     */
//    String getGameName();
//    
//    /**
//     * 获取版本名称
//     * @return 
//     */
//    String getVersionName();
//    
//    /**
//     * 获取游戏版本号
//     * @return 
//     */
//    int getVersionCode();
//    
    /**
     * 获取游戏全局声音的开关状态
     * @return 
     */
    boolean isSoundEnabled();
    
    /**
     * 设置是否开关全局声音。
     * @param enabled 
     */
    void setSoundEnabled(boolean enabled);
    
    /**
     * 获取声效音量大小
     * @return 返回 [0.0, 1.0]， 1.0表示声音最大
     */
    float getSoundVolume();
    
    /**
     * 设置音量，取值[0.0, 1.0], 0表示声音最小，1.0表示声音最大,但是并不关闭声音
     * @param volume 
     */
    void setSoundVolume(float volume);
    
    // remove20161117
//    /**
//     * 载入语言环境，该方法返回的必须是一个当前游戏能够支持的语言设置。即
//     * 必须是Config.locale_all中的其中任何一个。
//     * @return 
//     */
//    String loadLocale();
//    
//    /**
//     * 设置语言环境，注意大小写必须一致，　如：en_US, zh_CN. <br/>
//     * 注意：locale必须是Config.locale_all中支持的语言环境中已经存在的，否则将导
//     * 致出错。
//     * @param locale 
//     */
//    void changeLocale(String locale);
    
    // remove20161117
//    /**
//     * 获取当前正在使用的语言环境,返回如：zh_CN,en_US
//     * @return 
//     */
//    String getLocale();
//    
//    float getDropFactor();
//    
//    int getPort();
//    
//    int getPortDiscoverServer();
//    
//    int getPortDiscoverClient();
    
//    /**
//     * 获取所有支持的语言环境，返回格式， “{en_US,zh_CN}”
//     * @return 
//     */
//    String[] getAllSupportedLocale();
    
//    /**
//     * 获取系统定义的最高级别限制
//     * @return 
//     */
//    int getMaxLevel();
    
//    /**
//     * 是否打开全局阴影选项
//     * @return 
//     */
//    boolean isUseShadow();
//    
//    /**
//     * 是否打开阴影功能
//     * @param useShadow 
//     */
//    void setUseShadow(boolean useShadow);
    
    /**
     * 获取客户端唯一ID，这个ID用来识别不同的客户端，每台安装了游戏的机器都会自动生成一个唯一的全局ID来识别这台机器。
     * 这个ID对于不同的客户端必须保证不相同。该ID允许清除，在重新获取的时候可以重新生成，但不保证多次生成的ID会相同<br >
     * 注：<br >
     * 1.但不保证同一台机器安装多个游戏的时候该ID仍然唯一。<br >
     * 2.不保证当游戏重装的时候该ID仍然与前一次安装时生成的ID相同。
     * @return 
     */
    String getClientId();
    
//    /**
//     * 添加配置侦听器
//     * @param listener 
//     */
//    void addConfigListener(ConfigListener listener);
    
//    /**
//     * 移除配置侦听器
//     * @param listener 
//     * @return  
//     */
//    boolean removeConfigListener(ConfigListener listener);
}
