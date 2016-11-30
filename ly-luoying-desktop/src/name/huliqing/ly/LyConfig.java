/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly;

import name.huliqing.luoying.data.ConfigData;

/**
 *
 * @author huliqing
 */
public class LyConfig {
    
    private static ConfigData cd;
    
    public static void setConfigData(ConfigData configData) {
        cd = configData;
    }
    
    public static ConfigData getConfigData() {
        return cd;
    }
    
    public static String getConfig(String key) {
        return cd.getAsString(key);
    }
    
    public static boolean isDebug() {
        return cd.getAsBoolean("debug", false);
    }
    
    public static String getGameName() {
        return cd.getAsString("gameName", "落樱之剑");
    }
    
    public static String getVersionName() {
        return cd.getAsString("versionName", "unknow");
    }
    
    public static int getVersionCode() {
        return cd.getAsInteger("versionCode", -1);
    }
    
    public static int getServerPort() {
        return cd.getAsInteger("serverPort", 32990);
    }
    
    public static int getDiscoverServerPort() {
        return cd.getAsInteger("discoverServerPort", 32992);
    }
    
    public static int getDiscoverClientPort() {
        return cd.getAsInteger("discoverClientPort", 32993);
    }
    
    public static float getShortcutSize() {
        return cd.getAsFloat("shortcutSize", 1.0f);
    }
    
    public static void setShortcutSize(float shortcutSize) {
        cd.setAttribute("shortcutSize", shortcutSize);
    }
    
    public static boolean isShortcutLocked() {
        return cd.getAsBoolean("shortcutLocked", false);
    }

    public static void setShortcutLocked(boolean shortcutLocked) {
        cd.setAttribute("shortcutLocked", shortcutLocked);
    }

    public static float getSpeakTimeWorld() {
        return cd.getAsFloat("speakTimeWorld", 0.22f);
    }

    public static void setSpeakTimeWorld(float speakTimeWorld) {
        cd.setAttribute("speakTimeWorld", speakTimeWorld);
    }

    public static float getSpeakTimeMin() {
        return cd.getAsFloat("speakTimeMin", 0.25f);
    }

    public static void setSpeakTimeMin(float speakTimeMin) {
        cd.setAttribute("speakTimeMin", speakTimeMin);
    }

    public static float getSpeakTimeMax() {
        return cd.getAsFloat("speakTimeMax", 8.0f);
    }

    public static void setSpeakTimeMax(float speakTimeMax) {
        cd.setAttribute("speakTimeMax", speakTimeMax);
    }

    public static float getSpeakMaxDistance() {
        return cd.getAsFloat("speakMaxDistance", 80f);
    }

    public static void setSpeakMaxDistance(float speakMaxDistance) {
        cd.setAttribute("speakMaxDistance", speakMaxDistance);
    }
    
    public static String[] getLanGames() {
        return cd.getAsArray("lanGames");
    }

}
