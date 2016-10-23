/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.mess.MessBase;
import name.huliqing.luoying.object.SyncData;
import name.huliqing.ly.object.NetworkObject;
import name.huliqing.ly.layer.service.GameService;

/**
 *
 * @author huliqing
 */
@Serializable
public class MessSyncObject extends MessBase{

    // 同步的物品ID
    private long objectId;
    
    // 需要同步的数据
    private SyncData syncData;

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public void setSyncData(SyncData syncData) {
        this.syncData = syncData;
    }
    
    @Override
    public void applyOnClient() {
        super.applyOnClient();
        NetworkObject syncObject = Factory.get(GameService.class).findSyncObject(objectId);
        if (syncObject != null) {
            syncObject.applySyncData(syncData);
        }
    }
    
}
