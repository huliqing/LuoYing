/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.game.view.transfer;

import java.util.List;
import name.huliqing.core.data.ProtoData;
import name.huliqing.ly.game.view.tiles.ItemList;
import name.huliqing.ly.game.view.tiles.ItemPanel;
import name.huliqing.core.ui.Row;

/**
 *
 * @author huliqing
 */
public class SimpleTransferPanel extends TransferPanel {
    private ItemPanel itemPanel;
    
    public SimpleTransferPanel(float width, float height) {
        super(width, height);
        
        itemPanel = new ItemPanel(width, height, getDatas());
        itemPanel.setRowClickListener(new ItemList.RowClickListener() {
            @Override
            public void onClick(Row row, boolean isPressed, ProtoData data) {
                if (isPressed) return;
                transfer(data);
            }
        });
        addView(itemPanel);
    }

    @Override
    public void setDatas(List<ProtoData> datas) {
        super.setDatas(datas); 
        itemPanel.refresh();
    }
    
    @Override
    public void onAdded(Transfer transfer, ProtoData data, int count) {
        itemPanel.refresh();
    }

    @Override
    public void onRemoved(Transfer transfer, ProtoData data, int count) {
        itemPanel.refresh();
    }
    
}
