/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.data.MagicData;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.object.DataFactory;
import name.huliqing.fighter.object.magic.Magic;

/**
 *
 * @author huliqing
 */
public class MagicServiceImpl implements MagicService {

    @Override
    public void inject() {
        // ignore
    }
    
    @Override
    public MagicData loadMagic(String magicId) {
        return DataFactory.createData(magicId);
    }

    @Override
    public Magic loadMagic(MagicData magicData) {
        return DataFactory.createProcessor(magicData);
    }
 
}
