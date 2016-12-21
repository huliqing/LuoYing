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
package name.huliqing.luoying.layer.service;

import java.io.File;
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
    
    // ---- 存档设置
    // 默认的存档目录
    private String saveDir = System.getProperty("user.home") + File.separator + "LuoYing";
    
    // 日志设置
    private String logDir = new File("log").getAbsolutePath();
    
    @Override
    public void inject() {
        saveService = Factory.get(SaveService.class);
        soundService = Factory.get(SoundService.class);
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
    public String getSaveDir() {
        return saveDir;
    }
    
    @Override
    public void setSaveDir(String saveDir) {
        this.saveDir = saveDir;
    }

    @Override
    public String getLogDir() {
        return logDir;
    }

    @Override
    public void setLogDir(String logDir) {
        this.logDir = logDir;
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


}
