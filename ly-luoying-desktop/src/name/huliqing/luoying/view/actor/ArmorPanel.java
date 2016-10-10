/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.view.actor;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.Factory;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.data.SkinData;
import name.huliqing.ly.layer.network.SkinNetwork;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.layer.service.SkinService;
import name.huliqing.ly.object.skin.Skin;
import name.huliqing.ly.object.skin.Weapon;
import name.huliqing.ly.ui.ListView;
import name.huliqing.ly.ui.Row;
import name.huliqing.ly.ui.UI;

/**
 * @author huliqing
 */
public class ArmorPanel extends ListView<Skin> implements ActorPanel{
    private final PlayService playService = Factory.get(PlayService.class);
    private final SkinService skinService = Factory.get(SkinService.class);
    private final SkinNetwork skinNetwork = Factory.get(SkinNetwork.class);
    
    private Actor actor;
    private final List<Skin> datas = new ArrayList<Skin>();
    private int rowTotal;
    
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
                    if (filter(s)) {
                        continue;
                    }
                    datas.add(s);
                }
            }
        }
        return datas;
    }

    @Override
    protected boolean filter(Skin skin) {
        if (skin.isBaseSkin()) {
            return true;
        }
        if (skin instanceof Weapon) {
            return true;
        }
        return false;
    }

    @Override
    public boolean removeItem(Skin data) {
        throw new UnsupportedOperationException();
    }
    
    private class ArmorRow extends ItemRow<Skin> {
        
        @Override
        protected void display(Skin skin) {
            SkinData skinData = skin.getData();
            
            icon.setIcon(skinData.getIcon());
            body.setNameText(ResourceManager.getObjectName(skinData));
            body.setDesText(skinData.getDes());
            num.setText(String.valueOf(skinData.getTotal()));
            
            setBackgroundVisible(skin.isAttached());
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
