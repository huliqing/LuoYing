/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

import com.jme3.math.Ray;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author huliqing
 */
public class LocationObj extends AbstractActionObj {

    private static final Logger LOG = Logger.getLogger(LocationObj.class.getName());
    
    private final LocationAxis tileCoord;
    
    public LocationObj() {
        tileCoord = new LocationAxis();
        tileCoord.addControl(new AutoScaleControl());
        attachChild(tileCoord);
    }

    @Override
    public void setCullHint(CullHint hint) {
        super.setCullHint(hint);
        // 优化：当物体激活时，强制刷新一次，计算大小。
        tileCoord.getControl(AutoScaleControl.class).forceUpdate();
    }

    @Override
    protected void doAction(Ray ray) {
        LOG.log(Level.INFO, "doAction location");
        
    }


    
    
    
}
