/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.config;

import name.huliqing.fighter.data.ConfigData;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.object.DataLoader;

/**
 *
 * @author huliqing
 */
public class ConfigDataLoader implements DataLoader<ConfigData> {

    @Override
    public void load(Proto proto, ConfigData data) {
        data.setDebug(proto.getAsBoolean("debug", false));
        data.setGameName(proto.getAttribute("gameName"));
        data.setVersionName(proto.getAttribute("versionName"));
        data.setVersionCode(proto.getAsInteger("versionCode"));
        data.setPort(proto.getAsInteger("port"));
        
        // remove20160501，后续合并为一个端口
        data.setPortDiscoverServer(proto.getAsInteger("portDiscoverServer"));
        data.setPortDiscoverClient(proto.getAsInteger("portDiscoverClient"));
        data.setDropFactor(proto.getAsFloat("dropFactor"));
        data.setExpFactor(proto.getAsFloat("expFactor"));
        data.setMaxLevel(proto.getAsInteger("maxLevel"));
        data.setSoundEnabled(proto.getAsBoolean("soundEnabled", false));
        data.setSoundVolume(proto.getAsFloat("soundVolume"));
        data.setShortcutLocked(proto.getAsBoolean("shortcutLocked", false));
        data.setShortcutSize(proto.getAsFloat("shortcutSize"));
        data.setBaseWalkSpeed(proto.getAsFloat("baseWalkSpeed"));
        data.setBaseRunSpeed(proto.getAsFloat("baseRunSpeed"));
        data.setLocale(proto.getAttribute("locale"));
        data.setLocaleAll(proto.getAttribute("localeAll"));
        data.setSpeakTimeMin(proto.getAsFloat("speakTimeMin"));
        data.setSpeakTimeMax(proto.getAsFloat("speakTimeMax"));
        data.setSpeakTimeWorld(proto.getAsFloat("speakTimeWorld"));
        data.setSpeakMaxDistance(proto.getAsFloat("speakMaxDistance"));
        data.setUseHardwareSkinning(proto.getAsBoolean("useHardwareSkinning", false));
        data.setSummonLevelFactor(proto.getAsFloat("summonLevelFactor"));
        data.setLanGames(proto.getAttribute("lanGames"));
        data.setUseShadow(proto.getAsBoolean("useShadow", false));
    }
    
}
