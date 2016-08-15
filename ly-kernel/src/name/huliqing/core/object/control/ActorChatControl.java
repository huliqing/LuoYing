/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import name.huliqing.core.object.chat.Chat;

/**
 * 控制角色对话面板的控制器
 * @author huliqing
 */
public class ActorChatControl extends ActorControl {

    // 角色的主对话面板
    private Chat chat;
    
    @Override
    public void actorUpdate(float tpf) {
    }

    @Override
    public void actorRender(RenderManager rm, ViewPort vp) {
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Chat getChat() {
        return chat;
    }
    
}
