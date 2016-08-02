/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object;

/**
 * 需要在与客户端进行同步的物体
 * @author huliqing
 */
public interface NetworkObject {
    
    /**
     * 获取同步物体的ID
     * @return 
     */
    long getSyncId();
    
    /**
     * 判断是否开始同步
     * @return 
     */
    boolean isSyncEnabled();
    
    /**
     * 设置是否开始客户端同步
     * @param enabled 
     */
    void setSyncEnabled(boolean enabled);
    
    /**
     * 同步数据到本地
     * @param data 
     */
    void applySyncData(SyncData data);
}
