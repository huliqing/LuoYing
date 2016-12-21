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

import name.huliqing.luoying.message.Message;
import name.huliqing.luoying.message.MessageFactory;
import name.huliqing.luoying.message.MessageHandler;

/**
 *
 * @author huliqing
 */
public class MessageServiceImpl implements MessageService {

    @Override
    public void inject() {
        // 
    }

    @Override
    public void addHandler(MessageHandler handler) {
        MessageFactory.addHandler(handler);
    }

    @Override
    public boolean removeHandler(MessageHandler handler) {
        return MessageFactory.removeHandler(handler);
    }
    
    @Override
    public void addMessage(Message message) {
        MessageFactory.post(message);
    }
    
}
