/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.system;

import com.jme3.font.BitmapFont;
import java.util.List;
import name.huliqing.ly.ui.ListView;
import name.huliqing.ly.ui.Row;
import name.huliqing.ly.ui.Text;
import name.huliqing.ly.ui.UIFactory;

/**
 *
 * @author huliqing
 */
public class RowSimple extends Row<SystemData> {

    private Text rowName;
    private Text rowDes;
    
    public RowSimple(ListView listView, String name, String des) {
        super(listView);
        this.rowName = new Text(name);
        this.rowName.setFontSize(UIFactory.getUIConfig().getBodyFontSize());
        this.rowDes = new Text(des);
        this.rowDes.setFontSize(UIFactory.getUIConfig().getDesSize());
        this.rowDes.setFontColor(UIFactory.getUIConfig().getDesColor());
        
        addView(this.rowName);
        addView(this.rowDes);
        
        setBackground(UIFactory.getUIConfig().getBackground(), true);
        setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
        setBackgroundVisible(false);
    }

    public void setRowName(String rowName) {
        this.rowName.setText(rowName);
    }

    public void setRowDes(String rowDes) {
        this.rowDes.setText(rowDes);
    }

    @Override
    public void updateViewChildren() {
        super.updateViewChildren(); 
        float mw = 5;
        float rowW = width - mw * 2;
        float rowH = height * 0.5f;
        
        this.rowName.setWidth(rowW);
        this.rowName.setHeight(rowH);
        this.rowName.setMargin(mw, 0, 0, 0);
        this.rowName.setAlignment(BitmapFont.Align.Left);
        this.rowName.setVerticalAlignment(BitmapFont.VAlign.Center);
        
        this.rowDes.setWidth(rowW);
        this.rowDes.setHeight(rowH);
        this.rowDes.setMargin(mw, 0, 0, 0);
        this.rowDes.setAlignment(BitmapFont.Align.Left);
        this.rowDes.setVerticalAlignment(BitmapFont.VAlign.Center);
    }
    
    @Override
    protected void clickEffect(boolean isPress) {
        if (isPress) {
            setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
        }
        setBackgroundVisible(isPress);
    }

    @Override
    public void onRelease() {
        setBackgroundVisible(false);
    }
    
    @Override
    public void displayRow(SystemData data) {
        // ignore
    }
    
    
    
}
