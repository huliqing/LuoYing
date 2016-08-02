/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.network.discover;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.game.service.SystemService;

/**
 * 用于查找主机，或者通知客户端。这个类可以在客户端运行也可以在服务端运行。
 * 当在服务端运行时可以作为服务端监听来自客户端的消息。
 * 当作为客户端运行时可以向服务端发送查询消息。
 * 这个类使用UDP方式接收和发送数据.
 * @author huliqing
 */
public class UDPDiscover {
//    private final static Logger logger = Logger.getLogger(UDPDiscover.class.getName());
    
    // 本地监听端口
    private int localListenPort;
    private DatagramSocket socket;
//    private byte[] buffer = new byte[65535];
    private byte[] buffer = new byte[10240]; // 可尽量比需要的大一些
    private UDPListener listener; // 接收到消息时的侦听器
    private boolean started;
    // 消息接收线程
    private Receive receive;
    
    public UDPDiscover(int localListenPort) {
        this.localListenPort = localListenPort;
    }
    
    public void start() {
        if (started) {
            return;
        }
        try {
            socket = new DatagramSocket(new InetSocketAddress(localListenPort));
            socket.setBroadcast(true);
            receive = new Receive();
            receive.start();
            started = true;
        } catch (SocketException ex) {
            started = false;
            Logger.getLogger(UDPDiscover.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        started = false;
    }
    
    /**
     * 向目标机器发送UDP信息,调用该方法前需要确保已经启动（start）
     * @param mess 要向目标机器发送的消息
     * @param ipAddress 目标机器的IP地址，如：192.168.1.8
     * @param port 目标机器的端口，如：32999
     */
    public void send(AbstractMess mess, String ipAddress, int port) {
        if (!isRunning()) {
            return;
        }
        try {
            byte[] data = serializeMess(mess);
            DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(ipAddress), port);
            socket.send(packet);
        } catch (Exception ex) {
            Logger.getLogger(UDPDiscover.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 向局域网广播消息,调用该方法前需要确保已经启动（start）
     * @param message 要向本地网络广播的消息,如果为null则什么也不做
     * @param boradcastPort 指定要向本地网络中目标机器上哪一个端口进行广播
     */
    public void broadcast(AbstractMess mess, int broadcastPort) {
        if (!isRunning() || mess == null) {
            return;
        }
        try {            
            byte[] data = serializeMess(mess);
            DatagramPacket packet = new DatagramPacket(data, data.length, getBroadcastAddress(), broadcastPort);
            socket.send(packet);
        } catch (Exception ex) {
            Logger.getLogger(UDPDiscover.class.getName()).log(Level.SEVERE, "Could not broadcast message!", ex);
        }
    }
    
    private byte[] serializeMess(AbstractMess mess) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(mess);
        return baos.toByteArray();
    }
    
    /**
     * 获取当前系统的UDP广播地址
     * @return 
     */
    protected InetAddress getBroadcastAddress() {
        return Factory.get(SystemService.class).getBroadcastAddress();
    }
    
    private boolean isRunning() {
        return started;
    }
    
    /**
     * 设置一个侦听器，用于处理接收到的消息。
     * @param listener 
     */
    public void setListener(UDPListener listener) {
        this.listener = listener;
    }
    
    // 消息监听
    private class Receive extends Thread {
        
        @Override
        public void run() {
            while (started) {
                try {
                    // 接收UDP数据包。
                    DatagramPacket inPacket = new DatagramPacket(buffer, 0, buffer.length);
                    socket.receive(inPacket);
                    
                    ByteArrayInputStream bais = new ByteArrayInputStream(inPacket.getData());
                    ObjectInputStream bis = new ObjectInputStream(bais);
                    // 注意这里可能会发生异常，因为可能读取到不能转化为某个特定Object的数据包，
                    // 比如外界（非应用内）某些应用向当前监听端口发送特殊数据包则可能发生这种情况。
                    // 当发生这种情况则捕获异常后不作任何处理，因为不是当前应用程序需要的,丢弃后直接
                    // 继续监听下一个数据包就可以。
                    Object object = bis.readObject();
                    //logger.log(Level.INFO, "Receive Object={0}", object.getClass().getName());
                    
                    listener.receive(object, UDPDiscover.this, inPacket);
                } catch (Throwable e) {
                    //Logger.getLogger(UDPDiscover.class.getName()).log(Level.SEVERE, "", e);
                }
            }
        }
    }
    
}
