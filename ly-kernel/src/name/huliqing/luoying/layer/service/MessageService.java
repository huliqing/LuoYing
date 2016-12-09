/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.Inject;
import name.huliqing.luoying.message.Message;
import name.huliqing.luoying.message.MessageHandler;

/**
 * 游戏消息服务接口， 通过这个服务类来向游戏控制台发送游戏消息,和异常日志不一样
 * ，游戏消息是游戏正常运行时的消息。这些消息主要用于提示当前玩家的游戏行为，例如：技能不能执行、物品不能使用等等。
 * @author huliqing
 */
public interface MessageService extends Inject {
 
    /**
     * 添加一个消息处理器,用于接收游戏消息,注意：添加后的消息处理器将一直存在，除非进行了移除,注意不要重复添加。
     * @param handler 
     */
    void addHandler(MessageHandler handler);
    
    /**
     * 移除一个指定的消息处理器。
     * @param handler 
     * @return  
     */
    boolean removeHandler(MessageHandler handler);
    
    /**
     * 添加游戏消息
     * @param message 
     */
    void addMessage(Message message);
}
