/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.Inject;
import name.huliqing.fighter.data.MagicData;
import name.huliqing.fighter.object.magic.Magic;

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
