///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.game.state.lan.discover;
//
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//import java.net.SocketException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.game.service.EnvService;
//
///**
// * 用于查找主机，或者通知客户端。这个类可以在客户端运行也可以在服务端运行。
// * 当在服务端运行时可以作为服务端监听来自客户端的消息。
// * 当作为客户端运行时可以向服务端发送查询消息。
// * @author huliqing
// */
//public class Discover {
//    private final static Logger logger = Logger.getLogger(Discover.class.getName());
//    
//    // 本地监听端口
//    private int localListenPort;
//    private DatagramSocket socket;
////    private byte[] buffer = new byte[65535];
//    private byte[] buffer = new byte[10240]; // 可尽量比需要的大一些
//    private Listener listener; // 接收到消息时的侦听器
//    private boolean started;
//    // 消息接收线程
//    private Receive receive;
//    // 默认的分析器
//    private PacketParse packetParse = new PacketParse();
//    
//    public Discover(int localListenPort) {
//        this.localListenPort = localListenPort;
//    }
//    
//    public void start() {
//        if (started) {
//            return;
//        }
//        try {
//            socket = new DatagramSocket(new InetSocketAddress(localListenPort));
//            socket.setBroadcast(true);
//            receive = new Receive();
//            receive.start();
//            started = true;
//        } catch (SocketException ex) {
//            started = false;
//            Logger.getLogger(Discover.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    
//    public void close() {
//        if (socket != null && !socket.isClosed()) {
//            socket.close();
//        }
//        started = false;
//    }
//    
//    /**
//     * 向目标机器发送UDP信息,调用该方法前需要确保已经启动（start）
//     * @param mess 要向目标机器发送的消息
//     * @param ipAddress 目标机器的IP地址，如：192.168.1.8
//     * @param port 目标机器的端口，如：32999
//     */
//    public void send(AbstractMess mess, String ipAddress, int port) {
//        if (!isRunning()) {
//            return;
//        }
//        try {
//            byte[] data = packetParse.encode(mess.getCode() + "", mess.encodeContent()); // 给信息加上包头信息
//            DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(ipAddress), port);
//            socket.send(packet);
//        } catch (Exception ex) {
//            Logger.getLogger(Discover.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    
//    /**
//     * 向局域网广播消息,调用该方法前需要确保已经启动（start）
//     * @param message 要向本地网络广播的消息,如果为null则什么也不做
//     * @param boradcastPort 指定要向本地网络中目标机器上哪一个端口进行广播
//     */
//    public void broadcast(AbstractMess mess, int broadcastPort) {
//        if (!isRunning() || mess == null) {
//            return;
//        }
//        try {
//            byte[] data = packetParse.encode(mess.getCode() + "", mess.encodeContent()); // 给信息加上包头信息
//            logger.log(Level.INFO, "Broadcast message:{0}", new String(data));
//            DatagramPacket packet = new DatagramPacket(data, data.length, getBroadcastAddress(), broadcastPort);
//            socket.send(packet);
//        } catch (Exception ex) {
//            Logger.getLogger(Discover.class.getName()).log(Level.SEVERE, "Could not broadcast message!", ex);
//        }
//    }
//    
//    /**
//     * 获取当前系统的UDP广播地址
//     * @return 
//     */
//    protected InetAddress getBroadcastAddress() {
//        return Factory.get(EnvService.class).getBroadcastAddress();
//    }
//    
//    private boolean isRunning() {
//        if (!started) {
//            logger.log(Level.WARNING, "Discover is not started!");
//            return false;
//        }
//        return true;
//    }
//    
//    /**
//     * 设置一个侦听器，用于处理接收到的消息。
//     * @param listener 
//     */
//    public void setListener(Listener listener) {
//        this.listener = listener;
//    }
//    
//    // 消息监听
//    private class Receive extends Thread {
//        
//        @Override
//        public void run() {
//            while (started) {
//                try {
//                    DatagramPacket inPacket = new DatagramPacket(buffer, 0, buffer.length);
//                    socket.receive(inPacket);
//                    String[] tempData = packetParse.decode(inPacket.getData());
//                    if (tempData != null && listener != null) {
//                        listener.receive(tempData[0], tempData[1], Discover.this, inPacket);
//                    }
//                } catch (SocketException e) {
//                    // ignore
//                } catch (Exception e) {
//                    Logger.getLogger(Discover.class.getName()).log(Level.SEVERE, "", e);
//                }
//            }
//        }
//    }
//    
//}
