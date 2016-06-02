/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import name.huliqing.fighter.data.ChatData;
import name.huliqing.fighter.object.chat.Chat;
import name.huliqing.fighter.object.chat.GroupChat;
import name.huliqing.fighter.object.chat.SellChat;
import name.huliqing.fighter.object.chat.SendChat;
import name.huliqing.fighter.object.chat.ShopChat;
import name.huliqing.fighter.object.chat.TaskChat;

/**
 *
 * @author huliqing
 */
class ChatLoader {
    
    public static Chat load(ChatData data) {
        String tagName = data.getTagName();
        if (tagName.equals("chatGroup")) {
            return new GroupChat(data);
        }
        
        if (tagName.equals("chatSend")) {
            return new SendChat(data);
        }
        
        if (tagName.equals("chatShop")) {
            return new ShopChat(data);
        }
        
        if (tagName.equals("chatSell")) {
            return new SellChat(data);
        }
        
        if (tagName.equals("chatTask")) {
            return new TaskChat(data);
        }
        
        throw new UnsupportedOperationException("Unknow tagName=" + tagName);
    }
    
}
