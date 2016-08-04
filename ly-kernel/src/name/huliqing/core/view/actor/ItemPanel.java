/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view.actor;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.mvc.network.UserCommandNetwork;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.ui.ListView;
import name.huliqing.core.ui.Row;
import name.huliqing.core.ui.UI;

/**
 * 
 * @author huliqing
 */
public class ItemPanel extends ListView<ObjectData> implements ActorPanel {
    private PlayService playService = Factory.get(PlayService.class);
    private final UserCommandNetwork userCommandNetwork = Factory.get(UserCommandNetwork.class);
    
    private Actor actor;
    private List<ObjectData> datas = new ArrayList<ObjectData>();
    
    public ItemPanel(float width, float height) {
        super(width, height);
    }

    @Override
    public void refreshPageData() {
        if (actor != null) {
            actor.getData().getItemStore().getOthers(datas);
        }
        super.refreshPageData();
    }
    
    @Override
    protected Row<ObjectData> createEmptyRow() {
        final ItemRow row = new ItemRow();
        row.setRowClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    userCommandNetwork.useObject(actor, row.getData());
                    refreshPageData();
                }
            }
        });
        row.setShortcutListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    playService.addShortcut(actor, row.getData());
                }
            }
        });
        return row;
    }

    @Override
    public List<ObjectData> getDatas() {
        if (actor != null) {
            datas.clear();
            return actor.getData().getItemStore().getOthers(datas);
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
    
    private class ItemRow extends name.huliqing.core.view.actor.ItemRow<ObjectData> {

        public ItemRow() {
            super();
        }
        
        @Override
        public void display(ObjectData data) {
            icon.setIcon(data.getIcon());
            body.setNameText(ResourceManager.get(data.getId() + ".name"));
            body.setDesText(ResourceManager.get(data.getId() + ".des"));
            num.setText(data.getTotal() + "");
        }
    }
}
