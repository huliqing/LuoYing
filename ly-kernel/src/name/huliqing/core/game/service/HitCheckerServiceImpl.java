/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.service;

import name.huliqing.core.loader.Loader;
import name.huliqing.core.object.hitchecker.HitChecker;

/**
 *
 * @author huliqing
 */
public class HitCheckerServiceImpl implements HitCheckerService {

    @Override
    public void inject() {
        // ignore
    }

    @Override
    public HitChecker loadHitChecker(String hitCheckerId) {
        return Loader.loadHitChecker(hitCheckerId);
    }
    
}
