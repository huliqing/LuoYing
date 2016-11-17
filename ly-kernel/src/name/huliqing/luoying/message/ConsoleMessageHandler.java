/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.message;

import java.util.Arrays;

/**
 * 控制台消息处理器，用于简单的将消息输出到控制台。
 * @author huliqing
 */
public class ConsoleMessageHandler implements MessageHandler {

    @Override
    public void handle(int code, String message, Object... params) {
        System.out.println("[" + code + "] " + message + (params == null ? "[]" : Arrays.toString(params)));
    }
    
}
