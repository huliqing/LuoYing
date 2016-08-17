/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actormodule;

import name.huliqing.core.data.ModuleData;
import name.huliqing.core.object.chat.Chat;

/**
 * 控制角色对话面板的控制器
 * @author huliqing
 * @param <T>
 */
public class ChatActorModule<T extends ModuleData> extends AbstractSimpleActorModule<T> {

    // 角色的主对话面板
    private Chat chat;

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Chat getChat() {
        return chat;
    }
    
}
