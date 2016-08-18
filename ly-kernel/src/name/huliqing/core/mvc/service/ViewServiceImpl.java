/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import name.huliqing.core.data.ViewData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.object.view.View;

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
