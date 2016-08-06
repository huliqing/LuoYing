/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.sound;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 用于统一管理声音的管理器
 * @author huliqing
 */
public class SoundManager  {
    
    private final static SoundManager INSTANCE = new SoundManager();
    
    private final Set<Sound> sounds = new HashSet<Sound>();
    
    // 声音的开启或关闭
    private boolean soundEnabled = true;
    
    private SoundManager() {}
    
    public static SoundManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * 添加声音到列表中并立即播放，该方法会立即播放声音。让得当声音不再使用时要将声音从列表中移除。
     * @param sound 
     */
    public void addSound(Sound sound) {
        // 如果全局声音未打开，则只要将“循环”类型的声音添加到列表即可，不需要执行播放，而”非循环“的声音直接丢弃，不
        // 需要播放也不需要添加到列表中。
        if (!soundEnabled) {
            if (sound.isLoop()) {
                sounds.add(sound);
            }
            return;
        }
        sounds.add(sound);
        sound.play();
    }
    
    /**
     * 将声音移出列表
     * @param sound 
     * @return  
     */
    public boolean removeSound(Sound sound) {
        return sounds.remove(sound);
    }

    /**
     * 停止播放指定的声音，并将声音移出列表。
     * @param sound 
     */
    public void stopAndRemove(Sound sound) {
        sound.stop();
        removeSound(sound);
    }
    
    /**
     * 开启或关闭全局声音，如果是关闭声音，则所有当前列表中的声音都将被立即停止播放，并且所有“非循环”类型的声音将
     * 会被移出列表中，只留下“循环”的, 以便当重新打开声音时，所有循环类型的声音可以重新播放。
     * @param enabled 
     */
    public void setSoundEnabled(boolean enabled) {
        // 值不改变则不需要处理多余的逻辑
        if (soundEnabled == enabled) 
            return;
        
        soundEnabled = enabled;
        if (soundEnabled) {
            restartSounds();
        } else {
            stopSounds();
        }
    }
    
    /**
     * 停止播放所有声音,并且将所有不是循环类型的声音移除出列表。
     */
    private void stopSounds() {
        Iterator<Sound> it = sounds.iterator();
        Sound temp;
        while (it.hasNext()) {
            temp = it.next();
            temp.stop();
            if (!temp.isLoop()) {
                it.remove();
            }
        }
    }
    
    /**
     * 重新打开声音,所有循环类型的声音会重新开始播放，而非循环的声音会被移除出列表。
     */
    private void restartSounds() {
        Iterator<Sound> it = sounds.iterator();
        Sound temp;
        while (it.hasNext()) {
            temp = it.next();
            if (temp.isLoop()) {
                temp.play();
            } else {
                it.remove();
            }
        }
    }

}
