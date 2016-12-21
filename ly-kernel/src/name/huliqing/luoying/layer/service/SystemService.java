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

import java.net.InetAddress;
import name.huliqing.luoying.Inject;

/**
 * 该类主要用于定义一些与系统平台相关的信息
 * @author huliqing
 */
public interface SystemService extends Inject {
    
    /**
     * 获取本地语言环境，返回如：zh_CN, en_US等
     * @return 
     */
    String getLocale();
    
    /**
     * 获取机器名称,如PC电脑名称，手机名称、标识之类。这个名称标识并不需要
     * 是唯一的。
     * @return 
     */
    String getMachineName();
    
    /**
     * 获取当前机器IPv4地址，如：192.168.1.8, 注：在没有网络的情况下该方法
     * 可能会返回null，调用该方法时需要注意null的情况。
     * @return 
     */
    InetAddress getLocalHostIPv4();
    
    /**
     * 获取UDP广播地址,如果存在错误则返回null.
     * @return 
     */
    InetAddress getBroadcastAddress();
    
    /**
     * 获取系统平台名称
     * @see com.jme3.system.Platform
     * @return 
     */
    String getPlatformName();
}
