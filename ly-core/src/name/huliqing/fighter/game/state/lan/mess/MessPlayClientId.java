/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.mess;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * client to server, 客户端首次连接到服务端时发送的第一个信息，主要用于交
 * 待当前连接的客户端的身份信息。
 * @author huliqing
 */
@Serializable
public class MessPlayClientId extends MessBase {
    
    // 客户端名称标识,如PC名称，手机名称
    private String clientName;
    
    public MessPlayClientId() {}
    
    public MessPlayClientId(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

}
