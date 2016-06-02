/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.Inject;
import name.huliqing.fighter.object.hitchecker.HitChecker;

/**
 *
 * @author huliqing
 */
public interface HitCheckerService extends Inject {
    
    /**
     * 载入一个hitChecker
     * @param hitCheckerId
     * @return 
     */
    HitChecker loadHitChecker(String hitCheckerId);
    
}
