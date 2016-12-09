/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.object.sound.Sound;
import name.huliqing.luoying.object.sound.SoundManager;

/**
 *
 * @author huliqing
 */
public class SoundServiceImpl implements SoundService {
    private final SoundManager sm = SoundManager.getInstance();
    
    @Override
    public void inject() {
    }
    
    @Override
    public void playSound(String soundId, Vector3f position) {
        sm.playSound(soundId, position);
    }

    @Override
    public void addAndPlay(Sound sound) {
        sm.addAndPlay(sound);
    }

    @Override
    public boolean removeAndStopLoop(Sound sound) {
        return sm.removeAndStopLoop(sound);
    }

    @Override
    public boolean removeAndStopDirectly(Sound sound) {
        return sm.removeAndStopDirectly(sound);
    }

    @Override
    public boolean isSoundEnabled() {
        return sm.isSoundEnabled();
    }

    @Override
    public void setSoundEnabled(boolean enabled) {
        sm.setSoundEnabled(enabled);
    }

    @Override
    public float getVolume() {
        return sm.getVolume();
    }

    @Override
    public void setVolume(float volume) {
        sm.setVolume(volume);
    }
    
}
