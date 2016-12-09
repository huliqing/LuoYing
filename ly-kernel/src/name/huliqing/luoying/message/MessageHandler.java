/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.message;

/**
 * 消息处理器主要用于接收来自游戏运行时的消息，类似于日志消息处理器，
 * 不同的是MessageHandler用于接收来自游戏运行时的<b>非异常</b>消息，例如：角色的物品使用消息
 * ，技能使用消息等，通过MessageHandler可以将这些游戏运行时的消息显示到游戏控制台、或者游戏主界面等。
 * @author huliqing
 */
public interface MessageHandler {
    
    /**
     * 处理消息
     * @param message
     */
    abstract void handle(Message message);
}
