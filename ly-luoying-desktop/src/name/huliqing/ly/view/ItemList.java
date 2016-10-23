/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view;

import java.util.List;
import name.huliqing.luoying.data.ObjectData;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UI.Listener;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * 显示物品列表
 * @author huliqing
 * @param <T>
 */
public class ItemList<T extends DataProcessor<ObjectData>> extends ListView<T> implements Listener {
    
    public interface RowClickListener<T> {
        void onClick(Row row, boolean isPressed, T data);
    }
    
    private final List<T> datas;
    private RowClickListener<DataProcessor<ObjectData>> rowClickListener;
    
    public ItemList(float width, float height, List<T> datas) {
        super(width, height);
        this.datas = datas;
        setPageSize(6);
    }

    public void setRowClickListener(RowClickListener rowClickListener) {
        this.rowClickListener = rowClickListener;
    }
    
    @Override
    protected final Row<T> createEmptyRow() {
        ItemRow row = new ItemRow(this);
        row.addClickListener(this);
        return row;
    }

    @Override
    public void onClick(UI ui, boolean isPressed) {
        if (rowClickListener != null) {
            ItemRow itemRow = (ItemRow) ui;
            rowClickListener.onClick(itemRow, isPressed, itemRow.getData());
        }
    }
    
    @Override
    public List<T> getDatas() {
        return datas;
    }
    
    public float[] getColumnsWidth() {
        ItemRow row = (ItemRow) rows.get(0);
        return row.getColumnsWidth();
    }
}
