/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.message.Message;
import name.huliqing.luoying.message.MessageFactory;
import name.huliqing.luoying.message.MessageHandler;

/**
 *
 * @author huliqing
 */
public class MessageServiceImpl implements MessageService {

    @Override
    public void inject() {
        // 
    }

    @Override
    public void addHandler(MessageHandler handler) {
        MessageFactory.addHandler(handler);
    }

    @Override
    public boolean removeHandler(MessageHandler handler) {
        return MessageFactory.removeHandler(handler);
    }
    
    @Override
    public void addMessage(Message message) {
        MessageFactory.post(message);
    }
    
}
