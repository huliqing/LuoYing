/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.mess;

import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * server to client,告诉客户端：当前已经连接到服务器的客户端列表
 * @author huliqing
 */
@Serializable
public class MessSCClientList extends MessBase {
    
    private final List<MessPlayClientData> clients = new ArrayList<MessPlayClientData>();
    
    public MessSCClientList() {}
    
    public MessSCClientList(List<MessPlayClientData> clients) {
        this.clients.addAll(clients);
    }
    
    public void addClient(MessPlayClientData client) {
        clients.add(client);
    }
    
    public List<MessPlayClientData> getClients() {
        return clients;
    }
    
}
