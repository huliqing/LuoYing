/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.actor;

import name.huliqing.luoying.ui.tiles.ColumnBody;
import name.huliqing.luoying.ui.tiles.ColumnText;
import name.huliqing.luoying.ui.tiles.ColumnIcon;
import name.huliqing.luoying.Factory;
import name.huliqing.ly.constants.InterfaceConstants;
import name.huliqing.luoying.data.ResistData;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.ly.view.SimpleRow;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.ui.ListView;

/**
 * 
 * @author huliqing
 */
public class ResistRow extends SimpleRow<ResistData> {
    protected PlayService playService = Factory.get(PlayService.class);
    
    protected ResistData data;
    
    // 物品
    protected ColumnIcon icon;
    protected ColumnBody body;
    protected ColumnText num;
    
    public ResistRow(ListView parent) {
        super(parent);
        this.setLayout(Layout.horizontal);
        icon = new ColumnIcon(height, height, InterfaceConstants.UI_MISS);
        body = new ColumnBody(height, height, "", "");
        num = new ColumnText(height, height, "");
        addView(icon);
        addView(body);
        addView(num);
    }
    
    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        float iconSize = height;

        icon.setWidth(iconSize);
        icon.setHeight(iconSize);

        num.setWidth(iconSize);
        num.setHeight(iconSize);
        
        body.setWidth(width - iconSize * 2);
        body.setHeight(iconSize);
        
    }

    @Override
    public final void displayRow(ResistData data) {
        this.data = data;
        display(this.data);
    }
    
    public ResistData getData() {
        return this.data;
    }
    
    public void setRowClickListener(Listener listener) {
        addClickListener(listener);
    }
    
    protected void display(ResistData data) {
        icon.setIcon(data.getIcon());
        body.setNameText(ResourceManager.getObjectName(data));
        body.setDesText(ResourceManager.getObjectDes(data.getId()));
        num.setText(data.getValue() + "");
    }
}
