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
package name.huliqing.luoying.ui;

import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;

/**
 *
 * @author huliqing
 */
public class UIFactory {
    
    private static UIConfig uiConfig;
    
    /**
     * 获取默认的UI配置
     * @return 
     */
    public static UIConfig getUIConfig() {
        if (uiConfig == null) {
            AssetManager assetManager = new DesktopAssetManager(
                    Thread.currentThread().getContextClassLoader().getResource("com/jme3/asset/Desktop.cfg"));
            uiConfig = new UIConfigImpl(assetManager);
        }
        return uiConfig;
    }
    
    /**
     * 替换默认的UI配置
     * @param uiConfig 
     */
    public static void registerUIConfig(UIConfig uiConfig) {
        UIFactory.uiConfig = uiConfig;
    }
}
