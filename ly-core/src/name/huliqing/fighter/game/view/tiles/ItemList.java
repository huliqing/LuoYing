/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.view.tiles;

import java.util.List;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.ui.ListView;
import name.huliqing.fighter.ui.Row;
import name.huliqing.fighter.ui.UI;
import name.huliqing.fighter.ui.UI.Listener;

/**
 * 显示物品列表
 * @author huliqing
 */
public class ItemList extends ListView<ProtoData> implements Listener {
    
    public interface RowClickListener {
        void onClick(Row row, boolean isPressed, ProtoData data);
    }
    
    private List<ProtoData> datas;
    private RowClickListener rowClickListener;
    
    public ItemList(float width, float height, List<ProtoData> datas) {
        super(width, height);
        this.datas = datas;
        setPageSize(6);
    }

    public void setRowClickListener(RowClickListener rowClickListener) {
        this.rowClickListener = rowClickListener;
    }
    
    @Override
    protected final Row<ProtoData> createEmptyRow() {
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
    public List<ProtoData> getDatas() {
        return datas;
    }
    
    public float[] getColumnsWidth() {
        ItemRow row = (ItemRow) rows.get(0);
        return row.getColumnsWidth();
    }
}
