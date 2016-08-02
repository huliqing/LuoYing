/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.service;

import name.huliqing.core.Inject;
import name.huliqing.core.data.MagicData;
import name.huliqing.core.object.magic.Magic;

/**
 *
 * @author huliqing
 */
public interface MagicService extends Inject {
    
//    /**
//     * 载入一个魔法
//     * @param magicId
//     * @return 
//     */
//    Magic loadMagic(String magicId);
    
    MagicData loadMagic(String magicId);
    
    Magic loadMagic(MagicData magicData);
}
