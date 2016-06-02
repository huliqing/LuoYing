/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.data;

import com.jme3.network.serializing.Serializable;

/**
 * 基本的原始物体数据
 * @author huliqing
 */
@Serializable
public class ItemData extends PkgItemData {
    
    public ItemData() {} 

    public ItemData(String id) {
        super(id);
    }
}
