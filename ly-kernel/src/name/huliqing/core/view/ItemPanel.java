/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view;

import java.util.List;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.ui.LinearLayout;
import name.huliqing.core.ui.UIFactory;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class ItemPanel<T extends ObjectData> extends LinearLayout {
    private ItemTitle title;
    private ItemList itemList;
        
    public ItemPanel(float width, float height, List<T> datas) {
        super(width, height);
        float titleHeight = UIFactory.getUIConfig().getListTitleHeight();
        title = new ItemTitle(width, titleHeight);
        itemList = new ItemList(width, height - titleHeight, datas);
        addView(title);
        addView(itemList);
    }
        
    public void setRowClickListener(ItemList.RowClickListener rcl) {
        itemList.setRowClickListener(rcl);
    }

    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        float titleHeight = UIFactory.getUIConfig().getListTitleHeight();
        itemList.setWidth(width);
        itemList.setHeight(height - titleHeight);
        itemList.updateView();
        title.setWidth(width);
        title.setHeight(titleHeight);
        title.setColumnsWidth(itemList.getColumnsWidth());
    }
    
    public void refresh() {
        itemList.refreshPageData();
    }
}
