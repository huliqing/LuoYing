/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.mess;

import com.jme3.network.AbstractMessage;
import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.game.state.lan.GameServer;

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
//        Logger.getLogger(getClass().getName()).log(Level.WARNING, "TODO applyOnClint, mess class={0}", getClass());
    }
    
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
//        Logger.getLogger(getClass().getName()).log(Level.WARNING, "TODO applyOnServer, mess class={0}", getClass());
    }
}
