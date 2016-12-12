/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.message;

import com.jme3.util.SafeArrayList;

/**
 * MessageFactory用于统一处理游戏信息。
 * @author huliqing
 */
public class MessageFactory {
    
    private final static SafeArrayList<MessageHandler> HANDLERS = new SafeArrayList<MessageHandler>(MessageHandler.class);
    
    /**
     * 添加消息，这些消息将由当前已经注册的MessageHandler进行处理，如果没有添加过任何消息处理器, 
     * 则该方法将什么也不会做。
     * @param message 消息内容
     * @see #addHandler(MessageHandler) 
     */
    public static void post(Message message) {
        if (HANDLERS.isEmpty()) 
            return;
        for (MessageHandler handler : HANDLERS.getArray()) {
            handler.handle(message);
        }
    }
    
    /**
     * 添加一个消息处理器, 添加后除非调用了{@link #removeHandler(MessageHandler) }否则该消息处理器将一直存在，
     * 注意不要重复添加。
     * @param handler 
     */
    public static void addHandler(MessageHandler handler) {
        if (handler == null) 
            throw new NullPointerException("Handler could not be null!");
        
        if (HANDLERS.isEmpty()) {
            HANDLERS.add(handler);
            return;
        }
        
//        for (MessageHandler mh : HANDLERS.getArray()) {
//            if (mh.getClass().getName().equals(handler.getClass().getName())) {
//                LOG.log(Level.WARNING, "MessageHandler already exists! {0}", mh.getClass().getName());
//                return;
//            }
//        }
        
        if (!HANDLERS.contains(handler)) {
            HANDLERS.add(handler);
        }
    }
    
    /**
     * 移除一个消息处理器
     * @param handler
     * @return 
     */
    public static boolean removeHandler(MessageHandler handler) {
        return HANDLERS.remove(handler);
    }
}
