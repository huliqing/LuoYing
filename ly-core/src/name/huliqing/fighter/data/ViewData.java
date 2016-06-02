/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.data;

import com.jme3.network.serializing.Serializable;

/**
 * 界面
 * @author huliqing
 */
@Serializable
public class ViewData extends ProtoData {
    
    public ViewData(){
        super();
    }
    
    public ViewData(String id) {
        super(id);
    }

}
