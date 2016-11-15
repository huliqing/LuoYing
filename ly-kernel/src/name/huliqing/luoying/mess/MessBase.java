/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.AbstractMessage;
import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.manager.RandomManager;
import name.huliqing.luoying.network.GameServer;

/**
 * @author huliqing
 */
@Serializable
public class MessBase extends AbstractMessage {
    
    private double time;
    private byte randomIndex = -1;
    
    public MessBase() {}
    
    public MessBase(boolean reliable) {
        super(reliable);
    }

    /**
     * 获取消息的发送时间
     * @return 
     */
    public final double getTime() {
        return time;
    }
    
    /**
     * 设置消息的发送时间。
     * @param time 
     */
    public final void setTime(double time) {
        this.time = time;
    }
    
    /**
     * 设置随机数索引，以便与客户端进行同步。
     * @param randomIndex 
     */
    public final void setRandomIndex(byte randomIndex) {
        this.randomIndex = randomIndex;
    }
    
    public void applyOnClient() {
        // 在客户端同步随机数的索引。
        if (randomIndex != -1) {
            RandomManager.setIndex(randomIndex);
        }
    }
    
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        // 由子类覆盖
    }
}
