/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.data;

import com.jme3.network.serializing.Serializable;

/**
 *
 * @author huliqing
 */
@Serializable
public class GameLogicData extends ObjectData {
    
    private float interval;

    /**
     * 获取游戏逻辑的执行时间间隔，单位秒。
     * @return 
     */
    public float getInterval() {
        return interval;
    }

    /**
     * 设置游戏逻辑的执行时间间隔，单位秒。
     * @param interval 
     */
    public void setInterval(float interval) {
        this.interval = interval;
    }
    
}
