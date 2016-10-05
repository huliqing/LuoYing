/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import name.huliqing.ly.layer.network.ChatNetwork;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.chat.Chat;

/**
 *
 * @author huliqing
 */
public interface ChatService extends ChatNetwork {
    
    /**
     * 载入Chat
     * @param chatId
     * @return 
     */
    Chat loadChat(String chatId);
    
    /**
     * 获取角色的对话面板,如果没有设置，则返回null.
     * @param actor
     * @return 
     */
    Chat getChat(Actor actor);
    

}
