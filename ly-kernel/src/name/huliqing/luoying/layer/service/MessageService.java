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
