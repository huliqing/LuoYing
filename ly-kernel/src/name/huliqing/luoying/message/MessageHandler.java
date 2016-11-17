/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.message;

/**
 *
 * @author huliqing
 */
public interface MessageHandler {
    
    /**
     * 处理消息
     * @param code 消息的状态码
     * @param message
     * @param params 
     * @see name.huliqing.luoying.constants.StateCode
     */
    void handle(int code, String message, Object... params);
}
