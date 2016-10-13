/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;

/**
 * 角色行为逻辑数据
 * @author huliqing
 */
@Serializable
public class ConfigData extends ObjectData {
    
    /**
     * 游戏名称
     */
    private String gameName = "LuoYing"; 
    
    /**
     * 版本描述，保持与versionCode一致，如:1.5.1对应versionCode为151。
     */
    private String versionName = "1.6.1";
    
    /**
     * 版本号
     */
    private int versionCode = 161;
    
    /**
     * 默认的联网游戏端口
     */
    private int port = 32991;
    
    /**
     * Discover在服务端打开的端口，客户端可向服务端的这个udp端口广播消息来探测主机是否存在。
     */
    private int portDiscoverServer = 32992;
    
    /**
     * Discover在客户端打开的端口,服务端可向客户端的这个udp端口广播消息来通知所有可能存在的客户端。
     */
    private int portDiscoverClient = 32993;
    
    /**
     * 物品掉落率百分比,取值[0.0~1.0]， 注：1.0表示100%掉落物品。设为0可关闭掉
     * 落设置
     */
    private float dropFactor = 1.0f;// default 0.3f;
    
    /**
     * 奖励的经验倍率
     */
    private float expFactor = 1.0f;
   
    /**
     * 游戏允许的最高等级限制
     */
    private int maxLevel = 60;
    
    /**
     * 声音开关
     */
    private boolean soundEnabled = true;
    
    /**
     * 声音大小，0~1， 0为无声，1为最大声
     */
    private float soundVolume = 1.0f;
    
    /**
     * 当前使用的语言,e.g. zh_CN, en_US
     */
    private String locale = "";
    
    /**
     * 当前游戏能够支持的所有语言环境,使用半角逗号分隔。
     * 注意1：这里与data.font中文件夹中的名字是对应的,不能随便修改。
     * 注意2: 前后和中间都不要出现空格。
     * 注意3: 排在第一个的为默认的语言环境，当目标系统找不到任何合适的语言环境时
     * 就使该环境
     */
    private String localeAll = "en_US,zh_CN";
    
    /**
     * 是否打开全局投影功能,如果这个选项关闭，则场景中所有投影功能都要关闭。
     */
    private boolean useShadow;
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(gameName, "gameName", null);
        oc.write(versionName, "versionName", null);
        oc.write(versionCode, "versionCode", 0);
        oc.write(port, "port", port);
        oc.write(portDiscoverServer, "portDiscoverServer", portDiscoverServer);
        oc.write(portDiscoverClient, "portDiscoverClient", portDiscoverClient);
        oc.write(dropFactor, "dropFactor", dropFactor);
        oc.write(expFactor, "expFactor", expFactor);
        oc.write(maxLevel, "maxLevel", maxLevel);
        oc.write(soundEnabled, "soundEnabled", soundEnabled);
        oc.write(soundVolume, "soundVolume", soundVolume);
        oc.write(locale, "locale", "en_US");
        oc.write(localeAll, "localeAll", null);
        oc.write(useShadow, "useShadow", useShadow);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        // skip gameName,versionName,versionCode
        dropFactor = ic.readFloat("dropFactor", dropFactor);
        expFactor = ic.readFloat("expFactor", expFactor);
        maxLevel = ic.readInt("maxLevel", maxLevel);
        soundEnabled = ic.readBoolean("soundEnabled", soundEnabled);
        soundVolume = ic.readFloat("soundVolume", soundVolume);
        locale = ic.readString("locale", "en_US");
        localeAll = ic.readString("localeAll", null);
        useShadow = ic.readBoolean("useShadow", useShadow);
    }
    
    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
    
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPortDiscoverServer() {
        return portDiscoverServer;
    }

    public void setPortDiscoverServer(int portDiscoverServer) {
        this.portDiscoverServer = portDiscoverServer;
    }

    public int getPortDiscoverClient() {
        return portDiscoverClient;
    }

    public void setPortDiscoverClient(int portDiscoverClient) {
        this.portDiscoverClient = portDiscoverClient;
    }

    public float getDropFactor() {
        return dropFactor;
    }

    public void setDropFactor(float dropFactor) {
        this.dropFactor = dropFactor;
    }

    public float getExpFactor() {
        return expFactor;
    }

    public void setExpFactor(float expFactor) {
        this.expFactor = expFactor;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(float soundVolume) {
        this.soundVolume = soundVolume;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        if (locale != null)
            this.locale = locale;
    }

    public String getLocaleAll() {
        return localeAll;
    }

    public void setLocaleAll(String localeAll) {
        if (localeAll != null)
            this.localeAll = localeAll;
    }

    /**
     * 是否打开全局投影功能,如果这个选项关闭，则场景中所有投影功能都要关闭。
     * @return useShadow
     */
    public boolean isUseShadow() {
        return useShadow;
    }

    /**
     * 是否打开全局投影功能,如果这个选项关闭，则场景中所有投影功能都要关闭。
     * @param useShadow 
     */
    public void setUseShadow(boolean useShadow) {
        this.useShadow = useShadow;
    }

}
