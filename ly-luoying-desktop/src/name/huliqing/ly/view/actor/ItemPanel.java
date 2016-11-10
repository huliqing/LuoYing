/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.actor;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.manager.ResourceManager;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.UI;
import name.huliqing.ly.layer.service.GameService;

/**
 * 
 * @author huliqing
 */
public class ItemPanel extends ListView<ItemData> implements ActorPanel {
    private final GameService gameService = Factory.get(GameService.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    
    private Entity actor;
    private final List<ItemData> datas = new ArrayList<ItemData>();
    
    public ItemPanel(float width, float height) {
        super(width, height);
    }

    @Override
    public void refreshPageData() {
        if (actor != null) {
            datas.clear();
            actor.getData().getObjectDatas(ItemData.class, datas);
        }
        super.refreshPageData();
    }
    
    @Override
    protected Row<ItemData> createEmptyRow() {
        final ItemRow row = new ItemRow();
        row.setRowClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    entityNetwork.useData(actor, row.getData());
                    refreshPageData();
                }
            }
        });
        row.setShortcutListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    gameService.addShortcut(actor, row.getData());
                }
            }
        });
        return row;
    }

    @Override
    public List<ItemData> getDatas() {
        if (actor != null) {
            datas.clear();
            actor.getData().getObjectDatas(ItemData.class, datas);
        }
        return datas;
    }
    
    @Override
    public void setPanelVisible(boolean visible) {
        this.setVisible(visible);
    }
    
    @Override
    public void setPanelUpdate(Entity actor) {
        this.actor = actor;
        refreshPageData();
    }
    
    private class ItemRow extends name.huliqing.ly.view.actor.ItemRow<ItemData> {

        public ItemRow() {
            super();
        }
        
        @Override
        public void display(ItemData data) {
            icon.setIcon(data.getIcon());
            body.setNameText(ResourceManager.get(data.getId() + ".name"));
            body.setDesText(ResourceManager.get(data.getId() + ".des"));
            num.setText(data.getTotal() + "");
        }
    }
}
