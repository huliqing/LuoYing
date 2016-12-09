/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import java.util.UUID;
import name.huliqing.luoying.Config;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.utils.MathUtils;

/**
 *
 * @author huliqing
 */
public class ConfigServiceImpl implements ConfigService {
    private SaveService saveService;
    private SoundService soundService;
    
    // 用于标识游戏客户端唯一标识的ID的键。
    private final static String CLIENT_ID_KEY = "Client_ID";
    
    // 用于在本地多客户端测试,这个ID在每次启动时都重新生成，方便在桌面上测试时区别不同的客户端。
    private String clientIdForDebug;

    @Override
    public void inject() {
        saveService = Factory.get(SaveService.class);
        soundService = Factory.get(SoundService.class);
    }
    
    @Override
    public boolean isSoundEnabled() {
        return soundService.isSoundEnabled();
    }

    @Override
    public void setSoundEnabled(boolean enabled) {
        soundService.setSoundEnabled(enabled);
    }

    @Override
    public float getSoundVolume() {
        return soundService.getVolume();
    }

    @Override
    public void setSoundVolume(float volume) {
        soundService.setVolume(MathUtils.clamp(volume, 0f, 1.0f));
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

}
