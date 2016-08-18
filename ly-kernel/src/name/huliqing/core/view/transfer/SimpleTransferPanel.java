/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view.transfer;

import java.util.List;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.view.ItemList;
import name.huliqing.core.view.ItemPanel;
import name.huliqing.core.ui.Row;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class SimpleTransferPanel<T extends ObjectData> extends TransferPanel<T> {
    private ItemPanel itemPanel;
    
    public SimpleTransferPanel(float width, float height) {
        super(width, height);
        
        itemPanel = new ItemPanel(width, height, getDatas());
        itemPanel.setRowClickListener(new ItemList.RowClickListener<T>() {
            @Override
            public void onClick(Row row, boolean isPressed, T data) {
                if (isPressed) return;
                transfer(data);
            }
        });
        addView(itemPanel);
    }

    @Override
    public void setDatas(List<T> datas) {
        super.setDatas(datas); 
        itemPanel.refresh();
    }
    
    @Override
    public void onAdded(Transfer<T> transfer, T data, int count) {
        itemPanel.refresh();
    }

    @Override
    public void onRemoved(Transfer<T> transfer, T data, int count) {
        itemPanel.refresh();
    }
    
}
