/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.android;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import java.net.InetAddress;
import java.net.UnknownHostException;
import name.huliqing.fighter.Global;
import name.huliqing.fighter.game.service.EnvServiceImpl;

/**
 * Android下一些与环境相关的信息。
 * @author huliqing
 */
public class AndroidEnvServiceImpl extends EnvServiceImpl {

    @Override
    public String getMachineName() {
        return android.os.Build.MODEL;
    }
    
    @Override
    public InetAddress getLocalHostIPv4() {
        try {
            WifiManager wifiManager = (WifiManager) Global.getContext().getSystemService(Context.WIFI_SERVICE);  
            // 如果wifi未开启，则直接使用父类的方法
            if (!wifiManager.isWifiEnabled()) {  
                return super.getLocalHostIPv4();
            }  
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();       
            int ip = wifiInfo.getIpAddress();
            byte[] address = new byte[4];
            address[0] = (byte) (ip & 0xFF);
            address[1] = (byte) ((ip >> 8) & 0xFF);
            address[2] = (byte) ((ip >> 16) & 0xFF);
            address[3] = (byte) ((ip >> 24) & 0xFF);
            return InetAddress.getByAddress(address);
        } catch (UnknownHostException ex) {
            Log.e(AndroidEnvServiceImpl.class.getName(), "Could not getLocalHostIPv4!", ex);
        }
        return null;
    }
    
    @Override
    public InetAddress getBroadcastAddress() {
        try {
            WifiManager wifi = (WifiManager) Global.getContext().getSystemService(Context.WIFI_SERVICE);
            DhcpInfo dhcp = wifi.getDhcpInfo();
            int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
            byte[] quads = new byte[4];
            for (int k = 0; k < 4; k++) {
                quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
            }
            return InetAddress.getByAddress(quads);
        } catch (UnknownHostException ex) {
            Log.e(AndroidEnvServiceImpl.class.getName(), "Could not getBroadcastAddress!", ex);
        }
        return null;
    }
    
}
