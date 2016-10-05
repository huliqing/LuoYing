/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * test
 * Message管理器
 * @author huliqing
 */
public class MessageManager {
    
    private final static List<MessageHandler> HANDLERS = new ArrayList<MessageHandler>();
    private static boolean debug = true;
    
    static {
        // 添加一个默认的handler用于DEBUG
        HANDLERS.add(new SystemMessageHandler());
    }
    
    public static void postMessage(int code, String message, Object... params) {
        if (HANDLERS.isEmpty())
            return;
        for (int i = 0; i < HANDLERS.size(); i++) {
            HANDLERS.get(i).onPostMessage(code, message, params);
        }
    }
    
    /**
     * 注册一个新的用于处理消息的钩子
     * @param handler 
     */
    public static void registerHandler(MessageHandler handler) {
        if (!HANDLERS.contains(handler)) {
            HANDLERS.add(handler);
        }
    }
    
    /**
     * 打开或关闭默认的系统消息钩子
     * @param enabled 
     */
    public static void setDebugEnabled(boolean enabled) {
        debug = enabled;
    }
    
    private static class SystemMessageHandler implements MessageHandler {
        @Override
        public void onPostMessage(int code, String message, Object... params) {
            if (debug) {
                System.out.println("Message: [" + code + "] " +  String.format(message, params));
            }
        }
    }
}
