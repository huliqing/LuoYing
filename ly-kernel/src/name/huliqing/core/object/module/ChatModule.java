/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import java.util.List;
import name.huliqing.core.data.ChatData;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.chat.Chat;

/**
 * 角色对话模块
 * @author huliqing
 */
public class ChatModule extends AbstractModule {

    private Actor actor;
    private Chat chat;

    @Override
    public void initialize(Actor actor) {
        super.initialize(actor);
        this.actor = actor;
        
        List<ObjectData> ods = actor.getData().getObjectDatas();
        if (ods != null && !ods.isEmpty()) {
            for (ObjectData od : ods) {
                if (od instanceof ChatData) {
                    setChat((Chat) Loader.load(od));
                }
            }
        }
        
    }

    @Override
    public void cleanup() {
        chat = null;
        super.cleanup(); 
    }
    
    private void setChat(Chat chat) {
        chat.setActor(actor);
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
