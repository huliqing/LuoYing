/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
