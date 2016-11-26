/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.network.discover;

import java.net.DatagramPacket;

/**
 * UDP消息接收侦听器
 * @author huliqing
 */
public interface UDPListener {
  
    // remove,20160501,不再使用这种方式
//    /**
//     * 处理接收到的消息
//     * @param code 接收到的编码
//     * @param content 接收到的内容
//     * @param discover 本地UDP监听器
//     * @param packet 接收到的消息包
//     */
//    void receive(String code, String content, UDPDiscover discover, DatagramPacket packet) throws Exception;
    
    /**
     * 接收到的Object信息
     * @param object
     * @param discover
     * @param packet 
     * @throws java.lang.Exception 
     */
    void receive(Object object, UDPDiscover discover, DatagramPacket packet) throws Exception;
}
