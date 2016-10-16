/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.data.MagicData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.object.magic.Magic;

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
