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
import name.huliqing.core.data.SkinData;
import name.huliqing.core.mvc.network.SkinNetwork;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.SkinService;
import name.huliqing.core.object.skin.Skin;
import name.huliqing.core.ui.ListView;
import name.huliqing.core.ui.Row;
import name.huliqing.core.ui.UI;

/**
 *
 * @author huliqing
 */
public class ArmorPanel extends ListView<Skin> implements ActorPanel{
//    private final UserCommandNetwork userCommandNetwork = Factory
//            .get(UserCommandNetwork.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final SkinService skinService = Factory.get(SkinService.class);
    private final SkinNetwork skinNetwork = Factory.get(SkinNetwork.class);
    
    
    private Actor actor;
    private final List<Skin> datas = new ArrayList<Skin>();
    
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
                    Skin skin = row.getData();
                    if (skin.isAttached()) {
                        skinNetwork.detachSkin(actor, skin);
                    } else {
                        skinNetwork.attachSkin(actor, skin);
                    }
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
    public List<Skin> getDatas() {
        if (actor != null) {
            datas.clear();
            List<Skin> skins = skinService.getSkins(actor);
            if (skins != null && !skins.isEmpty()) {
                for (Skin s : skins) {
//                    if (s.isBaseSkin()) {
//                        continue;
//                    }
                    if (s.isWeapon()) {
                        continue;
                    }
                    datas.add(s);
                }
            }
        }
        return datas;
    }

    @Override
    public boolean removeItem(Skin data) {
        throw new UnsupportedOperationException();
    }
    
    private class ArmorRow extends ItemRow<Skin> {

        public ArmorRow() {
            super();
        }
        
        @Override
        protected void display(Skin skin) {
            SkinData skinData = skin.getData();
            
            icon.setIcon(skinData.getIcon());
            body.setNameText(ResourceManager.getObjectName(skinData));

            // remove20160912
//            if (skin instanceof SkinData) {
////                body.setDesText(data.getDes());
//                body.setDesText("----TO DO----");
//            } else {
//                body.setDesText(ResourceManager.getObjectDes(skin.getData().getId()));
//            }
            
            body.setDesText("----TO DO----");
            num.setText(String.valueOf(skinData.getTotal()));
            
            setBackgroundVisible(skinData.isUsed());
        }

        @Override
        protected void clickEffect(boolean isPress) {
            super.clickEffect(isPress);
            setBackgroundVisible(data.isAttached());
        }
        
        @Override
        public void onRelease() {
            setBackgroundVisible(data.isAttached());
        }
    }
    
}
