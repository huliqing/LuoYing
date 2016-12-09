/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.sound.Sound;

/**
 * 声音服务接口
 * @author huliqing
 */
public interface SoundService extends Inject {

    /**
     * 简单播放声效，非循环。
     * @param soundId 声效ID
     * @param position 声源位置
     */
    void playSound(String soundId, Vector3f position);
    
    /**
     * 添加声音到列表中并立即播放，该方法会立即播放声音。当声音不再使用时要将声音从列表中移除。
     * @param sound 
     */
    void addAndPlay(Sound sound);
    
    /**
     * 停止声音循环，并将声音移除出列表，这样可以让声音自然停止。
     * @param sound 
     * @return  
     * @see #removeAndStopDirectly(Sound) 
     */
    boolean removeAndStopLoop(Sound sound);

    /**
     * 直接停止播放指定的声音，并将声音移出列表, 部分情况下这可能会让声音停止得太不自然。
     * @param sound 
     * @return  
     * @see #removeAndStopLoop(Sound) 
     */
    boolean removeAndStopDirectly(Sound sound);
    
    /**
     * 判断音效是否打开.
     * @return 
     */
    boolean isSoundEnabled();
    
        /**
     * 开启或关闭全局声音，如果是关闭声音，则所有当前列表中的声音都将被立即停止播放，并且所有“非循环”类型的声音将
     * 会被移出列表中，只留下“循环”的, 以便当重新打开声音时，所有循环类型的声音可以重新播放。
     * @param enabled 
     */
    void setSoundEnabled(boolean enabled);
    
    /**
     * 设置音量，0.0~1.0
     * @return 
     */
    float getVolume();

    /**
     * 获取音量，0.0~1.0
     * @param volume 
     */
    void setVolume(float volume);
            
}


