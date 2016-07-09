/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

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
import name.huliqing.fighter.Config;

/**
 *
 * @author huliqing
 */
public class SystemServiceImpl implements SystemService {
    
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
            Logger.getLogger(SystemServiceImpl.class.getName()).log(Level.INFO, "Could not get machine name!", e);
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
            Logger.getLogger(SystemServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public InetAddress getBroadcastAddress() {
        // TODO: 正确的广播地址计算方法应该是由当前ip地址和子网掩码进行计算.
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
        if (Config.debug) {
            Logger.getLogger(SystemServiceImpl.class.getName()).log(Level.INFO, "broadcast address={0}", broadcast);
        }
        try {
            return InetAddress.getByName(broadcast);
        } catch (UnknownHostException ex) {
            Logger.getLogger(SystemServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
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
