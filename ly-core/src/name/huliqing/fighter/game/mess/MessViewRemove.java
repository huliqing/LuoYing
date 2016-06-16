/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.object.view.View;

/**
 * @author huliqing
 */
@Serializable
public class MessViewRemove extends MessBase {
    
    // View的唯一id
    private long viewId;

    public MessViewRemove() {}

    public long getViewId() {
        return viewId;
    }

    public void setViewId(long viewId) {
        this.viewId = viewId;
    }
    
    @Override
    public void applyOnClient() {
        PlayService ps = Factory.get(PlayService.class);
        View view = ps.findView(viewId);
        if (view != null) {
            ps.removeObject(view);
        }
    }
   
}
