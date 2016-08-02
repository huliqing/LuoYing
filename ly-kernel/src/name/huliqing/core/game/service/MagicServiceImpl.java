/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.service;

import name.huliqing.core.data.MagicData;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.object.DataFactory;
import name.huliqing.core.object.magic.Magic;

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
