/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.view.actor;

import name.huliqing.fighter.ui.tiles.ColumnBody;
import name.huliqing.fighter.ui.tiles.ColumnText;
import name.huliqing.fighter.ui.tiles.ColumnIcon;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.InterfaceConstants;
import name.huliqing.fighter.data.TalentData;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.view.tiles.SimpleRow;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.ui.ListView;

/**
 * 
 * @author huliqing
 */
public class TalentRow extends SimpleRow<TalentData> {
    protected PlayService playService = Factory.get(PlayService.class);
    
    protected TalentData data;
    
    // 物品
    protected ColumnIcon icon;
    protected ColumnBody body;
    protected ColumnText num;
    protected ColumnIcon shortcut;
    
    public TalentRow(ListView parent) {
        super(parent);
        this.setLayout(Layout.horizontal);
        icon = new ColumnIcon(height, height, InterfaceConstants.UI_MISS);
        body = new ColumnBody(height, height, "", "");
        num = new ColumnText(height, height, "");
        shortcut = new ColumnIcon(height, height, InterfaceConstants.UI_UP);
        addView(icon);
        addView(body);
        addView(num);
        addView(shortcut);
    }
    
    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        float iconSize = height;

        icon.setWidth(iconSize);
        icon.setHeight(iconSize);

        num.setWidth(iconSize);
        num.setHeight(iconSize);

        shortcut.setWidth(iconSize);
        shortcut.setHeight(iconSize);
        shortcut.setPreventEvent(true);

        body.setWidth(width - iconSize * 3);
        body.setHeight(iconSize);
    }

    @Override
    public final void displayRow(TalentData data) {
        this.data = data;
        display(this.data);
    }
    
    public TalentData getData() {
        return this.data;
    }
    
    public void setRowClickListener(Listener listener) {
        addClickListener(listener);
    }
    
    public void setUpListener(Listener listener) {
        shortcut.addClickListener(listener);
    }
    
    protected void display(TalentData data) {
        icon.setIcon(data.getIcon());
        body.setNameText(ResourceManager.getObjectName(data));
        body.setDesText(ResourceManager.getObjectDes(data.getId()));
        num.setText(data.getLevel() + "/" + data.getMaxLevel());
    }
}
