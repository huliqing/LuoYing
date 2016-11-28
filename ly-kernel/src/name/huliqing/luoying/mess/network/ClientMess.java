/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess.network;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.mess.BaseMess;

/**
 * 客户端首次连接到服务端时发送的第一个信息，主要用于向服务端交待当前连接的客户端的身份信息。
 * @author huliqing
 */
@Serializable
public class ClientMess extends BaseMess {
    
    // 客户端唯一标识，这个标识对于所有客户端或主机来说是唯一的。
    private String clientId;
    
    // 客户端名称标识,如PC名称，手机名称
    private String clientName;
    
    public ClientMess() {}
    
    public ClientMess(String clientId, String clientName) {
        this.clientId = clientId;
        this.clientName = clientName;
    }

    /**
     * 获取客户端的ID标识。
     * @return 
     */
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

}
