/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view;

import java.util.List;
import name.huliqing.core.constants.InterfaceConstants;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.data.AttributeApply;
import name.huliqing.core.xml.ProtoData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.ui.ListView;
import name.huliqing.core.ui.Row;
import name.huliqing.core.ui.UIFactory;
import name.huliqing.core.ui.tiles.ColumnBody;
import name.huliqing.core.ui.tiles.ColumnIcon;
import name.huliqing.core.ui.tiles.ColumnText;

/**
 * 显示物品的数据行
 * @author huliqing
 */
public class ItemRow extends Row<ProtoData> {

    protected ProtoData data;
    
    // 物品
    protected ColumnIcon icon;
    protected ColumnBody body;
    protected ColumnText num;
    
    private float[] columnsWidth;
    
    public ItemRow(ListView parentView) {
        super(parentView);
        this.setLayout(Layout.horizontal);
        icon = new ColumnIcon(height, height, InterfaceConstants.UI_MISS);
        body = new ColumnBody(height, height, "", "");
        num = new ColumnText(height, height, "");
        addView(icon);
        addView(body);
        addView(num);
        
        
        setBackground(UIFactory.getUIConfig().getBackground(), true);
        setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
        setBackgroundVisible(false);
    }
    
    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        float iconSize = height;

        icon.setWidth(iconSize);
        icon.setHeight(iconSize);

        num.setWidth(iconSize);
        num.setHeight(iconSize);

        body.setWidth(width - iconSize * 2);
        body.setHeight(iconSize);
        
    }
    
    public float[] getColumnsWidth() {
        if (columnsWidth == null) {
            columnsWidth = new float[3];
        }
        columnsWidth[0] = icon.getWidth();
        columnsWidth[1] = body.getWidth();
        columnsWidth[2] = num.getWidth();
        return columnsWidth;
    }

    @Override
    public void displayRow(ProtoData dd) {
        data = dd;
        icon.setIcon(data.getIcon());
        body.setNameText(ResourceManager.get(data.getId() + ".name"));
        
        if (data instanceof SkinData) {
            body.setDesText(data.getDes());
        } else {
            body.setDesText(ResourceManager.getObjectDes(data.getId()));
        }
        
        num.setText(data.getTotal() + "");
    }
    
    /**
     * 获取当前行所显示的ProtoData
     * @return 
     */
    public ProtoData getData() {
        return data;
    }
    
    @Override
    protected void clickEffect(boolean isPress) {
        if (isPress) {
            this.setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
        }
        setBackgroundVisible(isPress);
    }

    @Override
    public void onRelease() {
        setBackgroundVisible(false);
    }
    
}
