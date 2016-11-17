/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.loader;

import name.huliqing.luoying.data.ConfigData;
import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.xml.DataLoader;

/**
 *
 * @author huliqing
 */
public class ConfigDataLoader implements DataLoader<ConfigData> {

    @Override
    public void load(Proto proto, ConfigData data) {
        data.setGameName(proto.getAsString("gameName"));
        data.setVersionName(proto.getAsString("versionName"));
        data.setVersionCode(proto.getAsInteger("versionCode", -1));
        data.setPort(proto.getAsInteger("port", 32991));
        data.setPortDiscoverServer(proto.getAsInteger("portDiscoverServer", 32992));
        data.setPortDiscoverClient(proto.getAsInteger("portDiscoverClient", 32993));
        data.setDropFactor(proto.getAsFloat("dropFactor", 1.0f));
        data.setExpFactor(proto.getAsFloat("expFactor", 1.0f));
        data.setMaxLevel(proto.getAsInteger("maxLevel", 60));
        data.setSoundEnabled(proto.getAsBoolean("soundEnabled", false));
        data.setSoundVolume(proto.getAsFloat("soundVolume", 1.0f));
//        data.setLocale(proto.getAsString("locale"));
//        data.setLocaleAll(proto.getAsString("localeAll"));
        data.setUseShadow(proto.getAsBoolean("useShadow", false));
        
//        data.setShortcutLocked(proto.getAsBoolean("shortcutLocked", false));
//        data.setShortcutSize(proto.getAsFloat("shortcutSize", 1.0f));
//        data.setSpeakTimeMin(proto.getAsFloat("speakTimeMin", 0.25f));
//        data.setSpeakTimeMax(proto.getAsFloat("speakTimeMax", 8.0f));
//        data.setSpeakTimeWorld(proto.getAsFloat("speakTimeWorld", 0.22f));
//        data.setSpeakMaxDistance(proto.getAsFloat("speakMaxDistance", 80f));
//        data.setUseHardwareSkinning(proto.getAsBoolean("useHardwareSkinning", false));
//        data.setSummonLevelFactor(proto.getAsFloat("summonLevelFactor", 1.0f));
//        data.setLanGames(proto.getAsString("lanGames"));
    }
    
}
