/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader.data;

import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.data.ViewData;

/**
 *
 * @author huliqing
 */
public class ViewDataLoader implements DataLoader<ViewData>{

    @Override
    public ViewData loadData(Proto proto) {
        ViewData data = new ViewData(proto.getId());
        return data;
    }
    
}
