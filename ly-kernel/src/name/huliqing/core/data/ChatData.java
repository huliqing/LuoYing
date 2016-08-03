/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import name.huliqing.core.xml.ProtoData;
import com.jme3.network.serializing.Serializable;

/**
 * 对话面板
 * @author huliqing
 */
@Serializable
public class ChatData extends ProtoData {
    
    public ChatData(){
        super();
    }
    
    public ChatData(String id) {
        super(id);
    }

}
