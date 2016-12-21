/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.ly;

import java.util.logging.Level;
import name.huliqing.luoying.Config;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ConfigData;
import name.huliqing.luoying.layer.service.ConfigService;
import name.huliqing.luoying.layer.service.SystemService;
import name.huliqing.luoying.log.LogFactory;
import name.huliqing.ly.view.shortcut.ShortcutManager;

/**
 *
 * @author huliqing
 */
public class LyConfig {
    
    private static ConfigData cd;
    
    public static void setConfigData(ConfigData configData) {
        cd = configData;
        setSoundEnabled(isSoundEnabled());
        setSoundVolume(getSoundVolume());
        setDebug(isDebug());
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
    
    public static void setDebug(boolean debug) {
//        cd.setAttribute("debug", debug); // DEBUG不要存档。
        Config.debug = debug;
        if (Config.debug) {
            LogFactory.resetLogger(Level.INFO, true);
        } else {
            LogFactory.resetLogger(Level.WARNING, false);
        }
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
        ShortcutManager.setShortcutSize(shortcutSize);
    }
    
    public static boolean isShortcutLocked() {
        return cd.getAsBoolean("shortcutLocked", false);
    }

    public static void setShortcutLocked(boolean shortcutLocked) {
        cd.setAttribute("shortcutLocked", shortcutLocked);
        ShortcutManager.setShortcutLocked(shortcutLocked);
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

    public static String getLocale() {
        String locale = cd.getAsString("locale"); // from saved
        if (locale == null) {
            locale = Factory.get(SystemService.class).getLocale(); // Auto detect from system.
        }
        return locale;
    }
    
    public static void setLocale(String locale) {
        cd.setAttribute("locale", locale);
    }
    
    public static String[] getLocaleSupported() {
        return cd.getAsArray("localeSupported");
    }
    
    /**
     * 获取游戏全局声音的开关状态
     * @return 
     */
    public static boolean isSoundEnabled() {
        return cd.getAsBoolean("soundEnabled", true);
    }
    
    /**
     * 设置是否开关全局声音。
     * @param soundEnabled 
     */
    public static void setSoundEnabled(boolean soundEnabled) {
        cd.setAttribute("soundEnabled", soundEnabled);
        Factory.get(ConfigService.class).setSoundEnabled(soundEnabled);
    }
    
    /**
     * 获取声效音量大小
     * @return 返回 [0.0, 1.0]， 1.0表示声音最大
     */
    public static float getSoundVolume() {
        return cd.getAsFloat("soundVolume", 1.0f);
    }
    
    /**
     * 设置音量，取值[0.0, 1.0], 0表示声音最小，1.0表示声音最大,但是并不关闭声音 
     * @param soundVolume
     */
    public static void setSoundVolume(float soundVolume) {
        cd.setAttribute("soundVolume", soundVolume);
        Factory.get(ConfigService.class).setSoundVolume(soundVolume);
    }
    
}
