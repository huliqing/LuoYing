/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.view.actor;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.game.network.UserCommandNetwork;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.ui.ListView;
import name.huliqing.fighter.ui.Row;
import name.huliqing.fighter.ui.UI;

/**
 * 
 * @author huliqing
 */
public class ItemPanel extends ListView<ProtoData> implements ActorPanel {
    private PlayService playService = Factory.get(PlayService.class);
    private final UserCommandNetwork userCommandNetwork = Factory.get(UserCommandNetwork.class);
    
    private Actor actor;
    private List<ProtoData> datas = new ArrayList<ProtoData>();
    
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
    protected Row<ProtoData> createEmptyRow() {
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
    public List<ProtoData> getDatas() {
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
    
    private class ItemRow extends name.huliqing.fighter.game.view.actor.ItemRow<ProtoData> {

        public ItemRow() {
            super();
        }
        
        @Override
        public void display(ProtoData data) {
            icon.setIcon(data.getProto().getIcon());
            body.setNameText(ResourceManager.get(data.getProto().getId() + ".name"));
            body.setDesText(ResourceManager.get(data.getProto().getId() + ".des"));
            num.setText(data.getTotal() + "");
        }
    }
}
