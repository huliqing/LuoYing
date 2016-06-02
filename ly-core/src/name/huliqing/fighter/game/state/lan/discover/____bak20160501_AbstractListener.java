///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.game.state.lan.discover;
//
//import java.net.DatagramPacket;
//import java.nio.ByteBuffer;
//
///**
// *
// * @author huliqing
// */
//public abstract class AbstractListener implements Listener {
//    
//    // remove20160501
////    @Override
////    public void receive(String code, String content, UDPDiscover discover, DatagramPacket packet) throws Exception {
////        AbstractMess returnMessage = receive(code, content);
////        if (returnMessage != null) {
////            discover.send(returnMessage, packet.getAddress().getHostAddress(), packet.getPort());
////        }
////    }
//    
////    /**
////     * 处理接收到的消息，如果需要回复，则返回不为null的消息。
////     * 注意：该方法是在非render线程中运行的。
////     * @param code 接收到的消息代码
////     * @param content 接收到的消息内容
////     * @return 如果返回null,则表示不对接收到的消息进行回复(注意：只有返回null
////     * 才不进行回复，返回""（空字符串）还是会进行回复的。)
////     */
////    public abstract AbstractMess receive(String code, String content) throws Exception;
//    
//    
//}
