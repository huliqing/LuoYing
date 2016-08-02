/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.net.InetAddress;
import name.huliqing.core.Inject;

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
