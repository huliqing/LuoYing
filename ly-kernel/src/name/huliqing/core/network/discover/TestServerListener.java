/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.network.discover;

import java.net.DatagramPacket;

/**
 *
 * @author huliqing
 */
public class TestServerListener implements UDPListener {

    @Override
    public void receive(Object object, UDPDiscover discover, DatagramPacket packet) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
 
//    @Override
//    public AbstractMess receive(String code, String content) {
////        System.out.println("服务端收到数据包：" + new String(packet.getData()));
//        int c = Integer.parseInt(code);
//        if (c == Code.CODE_CS_FIND_SERVER) {
//            // do something
//            System.out.println("从客户端收到消息：code=" + c + ",content=" + content);
//        }
//        return null;
//    }
}
