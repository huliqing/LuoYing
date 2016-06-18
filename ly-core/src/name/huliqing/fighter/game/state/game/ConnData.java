/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.game;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author huliqing
 */
@Serializable
public class ConnData extends AbstractMessage {
    
    /**
     * HostedConnection的attribute的key
     */
    public transient final static String CONN_ATTRIBUTE_KEY = "_CONN_ATTRIBUTE_";
    
    // 客户端唯一标识，这个标识对于所有客户端或主机来说是唯一的。
    private String clientId;
    
    // 客户端名称标识,如PC名称，手机名称
    private String clientName;
    
    // 客户端的连接ID，该ID对于每个客户端连接都是唯一的
    private int connId; 
    
    // 连接地址，即客户端的IP信息
    private String address;
    
    // 客户端所控制的角色的唯一ID
    private long actorId;

     // 角色名称
    private String actorName;
    
    /**
     * 获取客户端唯一标识，这个标识对于所有客户端或主机来说是唯一的。
     * @return 
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * 客户端唯一标识，这个标识对于所有客户端或主机来说是唯一的。
     * @param clientId 
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * 客户端名称标识,如PC名称，手机名称
     * @return 
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * 客户端名称标识,如PC名称，手机名称
     * @param clientName 
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * 客户端所控制的角色的唯一ID,如果为
     * @return 
     */
    public long getActorId() {
        return actorId;
    }

    /**
     * 设置客户端所控制的角色的唯一ID
     * @param actorId 
     */
    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public int getConnId() {
        return connId;
    }

    public void setConnId(int connId) {
        this.connId = connId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }
    
    
}
