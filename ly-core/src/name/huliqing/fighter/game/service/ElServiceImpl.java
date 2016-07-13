/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import java.util.HashMap;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.object.el.El;
import name.huliqing.fighter.object.el.HitEl;
import name.huliqing.fighter.object.el.LevelEl;
import name.huliqing.fighter.object.el.XpDropEl;

/**
 *
 * @author huliqing
 */
public class ElServiceImpl implements ElService {
    
    // 缓存el处理器
    private final static HashMap<String, El> EL_MAP = new HashMap<String, El>();
    
    @Override
    public void inject() {
        // ignore
    }
    
    @Override
    public El getEl(String elId) {
        
        El el = EL_MAP.get(elId);
        if (el == null) {
            el = Loader.loadEl(elId);
            if (el == null) {
                throw new NullPointerException("Could not find el, elId=" + elId);
            }
            EL_MAP.put(elId, el);
        }
        return el;
    }

    @Override
    public float getLevelEl(String levelElId, int level) {
        LevelEl el = (LevelEl) getEl(levelElId);
        return (float) el.getValue(level);
    }

    @Override
    public XpDropEl getXpDropEl(String id) {
        return (XpDropEl) getEl(id);
    }
    
    @Override
    public HitEl getHitEl(String id) {
        return (HitEl) getEl(id);
    }

}
