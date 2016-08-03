/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import name.huliqing.core.xml.ProtoData;
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
public class ConfigData extends ProtoData {
    
    /**
     * 是否打开调试
     */
    private boolean debug;
    
    /**
     * 游戏名称
     */
    private String gameName = "落樱3D"; 
    
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
    private float dropFactor = 0.3f;// default 0.3f;
    
    /**
     * 奖励的经验倍率
     */
    private float expFactor = 1.0f;
    
    /**
     * 是否使用全局光源,不使用全局光源可提高运行性能.
     */
    private boolean useLight = false;
    
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
    private float soundVolume = 0.5f;
    
    /**
     * 是否锁定快捷方式
     */
    private boolean shortcutLocked = false;
    
    /**
     * shortcut的缩放倍率,1为原始大小，大于1则放大,小于1则缩小。
     * 注意不能等于0.
     */
    private float shortcutSize = 1.0f;
    
    /**
     * 默认的基本"步行"速度:1.5
     */
    private float baseWalkSpeed = 1.5f;
    
    /**
     * 默认的基本"跑步"速度:6
     */
    private float baseRunSpeed = 6;
    
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
     * 关于对话速度的限制，限制说话的最少停留时间
     */
    private float speakTimeMin = 0.1f;
    
    /**
     * 限制说话的最长停留时间
     */
    private float speakTimeMax = 8.0f;
    
    /**
     * 设置每个单位字词的停留时间(秒)。如3个中文单位则speak的停留时间为
     * time = speakTimeWorld * 3; 增加这个值会延长每个speak谈话的停留时间。但
     * speak的最终停留时间会限制在speakTimeMin和speakTimeMax的范围之内。
     * 注：中文以每一个字作为一个单位，英文以每个单词作为一个单位。
     * 这个值可根据普通人的阅读速度来设置，假如普通阅读速度为每分钟300个字。
     * 则这个时间 speakTimeWorld = 60 / 300
     */
    private float speakTimeWorld = 0.25f;
    
    /**
     * 允许显示谈话内容的最远距离。
     */
    private float speakMaxDistance = 60;
    
    /**
     * 默认情况下暂不要打开该选项，这在某些老机型上会存在权重和贴图错误。
     * 对于一些新机型的用户可以自己决定是否打开。
     */
    private boolean useHardwareSkinning = false;
    
    /**
     * 召唤的等级限制因数，该参数是一具相对于召唤者的设定。 
     * 召唤的角色等级这样计算： summonLevel = actor.level * SUMMON_LEVEL_FACTOR;
     * 比如：0.5表示召唤出的角色的等级相当于召唤者等级的一半。当然summonLevel会
     * 强制转换为整数
     */
    private float summonLevelFactor = 0.75f;
    
    /**
     * 局域网游戏列表，格式"game1,game2,game3,..."
     */
    private String lanGames;
    
    /**
     * 是否打开全局投影功能,如果这个选项关闭，则场景中所有投影功能都要关闭。
     */
    private boolean useShadow;
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(debug, "debug", false);
//        oc.write(gameName, "gameName", null);// 不保存
//        oc.write(versionName, "versionName", null);
//        oc.write(versionCode, "versionCode", 0);
//        oc.write(port, "port", 32991);
//        oc.write(portDiscoverServer, "portDiscoverServer", 32992);
//        oc.write(portDiscoverClient, "portDiscoverClient", 32993);
        oc.write(dropFactor, "dropFactor", 0.3f);
        oc.write(expFactor, "expFactor", 1.0f);
        oc.write(useLight, "useLight", false);
        oc.write(maxLevel, "maxLevel", 60);
        oc.write(soundEnabled, "soundEnabled", true);
        oc.write(soundVolume, "soundVolume", 0.5f);
        oc.write(shortcutLocked, "shortcutLocked", false);
        oc.write(shortcutSize, "shortcutSize", 1.0f);
        oc.write(baseWalkSpeed, "baseWalkSpeed", 1.5f);
        oc.write(baseRunSpeed, "baseRunSpeed", 6f);
        oc.write(locale, "locale", "en_US");
        oc.write(localeAll, "localeAll", "en_US,zh_CN");
        oc.write(speakTimeMin, "speakTimeMin", 0.1f);
        oc.write(speakTimeMax, "speakTimeMax", 8.0f);
        oc.write(speakTimeWorld, "speakTimeWorld", 0.25f);
        oc.write(speakMaxDistance, "speakMaxDistance", 60);
        oc.write(useHardwareSkinning, "useHardwareSkinning", false);
        oc.write(summonLevelFactor, "summonLevelFactor", 0.75f);
        oc.write(lanGames, "lanGames", null);
        oc.write(useShadow, "useShadow", false);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        debug = ic.readBoolean("debug", false);
//        gameName = ic.readString("gameName", "落樱3D"); 
//        versionName = ic.readString("versionName", versionName);
//        versionCode = ic.readInt("versionCode", -1);
//        port = ic.readInt("port", 32991);
//        portDiscoverServer = ic.readInt("portDiscoverServer", 32992);
//        portDiscoverClient = ic.readInt("portDiscoverClient", 32993);
        dropFactor = ic.readFloat("dropFactor", 0.3f);
        expFactor = ic.readFloat("expFactor", 1.0f);
        useLight = ic.readBoolean("useLight", false);
        maxLevel = ic.readInt("maxLevel", 60);
        soundEnabled = ic.readBoolean("soundEnabled", true);
        soundVolume = ic.readFloat("soundVolume", 0.5f);
        shortcutLocked = ic.readBoolean("shortcutLocked", false);
        shortcutSize = ic.readFloat("shortcutSize", 1.0f);
        baseWalkSpeed = ic.readFloat("baseWalkSpeed", 1.5f);
        baseRunSpeed = ic.readFloat("baseRunSpeed", 6f);
        locale = ic.readString("locale", "en_US");
        localeAll = ic.readString("localeAll", "en_US,zh_CN");
        speakTimeMin = ic.readFloat("speakTimeMin", 0.1f);
        speakTimeMax = ic.readFloat("speakTimeMax", 8.0f);
        speakTimeWorld = ic.readFloat("speakTimeWorld", 0.25f);
        speakMaxDistance = ic.readFloat("speakMaxDistance", 60f);
        useHardwareSkinning = ic.readBoolean("useHardwareSkinning", false);
        summonLevelFactor = ic.readFloat("summonLevelFactor", 0.75f);
        lanGames = ic.readString("lanGames", null);
        useShadow = ic.readBoolean("useShadow", false);
    }
    
    public ConfigData(){}
    
    public ConfigData(String id) {
        super(id);
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
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

    public boolean isUseLight() {
        return useLight;
    }

    public void setUseLight(boolean useLight) {
        this.useLight = useLight;
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

    public boolean isShortcutLocked() {
        return shortcutLocked;
    }

    public void setShortcutLocked(boolean shortcutLocked) {
        this.shortcutLocked = shortcutLocked;
    }

    public float getShortcutSize() {
        return shortcutSize;
    }

    public void setShortcutSize(float shortcutSize) {
        this.shortcutSize = shortcutSize;
    }

    public float getBaseWalkSpeed() {
        return baseWalkSpeed;
    }

    public void setBaseWalkSpeed(float baseWalkSpeed) {
        this.baseWalkSpeed = baseWalkSpeed;
    }

    public float getBaseRunSpeed() {
        return baseRunSpeed;
    }

    public void setBaseRunSpeed(float baseRunSpeed) {
        this.baseRunSpeed = baseRunSpeed;
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

    public float getSpeakTimeMin() {
        return speakTimeMin;
    }

    public void setSpeakTimeMin(float speakTimeMin) {
        this.speakTimeMin = speakTimeMin;
    }

    public float getSpeakTimeMax() {
        return speakTimeMax;
    }

    public void setSpeakTimeMax(float speakTimeMax) {
        this.speakTimeMax = speakTimeMax;
    }

    public float getSpeakTimeWorld() {
        return speakTimeWorld;
    }

    public void setSpeakTimeWorld(float speakTimeWorld) {
        this.speakTimeWorld = speakTimeWorld;
    }

    public float getSpeakMaxDistance() {
        return speakMaxDistance;
    }

    public void setSpeakMaxDistance(float speakMaxDistance) {
        this.speakMaxDistance = speakMaxDistance;
    }

    public boolean isUseHardwareSkinning() {
        return useHardwareSkinning;
    }

    public void setUseHardwareSkinning(boolean useHardwareSkinning) {
        this.useHardwareSkinning = useHardwareSkinning;
    }

    public float getSummonLevelFactor() {
        return summonLevelFactor;
    }

    public void setSummonLevelFactor(float summonLevelFactor) {
        this.summonLevelFactor = summonLevelFactor;
    }

    public String getLanGames() {
        return lanGames;
    }

    public void setLanGames(String lanGames) {
        this.lanGames = lanGames;
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
