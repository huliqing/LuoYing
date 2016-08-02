/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.service;

import name.huliqing.core.Inject;
import name.huliqing.core.object.hitchecker.HitChecker;

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
