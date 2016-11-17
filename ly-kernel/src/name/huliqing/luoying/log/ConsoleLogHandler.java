/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.log;

import java.util.Arrays;

/**
 * 控制台日志输出，用于简单的将日志消息输出到控制台。功能测试中。。。
 * @author huliqing
 */
public class ConsoleLogHandler implements LogHandler {

    @Override
    public void handle(int code, String message, Object... params) {
        System.out.println("[" + code + "] " + message + (params == null ? "[]" : Arrays.toString(params)));
    }
    
}
