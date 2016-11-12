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
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.UI;
import name.huliqing.ly.layer.service.GameService;

/**
 * @author huliqing
 */
public class ArmorPanel extends ListView<SkinData> implements ActorPanel{
    private final GameService gameService = Factory.get(GameService.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    
    private Entity actor;
    private final List<SkinData> datas = new ArrayList<SkinData>();
    
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
    public List<SkinData> getDatas() {
        if (actor != null) {
            datas.clear();
            List<SkinData> skins = actor.getData().getObjectDatas(SkinData.class, null);
            if (skins != null && !skins.isEmpty()) {
                for (SkinData s : skins) {
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
    protected boolean filter(SkinData skin) {
        if (skin.isBaseSkin()) {
            return true;
        }
        if (skin.isWeapon()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean removeItem(SkinData data) {
        throw new UnsupportedOperationException();
    }
    
    private class ArmorRow extends ItemRow<SkinData> {
        
        @Override
        protected void display(SkinData skinData) {
            icon.setIcon(skinData.getIcon());
            body.setNameText(ResourceManager.getObjectName(skinData));
            body.setDesText("unknow");
            num.setText(String.valueOf(skinData.getTotal()));
            
            setBackgroundVisible(skinData.isUsed());
        }

        @Override
        protected void clickEffect(boolean isPress) {
            super.clickEffect(isPress);
            setBackgroundVisible(data.isUsed());
        }
        
        @Override
        public void onRelease() {
            setBackgroundVisible(data.isUsed());
        }
    }
    
}
