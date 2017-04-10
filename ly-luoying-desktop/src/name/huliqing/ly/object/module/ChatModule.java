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
package name.huliqing.ly.object.module;

import java.util.List;
import name.huliqing.ly.data.ChatData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.ly.object.chat.Chat;
import name.huliqing.luoying.object.module.AbstractModule;

/**
 * 角色对话模块
 * @author huliqing
 */
public class ChatModule extends AbstractModule {

    private Chat chat;

    @Override
    public void initialize() {
        super.initialize();
        // 目标只支持配置一个chat, 如果需要多个chat，则应该包装在GroupChat下面。
        List<ChatData> chatDatas = entity.getData().getObjectDatas(ChatData.class, null);
        if (chatDatas != null && !chatDatas.isEmpty()) {
            for (ChatData cd : chatDatas) {
                setChat((Chat) Loader.load(cd));
            }
        }
    }

    @Override
    public void cleanup() {
        chat = null;
        super.cleanup(); 
    }
    
    private void setChat(Chat chat) {
        chat.setActor(entity);
        this.chat = chat;
    }
    
    /**
     * 获取角色的主对话面板,如果没有为角色设置对话功能则返回null.
     * @return 
     */
    public Chat getChat() {
        return chat;
    }


    
}
