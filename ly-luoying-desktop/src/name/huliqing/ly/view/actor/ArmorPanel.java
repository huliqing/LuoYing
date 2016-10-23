/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.actor;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.manager.ResourceManager;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.luoying.layer.network.SkinNetwork;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.SkinService;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.skin.Skin;
import name.huliqing.luoying.object.skin.Weapon;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.UI;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 * @author huliqing
 */
public class ArmorPanel extends ListView<Skin> implements ActorPanel{
    private final PlayService playService = Factory.get(PlayService.class);
    private final SkinService skinService = Factory.get(SkinService.class);
    private final SkinNetwork skinNetwork = Factory.get(SkinNetwork.class);
    private final GameService gameService = Factory.get(GameService.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    
    private Entity actor;
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
                    gameService.addShortcut(actor, row.getData().getData());
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
    public void setPanelUpdate(Entity actor) {
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
            body.setDesText("unknow");
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
