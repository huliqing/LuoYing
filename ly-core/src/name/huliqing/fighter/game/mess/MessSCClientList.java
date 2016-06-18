/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.mess;

import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.game.state.game.ConnData;

/**
 * 服务端发客户端发送客户端列表信息：当前已经连接到服务器的客户端列表
 * @author huliqing
 */
@Serializable
public class MessSCClientList extends MessBase {
    
    // remove注意：在Serializable对象中不要使用final字段，这会造成无法序列化，除非该对象始终不变（特别是LIST,MAP）
    // 。这在JME中没有报错，但是在重新生成对象的时候该列表会空值。
//    private final List<ConnData> clients = new ArrayList<ConnData>();
    
    private List<ConnData> clients = new ArrayList<ConnData>();
    
    public MessSCClientList() {}
    
    public MessSCClientList(List<ConnData> clients) {
        this.clients.addAll(clients);
    }
    
    public void addClient(ConnData client) {
        clients.add(client);
    }
    
    public List<ConnData> getClients() {
        return clients;
    }
    
}
