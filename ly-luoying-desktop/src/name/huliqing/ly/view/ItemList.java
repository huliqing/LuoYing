/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view;

import java.util.List;
import name.huliqing.luoying.transfer.TransferData;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UI.Listener;

/**
 * 显示物品列表
 * @author huliqing
 */
public class ItemList extends ListView<TransferData> implements Listener {
    
    public interface RowClickListener {
        void onClick(Row row, boolean isPressed, TransferData data);
    }
    
    private final List<TransferData> datas;
    private RowClickListener rowClickListener;
    
    public ItemList(float width, float height, List<TransferData> datas) {
        super(width, height);
        this.datas = datas;
        setPageSize(6);
    }

    public void setRowClickListener(RowClickListener rowClickListener) {
        this.rowClickListener = rowClickListener;
    }
    
    @Override
    protected final Row<TransferData> createEmptyRow() {
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
    public List<TransferData> getDatas() {
        return datas;
    }
    
    public float[] getColumnsWidth() {
        ItemRow row = (ItemRow) rows.get(0);
        return row.getColumnsWidth();
    }
}
