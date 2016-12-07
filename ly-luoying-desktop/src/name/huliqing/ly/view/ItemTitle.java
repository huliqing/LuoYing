/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view;

import com.jme3.font.BitmapFont;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UIFactory;

/**
 *
 * @author huliqing
 */
public class ItemTitle extends Title {

    private final Text icon;
    private final Text name;
    private final Text total;
    
    public ItemTitle(float width, float height) {
        super(width, height);
        icon = new Text("");
        icon.setHeight(height);
        icon.setFontSize(UIFactory.getUIConfig().getDesSize());
        icon.setFontColor(UIFactory.getUIConfig().getDesColor());
        icon.setVerticalAlignment(BitmapFont.VAlign.Center);
        
        name = new Text(ResourceManager.get(ResConstants.COMMON_NAME));
        name.setHeight(height);
        name.setFontSize(UIFactory.getUIConfig().getDesSize());
        name.setFontColor(UIFactory.getUIConfig().getDesColor());
        name.setVerticalAlignment(BitmapFont.VAlign.Center);
        
        total = new Text(ResourceManager.get(ResConstants.COMMON_TOTAL));
        total.setHeight(height);
        total.setFontSize(UIFactory.getUIConfig().getDesSize());
        total.setFontColor(UIFactory.getUIConfig().getDesColor());
        total.setVerticalAlignment(BitmapFont.VAlign.Center);
        addView(icon);
        addView(name);
        addView(total);
    }
    
    public void setColumnsWidth(float[] widths) {
        icon.setWidth(widths[0]);
        name.setWidth(widths[1]);
        total.setWidth(widths[2]);
        setNeedUpdate();
    }
    
}
