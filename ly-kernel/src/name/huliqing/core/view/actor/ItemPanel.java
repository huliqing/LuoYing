/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view.actor;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.mvc.network.ItemNetwork;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.mvc.service.ItemService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.object.item.Item;
import name.huliqing.core.ui.ListView;
import name.huliqing.core.ui.Row;
import name.huliqing.core.ui.UI;

/**
 * 
 * @author huliqing
 */
public class ItemPanel extends ListView<Item> implements ActorPanel {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ItemService itemService = Factory.get(ItemService.class);
    private final ItemNetwork itemNetwork = Factory.get(ItemNetwork.class);
    
    private Actor actor;
    private final List<Item> datas = new ArrayList<Item>();
    
    public ItemPanel(float width, float height) {
        super(width, height);
    }

    @Override
    public void refreshPageData() {
        if (actor != null) {
//            actor.getData().getItemStore().getOthers(datas);
            datas.clear();
            datas.addAll(itemService.getItems(actor));
        }
        super.refreshPageData();
    }
    
    @Override
    protected Row<Item> createEmptyRow() {
        final ItemRow row = new ItemRow();
        row.setRowClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    itemNetwork.useItem(actor, row.getData().getId());
                    refreshPageData();
                }
            }
        });
        row.setShortcutListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    playService.addShortcut(actor, row.getData().getData());
                }
            }
        });
        return row;
    }

    @Override
    public List<Item> getDatas() {
        if (actor != null) {
            datas.clear();
            datas.addAll(itemService.getItems(actor));
        }
        return datas;
    }
    
    @Override
    public void setPanelVisible(boolean visible) {
        this.setVisible(visible);
    }
    
    @Override
    public void setPanelUpdate(Actor actor) {
        this.actor = actor;
        refreshPageData();
    }
    
    private class ItemRow extends name.huliqing.core.view.actor.ItemRow<Item> {

        public ItemRow() {
            super();
        }
        
        @Override
        public void display(Item data) {
            icon.setIcon(data.getData().getIcon());
            body.setNameText(ResourceManager.get(data.getId() + ".name"));
            body.setDesText(ResourceManager.get(data.getId() + ".des"));
            num.setText(data.getData().getTotal() + "");
        }
    }
}
