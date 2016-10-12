/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.ly.Factory;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.mess.MessBase;

/**
 *
 * @author huliqing
 */
@Serializable
public class MessMessage extends MessBase {
    
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
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        playService.addMessage(message, type);
    }
    
}
