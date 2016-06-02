/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.ViewData;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.ViewService;
import name.huliqing.fighter.object.view.View;

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
