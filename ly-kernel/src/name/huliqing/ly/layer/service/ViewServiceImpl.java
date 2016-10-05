/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import name.huliqing.ly.data.ViewData;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.xml.DataFactory;
import name.huliqing.ly.object.view.View;

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
