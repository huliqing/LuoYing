/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.view.tiles;

import com.jme3.font.BitmapFont;
import name.huliqing.fighter.constants.ResConstants;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.ui.Text;
import name.huliqing.fighter.ui.UIFactory;

/**
 *
 * @author huliqing
 */
public class ItemTitle extends Title {

    private Text icon;
    private Text name;
    private Text total;
    
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
