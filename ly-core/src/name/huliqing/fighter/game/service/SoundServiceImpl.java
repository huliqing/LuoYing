/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.data.SoundData;
import name.huliqing.fighter.object.DataFactory;

/**
 *
 * @author huliqing
 */
public class SoundServiceImpl implements SoundService {

    @Override
    public void inject() {
    }
    
    @Override
    public SoundData loadSoundData(String soundId) {
        SoundData sd = DataFactory.createData(soundId);
        return sd;
    }

    
}
