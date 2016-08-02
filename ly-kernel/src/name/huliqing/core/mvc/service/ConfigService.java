/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import name.huliqing.core.Inject;
import name.huliqing.core.data.ConfigData;

/**
 *
 * @author huliqing
 */
public interface ConfigService extends Inject {
    
    /**
     * 用于监听配置变更.
     */
    public interface ConfigListener {
        
        /**
         * 当配置发生变化时调用该方法。
         */
        void onConfigChanged();
        
    }
    
    /**
     * 返回系统配置
     * @return 
     */
    ConfigData getConfig();
    
    /**
     * 载入全局配置
     */
    void loadGlobalConfig();
    
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
    
    /**
     * 快捷方式是否已经锁定
     * @return 
     */
    boolean isShortcutLocked();
    
    /**
     * 设置快捷方式锁定状态
     * @param locked
     */
    void setShortcutLocked(boolean locked);
    
    /**
     * 获取快捷方式缩放倍率
     * @return 
     */
    float getShortcutSize();
    
    /**
     * 设置快捷方式大小
     * @param size
     */
    void setShortcutSize(float size);
    
    /**
     * 清理界面所有快捷方式
     */
    void clearShortcuts();
    
    /**
     * 判断debug是否处于打开状态
     * @return 
     */
    boolean isDebugEnabled();
    
    /**
     * 显示或关闭调试信息
     */
    void setDebug(boolean enabled);
    
    /**
     * 获得speak的每个字词的停留时间单位长度。该值越大，talk过程中文字的停
     * 留时间越长,单位秒
     * @return 
     */
    float getSpeakTimeWorld();
    
    /**
     * 设置speak的每个字词的停留时间单位长度。该值越大，talk过程中文字的停
     * 留时间越长,单位秒
     * @param time 
     */
    void setSpeakTimeWorld(float time);
    
    float getSpeakTimeMin();
    float getSpeakTimeMax();
    
    float getSpeakMaxDistance();
    
    /**
     * 判断系统是否打开了全局hardwareSkinning.
     * 注：这是全局设定,如果关闭这个功能则全游戏的hardwareSkinning都应该关闭。
     * 但是打开这个功能并不意味着所有物体都会打开hardwareSkinning.部分物体可能有
     * 自身的设定。
     * @return 
     */
    boolean isUseHardwareSkinning();
    
    /**
     * 设置是否打开hardwareSkinning加速，该功能可以大量减轻CPU负担，对提高FPS
     * 有比较大的帮助.注：这是全局设定,如果关闭这个功能则全游戏的hardwareSkinning都
     * 应该关闭。但是打开这个功能并不意味着所有物体都会打开hardwareSkinning.
     * 部分物体可能有自身的设定。
     * @param enabled 
     */
    void setUseHardwareSkining(boolean enabled);
    
    /**
     * 载入语言环境，该方法返回的必须是一个当前游戏能够支持的语言设置。即
     * 必须是Config.locale_all中的其中任何一个。
     * @return 
     */
    String loadLocale();
    
    /**
     * 设置语言环境，注意大小写必须一致，　如：en_US, zh_CN. <br/>
     * 注意：locale必须是Config.locale_all中支持的语言环境中已经存在的，否则将导
     * 致出错。
     * @param locale 
     */
    void changeLocale(String locale);
    
    /**
     * 获取当前正在使用的语言环境,返回如：zh_CN,en_US
     * @return 
     */
    String getLocale();
    
    float getDropFactor();
    String[] getLanGames();
    String getGameName();
    String getVersionName();
    int getVersionCode();
    int getPort();
    int getPortDiscoverServer();
    int getPortDiscoverClient();
    float getSummonLevelFactor();
    
    float getBaseWalkSpeed();
    float getBaseRunSpeed();
    
    /**
     * 获取所有支持的语言环境，返回格式， “{en_US,zh_CN}”
     * @return 
     */
    String[] getAllSupportedLocale();
    
    /**
     * 是否打开光源
     * @return 
     */
    boolean isUseLight();
    
    /**
     * 获取系统定义的最高级别限制
     * @return 
     */
    int getMaxLevel();
    
    /**
     * 是否打开全局阴影选项
     * @return 
     */
    boolean isUseShadow();
    
    /**
     * 是否打开阴影功能
     * @param useShadow 
     */
    void setUseShadow(boolean useShadow);
    
    /**
     * 获取客户端唯一ID，这个ID用来识别不同的客户端，每台安装了游戏的机器都会自动生成一个唯一的全局ID来识别这台机器。
     * 这个ID对于不同的客户端必须保证不相同。该ID允许清除，在重新获取的时候可以重新生成，但不保证多次生成的ID会相同<br >
     * 注：<br >
     * 1.但不保证同一台机器安装多个游戏的时候该ID仍然唯一。<br >
     * 2.不保证当游戏重装的时候该ID仍然与前一次安装时生成的ID相同。
     * @return 
     */
    String getClientId();
    
    /**
     * 添加配置侦听器
     * @param listener 
     */
    void addConfigListener(ConfigListener listener);
    
    /**
     * 移除配置侦听器
     * @param listener 
     * @return  
     */
    boolean removeConfigListener(ConfigListener listener);
}
