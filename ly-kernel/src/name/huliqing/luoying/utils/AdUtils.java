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
package name.huliqing.luoying.utils;

/**
 * 用于控制广告的显示或隐藏
 * @author huliqing
 */
public class AdUtils {
    
    /** 
     * 开始显示开启广告的时间,确保经过一段时间后关闭该广告,
     * 每次显示开屏广告的时候
     */ 
    public static long begin_show_openAD_time;
    
    private static AdController adController;
    
    /**
     * 注册一个广告控制器
     * @param adController 
     */
    public static void setAdController(AdController adController) {
        AdUtils.adController = adController;
    }
    
    /**
     * 显示广告
     * @param type
     */
    public static void showAd(AdType type) {
        if (adController != null) {
            adController.showAd(type);
        }
    }
    
    /**
     * 隐藏广告
     * @param types
     */
    public static void hideAd(AdType... types) {
        if (adController != null) {
            adController.hideAd(types);
        }
    }
}
