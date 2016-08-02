/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ViewData;
import name.huliqing.core.game.service.PlayService;
import name.huliqing.core.game.service.ViewService;
import name.huliqing.core.object.view.View;

/**
 * 告诉客户端添加一个View组件到界面
 * @author huliqing
 */
@Serializable
public class MessViewAdd extends MessBase {
    
    private ViewData viewData;

    public MessViewAdd() {}
    
    public ViewData getViewData() {
        return viewData;
    }

    public void setViewData(ViewData viewData) {
        this.viewData = viewData;
    }
    
    @Override
    public void applyOnClient() {
        PlayService ps = Factory.get(PlayService.class);
        ViewService vs = Factory.get(ViewService.class);
        View view = vs.loadView(viewData);
        ps.addView(view);
    }
   
}
