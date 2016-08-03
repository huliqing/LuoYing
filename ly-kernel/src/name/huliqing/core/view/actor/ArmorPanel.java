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
import name.huliqing.core.data.ProtoData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.mvc.network.UserCommandNetwork;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.SkinService;
import name.huliqing.core.ui.ListView;
import name.huliqing.core.ui.Row;
import name.huliqing.core.ui.UI;

/**
 *
 * @author huliqing
 */
public class ArmorPanel extends ListView<SkinData> implements ActorPanel{
    private final UserCommandNetwork userCommandNetwork = Factory
            .get(UserCommandNetwork.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final SkinService skinService = Factory.get(SkinService.class);
    private Actor actor;
    private List<SkinData> datas = new ArrayList<SkinData>();
    
    public ArmorPanel(float width, float height) {
        super(width, height);
    }
    
    @Override
    protected Row createEmptyRow() {
        final ArmorRow row = new ArmorRow();
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
    public void setPanelVisible(boolean visible) {
        this.setVisible(visible);
    }

    @Override
    public void setPanelUpdate(Actor actor) {
        this.actor = actor;
        getDatas();
        super.refreshPageData();
    }

    @Override
    public List<SkinData> getDatas() {
        if (actor != null) {
            datas.clear();
            
            return skinService.getArmorSkins(actor, datas);
        }
        return datas;
    }

    @Override
    public boolean removeItem(SkinData data) {
        throw new UnsupportedOperationException();
    }
    
    private class ArmorRow extends ItemRow<ProtoData> {

        public ArmorRow() {
            super();
        }
        
        @Override
        protected void display(ProtoData data) {
            SkinData sd = (SkinData) data;
            
            icon.setIcon(sd.getIcon());
            body.setNameText(ResourceManager.getObjectName(sd));
            
            
            if (data instanceof SkinData) {
                body.setDesText(data.getDes());
            } else {
                body.setDesText(ResourceManager.getObjectDes(data.getId()));
            }
            
            num.setText(String.valueOf(sd.getTotal()));
            
            setBackgroundVisible(sd.isUsing());
        }

        @Override
        protected void clickEffect(boolean isPress) {
            super.clickEffect(isPress);
            setBackgroundVisible(((SkinData)data).isUsing());
        }
        
        @Override
        public void onRelease() {
            SkinData sd = ((SkinData)data);
            setBackgroundVisible(sd.isUsing());
        }
    }
    
}
