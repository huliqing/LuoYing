/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.mess;

import com.jme3.network.serializing.Serializable;

/**
 * 客户端向服务端发出消息，要求服务端偿试去载入客户端的存档角色。这发生在故事模式情况下，
 * @author huliqing
 */
@Serializable
public class MessPlayLoadSavedActor  extends MessBase {

    public MessPlayLoadSavedActor() {}
    
}
