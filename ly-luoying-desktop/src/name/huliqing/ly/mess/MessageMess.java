/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.luoying.mess.GameMess;
import name.huliqing.luoying.network.GameClient;
import name.huliqing.ly.layer.service.GameService;

/**
 *
 * @author huliqing
 */
@Serializable
public class MessageMess extends GameMess {
    
    private String message;
    private MessageType type = MessageType.notice;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    @Override
    public void applyOnClient(GameClient gameClient) {
        super.applyOnClient(null);
        GameService gameService = Factory.get(GameService.class);
        gameService.addMessage(message, type);
    }
    
}
