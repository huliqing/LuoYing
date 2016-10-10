/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.position;

import com.jme3.math.Vector3f;
import name.huliqing.ly.Factory;
import name.huliqing.ly.Ly;
import name.huliqing.ly.data.PositionData;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.ui.UI.Corner;

/**
 * GUI界面上的位置点
 * @author huliqing
 * @param <T>
 */
public class ViewPosition<T extends PositionData> extends AbstractPosition<T> {
    private final PlayService playService = Factory.get(PlayService.class);
    
    // 在视图界面上的角落位置
    private Corner corner;
    // left,top,right,bottom. 权重取值0.0~1.0
    private float[] marginWeight;

    @Override
    public void setData(T data) {
        super.setData(data);
        corner = Corner.identify(data.getAsString("corner"));
        marginWeight = data.getAsFloatArray("marginWeight");
    }

    @Override
    public Vector3f getPoint(Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        
        float x = 0;
        float y = 0;
        float sw = Ly.getSettings().getWidth();
        float sh = Ly.getSettings().getHeight();
        
        float marginLeft = 0;
        float marginTop = 0;
        float marginRight = 0;
        float marginBottom = 0;
        if (marginWeight != null) {
            marginLeft = marginWeight[0] * sw;
            marginTop = marginWeight[1] * sh;
            marginRight = marginWeight[2] * sw;
            marginBottom = marginWeight[3] * sh;
        }
        
        // center
        if (corner == Corner.CB) {
            x = sw * 0.5f;
            x += marginLeft;
            x -= marginRight;
            y -= marginTop;
            y += marginBottom;
        } else if (corner == Corner.CC) {
            x = sw * 0.5f;
            x += marginLeft;
            x -= marginRight;
            y = sh * 0.5f;
            y -= marginTop;
            y += marginBottom;
        } else if (corner == Corner.CT) {
            x = sw * 0.5f;
            x += marginLeft;
            x -= marginRight;
            y = sh;
            y -= marginTop;
            y += marginBottom;
        }
        // left 
        else if (corner  == Corner.LB) {
            x = y = 0;
            x += marginLeft;
            x -= marginRight;
            y -= marginTop;
            y += marginBottom;
        } else if (corner == Corner.LC) {
            x = 0;
            x += marginLeft;
            x -= marginRight;
            y = sh * 0.5f;
            y -= marginTop;
            y += marginBottom;
        } else if (corner == Corner.LT) {
            x = 0;
            x += marginLeft;
            x -= marginRight;
            y = sh;
            y -= marginTop;
            y += marginBottom;
        }
        // right
        else if (corner == Corner.RB) {
            x = sw;
            x += marginLeft;
            x -= marginRight;
            y = 0;
            y -= marginTop;
            y += marginBottom;
        } else if (corner == Corner.RC) {
            x = sw;
            x += marginLeft;
            x -= marginRight;
            y = sh * 0.5f;
            y -= marginTop;
            y += marginBottom;
        } else if (corner == Corner.RT) {
            x = sw;
            x += marginLeft;
            x -= marginRight;
            y = sh;
            y -= marginTop;
            y += marginBottom;
        }
        
        store.setX(x);
        store.setY(y);
        
        return store;
    }
    
}
