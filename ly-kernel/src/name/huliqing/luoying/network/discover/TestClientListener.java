/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.network.discover;

import java.net.DatagramPacket;

/**
 *
 * @author huliqing
 */
public class TestClientListener implements UDPListener {

//    @Override
//    public AbstractMess receive(String code, String content) {
////        System.out.println("客户端收到数据包：" + new String(packet.getData()));
//        int c = Integer.parseInt(code);
//        if (c == Code.CODE_SC_STARTED) {
//            // do something
//            System.out.println("从服务端收到消息：code=" + c + ",content=" + content);
//        }
//        return null;
//    }

    @Override
    public void receive(Object object, UDPDiscover discover, DatagramPacket packet) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
