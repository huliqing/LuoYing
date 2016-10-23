/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.module;

import java.util.List;
import name.huliqing.ly.data.ChatData;
import name.huliqing.luoying.data.ObjectData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.ly.object.chat.Chat;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.AbstractModule;

/**
 * 角色对话模块
 * @author huliqing
 */
public class ChatModule extends AbstractModule {

    private Chat chat;

    @Override
    public void updateDatas() {
        // ignore
    }

    @Override
    public void initialize(Entity actor) {
        super.initialize(actor);
        
        // 目标只支持配置一个chat, 如果需要多个chat，则应该包装在GroupChat下面。
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
