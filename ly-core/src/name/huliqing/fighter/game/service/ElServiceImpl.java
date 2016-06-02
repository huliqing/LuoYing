/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.object.el.HitEl;
import name.huliqing.fighter.object.el.LevelEl;
import name.huliqing.fighter.object.el.XpDropEl;

/**
 *
 * @author huliqing
 */
public class ElServiceImpl implements ElService {
    
    @Override
    public void inject() {
        // ignore
    }

    @Override
    public float getLevelEl(String levelElId, int level) {
        LevelEl el = (LevelEl) Loader.loadEl(levelElId);
        return (float) el.getValue(level);
    }

    @Override
    public XpDropEl getXpDropEl(String id) {
        return (XpDropEl) Loader.loadEl(id);
    }
    
    @Override
    public HitEl getHitEl(String id) {
        return (HitEl) Loader.loadEl(id);
    }

}
