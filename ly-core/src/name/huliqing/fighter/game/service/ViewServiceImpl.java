/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.data.ViewData;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.object.DataFactory;
import name.huliqing.fighter.object.view.View;

/**
 *
 * @author huliqing
 */
public class ViewServiceImpl implements ViewService {

    @Override
    public void inject() {
        // ignore
    }

    @Override
    public View loadView(String viewId) {
        return loadView((ViewData) DataFactory.createData(viewId));
    }

    @Override
    public View loadView(ViewData data) {
        return Loader.loadView(data);
    }
    
}
