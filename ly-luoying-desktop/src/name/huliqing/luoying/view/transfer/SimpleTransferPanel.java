/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.view.transfer;

import java.util.List;
import name.huliqing.luoying.data.ObjectData;
import name.huliqing.luoying.view.ItemPanel;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.view.ItemList.RowClickListener;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * @author huliqing
 */
public class SimpleTransferPanel extends TransferPanel<DataProcessor<ObjectData>> {
    private ItemPanel itemPanel;
    
    public SimpleTransferPanel(float width, float height) {
        super(width, height);
        
        itemPanel = new ItemPanel(width, height, getDatas());
        itemPanel.setRowClickListener(new RowClickListener<DataProcessor<ObjectData>>() {
            @Override
            public void onClick(Row row, boolean isPressed, DataProcessor<ObjectData> data) {
                if (isPressed) return;
                transfer(data);
            }
        });
        addView(itemPanel);
    }

    @Override
    public void setDatas(List<DataProcessor<ObjectData>> datas) {
        super.setDatas(datas); 
        itemPanel.refresh();
    }
    
    @Override
    public void onAdded(Transfer<DataProcessor<ObjectData>> transfer, DataProcessor<ObjectData> data, int count) {
        itemPanel.refresh();
    }

    @Override
    public void onRemoved(Transfer<DataProcessor<ObjectData>> transfer, DataProcessor<ObjectData> data, int count) {
        itemPanel.refresh();
    }
    
}
