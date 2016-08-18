/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import name.huliqing.core.data.module.ChatModuleData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.chat.Chat;

/**
 * 角色对话模块
 * @author huliqing
 * @param <T>
 */
public class ChatModule<T extends ChatModuleData> extends AbstractModule<T> {

    private Actor actor;
    private Chat chat;

    @Override
    public void initialize(Actor actor) {
        super.initialize(actor);
        this.actor = actor;
    }

    @Override
    public void cleanup() {
        chat = null;
        super.cleanup(); 
    }

    /**
     * 获取角色的主对话面板,如果没有为角色设置对话功能则返回null.
     * @return 
     */
    public Chat getChat() {
        if (chat == null && data.getChat() != null) {
            chat = Loader.load(data.getChat());
            chat.setActor(actor);
        }
        return chat;
    }
    
}
