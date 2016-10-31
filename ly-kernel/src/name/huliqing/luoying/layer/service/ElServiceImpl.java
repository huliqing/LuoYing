/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.el.ElFactory;
import name.huliqing.luoying.object.el.HitCheckEl;
import name.huliqing.luoying.object.el.HitEl;
import name.huliqing.luoying.object.el.LevelEl;
import name.huliqing.luoying.object.el.CheckEl;

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
    public CheckEl createCheckEl(String idOrExpression) {
        if (ElFactory.isElExpression(idOrExpression)) {
            CheckEl el = Loader.load(IdConstants.SYS_EL_CHECK);
            el.setExpression(idOrExpression);
            return el;
        } else {
            return Loader.load(idOrExpression);
        }
    }
    
    @Override
    public HitCheckEl createHitCheckEl(String idOrExpression) {
        if (ElFactory.isElExpression(idOrExpression)) {
            HitCheckEl el = Loader.load(IdConstants.SYS_EL_HIT_CHECK_EMPTY);
            el.setExpression(idOrExpression);
            return el;
        } else {
            return Loader.load(idOrExpression);
        }
    }

    @Override
    public HitEl createHitEl(String idOrExpression) {
        if (ElFactory.isElExpression(idOrExpression)) {
            HitEl el = Loader.load(IdConstants.SYS_EL_HIT_EMPTY);
            el.setExpression(idOrExpression);
            return el;
        } else {
            return Loader.load(idOrExpression);
        }
    }

    @Override
    public LevelEl createLevelEl(String idOrExpression) {
        if (ElFactory.isElExpression(idOrExpression)) {
            LevelEl el = Loader.load(IdConstants.SYS_EL_LEVEL_EMPTY);
            el.setExpression(idOrExpression);
            return el;
        } else {
            return Loader.load(idOrExpression);
        }
    }

}
