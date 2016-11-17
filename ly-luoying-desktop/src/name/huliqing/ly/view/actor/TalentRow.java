/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.actor;

import name.huliqing.luoying.ui.tiles.ColumnBody;
import name.huliqing.luoying.ui.tiles.ColumnText;
import name.huliqing.luoying.ui.tiles.ColumnIcon;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.InterfaceConstants;
import name.huliqing.luoying.data.TalentData;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.ly.view.SimpleRow;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.ui.ListView;

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
    
    protected void display(TalentData td) {
        icon.setIcon(td.getIcon());
        body.setNameText(ResourceManager.getObjectName(td));
        body.setDesText(ResourceManager.getObjectDes(td.getId()));
        num.setText(td.getLevel() + "/" + td.getMaxLevel());
    }
}
