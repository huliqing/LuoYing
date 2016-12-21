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

import com.jme3.system.JmeSystem;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author huliqing
 */
public class SystemServiceImpl implements SystemService {

    private static final Logger LOG = Logger.getLogger(SystemServiceImpl.class.getName());
    
    private String platformName;

    @Override
    public void inject() {
        // 
    }
    
    public static void main(String[] args) {
        System.out.println(new SystemServiceImpl().getMachineName());
    }
    
    @Override
    public String getLocale() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage() + "_" + locale.getCountry();
    }

    @Override
    public String getMachineName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            LOG.log(Level.INFO, "Could not get machine name!", e);
            return "Unknow";
        }
    }

    @Override
    public InetAddress getLocalHostIPv4() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                if (ni == null || ni.isLoopback() || !ni.isUp()) {
                    continue;
                }
                // 注意：ni.getInterfaceAddresses获得的列表中可能包含null对象
                List<InterfaceAddress> iAddresses = ni.getInterfaceAddresses();
                for (InterfaceAddress ia : iAddresses) {
                    if (ia == null) {
                        continue;
                    }
                    InetAddress address = ia.getAddress();
                    String host = address.getHostAddress();
                    if (host.contains(":")) {
                        continue;
                    }
                    return address;
                }
            }
        } catch (SocketException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public InetAddress getBroadcastAddress() {
        // 注: 正确的广播地址计算方法应该是由当前ip地址和子网掩码进行计算.
        // 1.当前做法：直接将当前IP的最后一段改为255在大部分情况也没有错。
        // 2.不要直接使用255.255.255.255,这在wifi网络下,其它客户端（如手机）可能无法接收到广播信息。
        InetAddress ipv4 = getLocalHostIPv4();
        if (ipv4 == null) {
            // 这可能在无网络的时候发生
            return null;
        }
        
        String host = ipv4.getHostAddress();
        int index = host.lastIndexOf(".");
        String broadcast = host.substring(0, index + 1) + "255";
        try {
            return InetAddress.getByName(broadcast);
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getPlatformName() {
        if (platformName == null) {
            platformName = JmeSystem.getPlatform().name();
        }
        return platformName;
    }
   
}
