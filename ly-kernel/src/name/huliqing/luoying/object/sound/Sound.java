/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.object.sound;

import com.jme3.audio.AudioContext;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.SoundData;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * 声效接口
 * @author huliqing
 * @param <T>
 */
public class Sound<T extends SoundData> extends Node implements DataProcessor<T> {

    protected T data;
    
    private AudioNode audio;
    private boolean initialized;
    
    @Override
    public void setData(T data) {
        this.data = data;
    }
    
    @Override
    public T getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
    }
    
    /**
     * 初始化声音
     */
    public void initialize() {
        audio = loadAudio();
        if (audio != null) {
            attachChild(audio);
        }
        initialized = true;
    }
    
    /**
     * 判断音效是否已经初始化
     * @return 
     */
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * 清理并释放音效资源
     */
    public void cleanup() {
        if (audio != null) {
            audio.stop();
            audio = null;
        }
        initialized = false;
    }
    
    /**
     * 播放声音文件
     */
    public void play() {
        if (audio == null) {
            return;
        }
        
        // 声音文件在播放的时候需要在渲染线程,不然会报错。
        //    Caused by: java.lang.IllegalStateException: No audio renderer available, make sure call is being performed on render thread.
        //	at com.jme3.audio.AudioNode.getRenderer(AudioNode.java:207)
        //	at com.jme3.audio.AudioNode.play(AudioNode.java:218)
        //	at name.huliqing.core.object.sound.Sound.play(Sound.java:43)
        if (AudioContext.getAudioRenderer() == null) {
            return;
        }
        
        if (data.isInstance()) {
            audio.playInstance();
        } else {
            audio.play();
        }
    }
    
    public void pause() {
        if (audio == null || AudioContext.getAudioRenderer() == null) {
            return;
        }
        audio.pause();
    }
    
    /**
     * 停止声音
     */
    public void stop() {
        if (audio == null || AudioContext.getAudioRenderer() == null) {
            return;
        }
        audio.stop();
    }
    
    public boolean isLoop() {
        if (audio == null)
            return false;
        return audio.isLooping();
    }
    
    /**
     * 设置声音循环
     * @param loop
     */
    public void setLoop(boolean loop) {
        if (audio == null || AudioContext.getAudioRenderer() == null) {
            return;
        }
        audio.setLooping(loop);
    }
    
    public void setVolume(float volume) {
        if (audio == null || AudioContext.getAudioRenderer() == null) {
            return;
        }
        audio.setVolume(MathUtils.clamp(volume, 0f, 1.0f));
    }
    
    private AudioNode loadAudio() {
        if (data.getSoundFile() == null) {
            return null;
        }
        
        AudioNode an = new AudioNode(LuoYing.getAssetManager(), data.getSoundFile(), AudioData.DataType.Buffer);
        an.setVolume(data.getVolume());
        an.setTimeOffset(data.getTimeOffset());
        an.setLooping(data.isLooping());
        an.setDirection(data.getDirection());
        an.setDirectional(data.isDirectional());
        an.setInnerAngle(data.getInnerAngle());
        an.setMaxDistance(data.getMaxDistance());
        an.setOuterAngle(data.getOuterAngle());
        an.setPitch(data.getPitch());
        
        // 容易java.lang.IllegalStateException: Only mono audio is supported for positional audio nodes
        an.setPositional(data.isPositional());
        
        an.setRefDistance(data.getRefDistance());
        an.setReverbEnabled(data.isReverbEnabled());
        an.setVelocity(data.getVelocity());
        an.setVelocityFromTranslation(data.isVelocityFromTranslation());
        
//        audio.setDryFilter(); // 暂不支持。
//        audio.setReverbFilter();
        return an;
    }
}
