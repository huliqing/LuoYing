/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess.network;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.mess.BaseMess;

/**
 * 服务端响应客户端关于请求初始化游戏（MessRequestGameInit）的结果，
 * 这个消息会返回一个当前服务端需要初始化到客户端场景的实体数。<br>
 * 注：当客户端接收到这个消息之后，客户端状态应该立即改变为running状态，服务端将立即向客户端发送初始化场景的实体数据，
 * 这些数据是连续发送的，直到到达initEntityCount所指定的数量,之后服务端才可能发送其它消息及响应客户端的其它消息。
 * @author huliqing
 */
@Serializable
public class RequestGameInitOkMess extends BaseMess {
    
    // Will init entity count, 这个参数表示服务端将会向客户端初始化多少个场景实体。
    // 当客户端接收到这个消息后，
    private int initEntityCount;
    
    public RequestGameInitOkMess() {}

    /**
     * 设置需要向客户端初始化的场景实体数。
     * @param initEntityCount 
     */
    public RequestGameInitOkMess(int initEntityCount) {
        this.initEntityCount = initEntityCount;
    }

    /**
     * 获取服务端将向客户端初始化的实体数
     * @return 
     */
    public int getInitEntityCount() {
        return initEntityCount;
    }

    /**
     * 设置需要向客户端初始化的场景实体数。
     * @param initEntityCount 
     */
    public void setInitEntityCount(int initEntityCount) {
        this.initEntityCount = initEntityCount;
    }

    @Override
    public String toString() {
        return "RequestGameInitOkMess{" + "initEntityCount=" + initEntityCount + '}';
    }
    
}
