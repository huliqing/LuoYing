/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.message;

/**
 * @author huliqing
 */
public interface Message {
    
    /**
     * 获取消息的状态码,一般来说每个不同的状态码表示一个不同的消息类型。
     * 内置的状态码参考{@link StateCode}
     * @return 
     * @see StateCode
     */
    int getStateCode();
    
    /**
     * 获取消息的描述说明
     * @return 
     */
    String getMessage();
    
}
