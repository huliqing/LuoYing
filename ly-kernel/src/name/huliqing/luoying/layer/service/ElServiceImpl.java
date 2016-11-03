/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.el.ElFactory;
import name.huliqing.luoying.object.el.STBooleanEl;
import name.huliqing.luoying.object.el.STNumberEl;
import name.huliqing.luoying.object.el.LNumberEl;
import name.huliqing.luoying.object.el.SBooleanEl;

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
    public SBooleanEl createSBooleanEl(String idOrExpression) {
        if (ElFactory.isElExpression(idOrExpression)) {
            SBooleanEl el = Loader.load(IdConstants.SYS_EL_SBOOLEAN);
            el.setExpression(idOrExpression);
            return el;
        } else {
            return Loader.load(idOrExpression);
        }
    }
    
    @Override
    public STBooleanEl createSTBooleanEl(String idOrExpression) {
        if (ElFactory.isElExpression(idOrExpression)) {
            STBooleanEl el = Loader.load(IdConstants.SYS_EL_STBOOLEAN);
            el.setExpression(idOrExpression);
            return el;
        } else {
            return Loader.load(idOrExpression);
        }
    }

    @Override
    public STNumberEl createSTNumberEl(String idOrExpression) {
        if (ElFactory.isElExpression(idOrExpression)) {
            STNumberEl el = Loader.load(IdConstants.SYS_EL_STNUMBER);
            el.setExpression(idOrExpression);
            return el;
        } else {
            return Loader.load(idOrExpression);
        }
    }

    @Override
    public LNumberEl createLNumberEl(String idOrExpression) {
        if (ElFactory.isElExpression(idOrExpression)) {
            LNumberEl el = Loader.load(IdConstants.SYS_EL_LNUMBER);
            el.setExpression(idOrExpression);
            return el;
        } else {
            return Loader.load(idOrExpression);
        }
    }

}
