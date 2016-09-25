/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view.actor;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.mvc.network.SkinNetwork;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.SkinService;
import name.huliqing.core.object.skin.Skin;
import name.huliqing.core.object.skin.Weapon;
import name.huliqing.core.ui.ListView;
import name.huliqing.core.ui.Row;
import name.huliqing.core.ui.UI;

/**
 * 武器面板列表
 * @author huliqing
 */
public class WeaponPanel extends ListView<Skin> implements ActorPanel{
    private final PlayService playService = Factory.get(PlayService.class);
    private final SkinService skinService = Factory.get(SkinService.class);
    private final SkinNetwork skinNetwork = Factory.get(SkinNetwork.class);
    
    private Actor actor;
    private final List<Skin> datas = new ArrayList<Skin>();
    
    public WeaponPanel(float width, float height) {
        super(width, height);
    }

    @Override
    protected Row createEmptyRow() {
        final WeaponRow row = new WeaponRow();
        row.setRowClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    Skin skin = row.getData();
                    if (skin.isAttached()) {
                        if (skinService.isWeaponTakeOn(actor)) {
                            skinNetwork.takeOffWeapon(actor, false);
                        } else {
                            skinNetwork.detachSkin(actor, skin);
                        }
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
                datas.addAll(skins);
            }
        }
        return datas;
    }

    @Override
    protected boolean filter(Skin data) {
        if (data.isBaseSkin()) {
            return true;
        }
        if (data instanceof Weapon) {
            return false;
        }
        return true;
    }
    
    
  
    @Override
    public boolean removeItem(Skin data) {
        throw new UnsupportedOperationException();
    }
    
    private class WeaponRow extends ItemRow<Skin> {

        public WeaponRow() {
            super();
        }
        
        @Override
        public void display(Skin skin) {
            SkinData skinData = skin.getData();
            icon.setIcon(skinData.getIcon());
            body.setNameText(ResourceManager.getObjectName(skinData));
            body.setDesText(skinData.getDes());
            num.setText(String.valueOf(skinData.getTotal()));
            setBackgroundVisible(data.isAttached());
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
