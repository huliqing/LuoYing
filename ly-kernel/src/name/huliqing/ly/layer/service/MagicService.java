/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import name.huliqing.ly.Inject;
import name.huliqing.ly.data.MagicData;
import name.huliqing.ly.object.magic.Magic;

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
