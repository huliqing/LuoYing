/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mess;

import com.jme3.network.AbstractMessage;
import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.core.network.GameServer;

/**
 * @author huliqing
 */
@Serializable
public class MessBase extends AbstractMessage {
    
    public double time;
    
    public MessBase() {}
    
    public MessBase(boolean reliable) {
        super(reliable);
    }

    public void applyOnClient() {
        // 由子类覆盖
    }
    
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        // 由子类覆盖
    }
}
