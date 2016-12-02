/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * luoying的Message的基类
 * @author huliqing
 */
@Serializable
public class BaseMess extends AbstractMessage{
    
    private double time;
    
    public BaseMess() {}
    
    public BaseMess(boolean reliable) {
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
    
    
}
