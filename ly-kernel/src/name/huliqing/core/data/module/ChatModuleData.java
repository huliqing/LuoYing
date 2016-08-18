/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data.module;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;

/**
 * @author huliqing
 */
@Serializable
public class ChatModuleData extends ModuleData {
    
    private String chat;

    /**
     * 角色默认的对话面板id
     * @return 
     */
    public String getChat() {
        return chat;
    }

    /**
     * 角色默认的对话面板id
     * @param chat 
     */
    public void setChat(String chat) {
        this.chat = chat;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(chat, "chat", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im); 
        InputCapsule ic = im.getCapsule(this);
        chat = ic.readString("chat", null);
    }
    
}
