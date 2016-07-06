/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.Inject;
import name.huliqing.fighter.data.SoundData;

/**
 *
 * @author huliqing
 */
public interface SoundService extends Inject {
    
    SoundData loadSoundData(String soundId);
    
}
