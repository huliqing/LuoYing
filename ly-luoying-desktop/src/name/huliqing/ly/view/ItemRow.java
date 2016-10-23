/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view;

import name.huliqing.luoying.constants.InterfaceConstants;
import name.huliqing.luoying.data.ObjectData;
import name.huliqing.luoying.manager.ResourceManager;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.tiles.ColumnBody;
import name.huliqing.luoying.ui.tiles.ColumnIcon;
import name.huliqing.luoying.ui.tiles.ColumnText;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * 显示物品的数据行
 * @author huliqing
 * @param <T>
 */
public class ItemRow<T extends DataProcessor<ObjectData>> extends Row<T> {

    protected T data;
    
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
    public void displayRow(T dd) {
        data = dd;
        icon.setIcon(data.getData().getIcon());
        body.setNameText(ResourceManager.get(data.getData().getId() + ".name"));
        body.setDesText(ResourceManager.getObjectDes(data.getData().getId()));
        
        num.setText(data.getData().getTotal() + "");
    }
    
    /**
     * 获取当前行所显示的ProtoData
     * @return 
     */
    public T getData() {
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
