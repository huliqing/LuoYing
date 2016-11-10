/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.transfer;

import java.util.List;
import name.huliqing.ly.view.ItemPanel;
import name.huliqing.luoying.ui.Row;
import name.huliqing.ly.view.ItemList.RowClickListener;
import name.huliqing.luoying.transfer.Transfer;
import name.huliqing.luoying.transfer.TransferData;

/**
 * @author huliqing
 */
public class SimpleTransferPanel extends TransferPanel {
    private ItemPanel itemPanel;
    
    public SimpleTransferPanel(float width, float height) {
        super(width, height);
        
        itemPanel = new ItemPanel(width, height, getDatas());
        itemPanel.setRowClickListener(new RowClickListener<TransferData>() {
            @Override
            public void onClick(Row row, boolean isPressed, TransferData data) {
                if (isPressed) return;
                transfer(data);
            }
        });
        addView(itemPanel);
    }

    @Override
    public void setDatas(List<TransferData> datas) {
        super.setDatas(datas); 
        itemPanel.refresh();
    }
    
    @Override
    public void onAdded(Transfer transfer, TransferData data, int count) {
        itemPanel.refresh();
    }

    @Override
    public void onRemoved(Transfer transfer, TransferData data, int count) {
        itemPanel.refresh();
    }
    
}
