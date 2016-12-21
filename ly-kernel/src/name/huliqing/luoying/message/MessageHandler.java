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
