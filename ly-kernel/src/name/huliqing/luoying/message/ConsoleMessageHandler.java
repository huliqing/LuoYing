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
 * 控制台消息输出，用于简单的将游戏消息输出到控制台。
 * @author huliqing
 */
public class ConsoleMessageHandler implements MessageHandler {

    private static ConsoleMessageHandler instance;
    
    private ConsoleMessageHandler() {}
    
    public static synchronized ConsoleMessageHandler getInstance() {
        if (instance == null) {
            instance = new ConsoleMessageHandler();
        }
        return instance;
    }
    
    @Override
    public void handle(Message message) {
        
        if (message instanceof EntityDataUseMessage) {
            EntityDataUseMessage mess = (EntityDataUseMessage) message;
            System.out.println(getClass().getSimpleName() + ":" + mess.getClass().getSimpleName() + "[stateCode=" + mess.getStateCode() + "]"
                    + mess.getEntity().getData().getId() + " use data " + mess.getObjectData().getId());
        } else {
            System.out.println(getClass().getSimpleName() + ":" + message.getClass().getSimpleName() + "[stateCode=" + message.getStateCode() + "]" 
                    + message.getMessage());
        }
        
    }
    
}
