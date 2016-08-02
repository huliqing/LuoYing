/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view.actor;

import name.huliqing.core.ui.tiles.ColumnBody;
import name.huliqing.core.ui.tiles.ColumnText;
import name.huliqing.core.ui.tiles.ColumnIcon;
import java.util.List;
import name.huliqing.core.constants.InterfaceConstants;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.ui.ListView;
import name.huliqing.core.ui.UIFactory;
import name.huliqing.core.ui.Row;

/**
 *
 * @author huliqing
 */
public class SkillRow extends Row<SkillData> {
//    private final PlayService playService = Factory.get(PlayService.class);
    
    private SkillPanel skillPanel;
    
    private SkillData data;
    
    // 物品
    private ColumnIcon icon;
    private ColumnBody body;
    private ColumnText num;
    private ColumnIcon shortcut;
    
    public SkillRow(SkillPanel skillPanel) {
        super();
        this.skillPanel = skillPanel;
        this.setLayout(Layout.horizontal);
        icon = new ColumnIcon(height, height, InterfaceConstants.UI_MISS);
        body = new ColumnBody(height, height, "", "");
        num = new ColumnText(height, height, "");
        shortcut = new ColumnIcon(height, height, "Interface/icon/oper.png");
        shortcut.setOnTop();
        addView(icon);
        addView(body);
        addView(num);
        addView(shortcut);
        
        setBackground(UIFactory.getUIConfig().getBackground(), true);
        setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
        setBackgroundVisible(false);
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
    public final void displayRow(SkillData data) {
        this.data = data;
        display(this.data);
    }
    
    public SkillData getData() {
        return this.data;
    }
    
    public void setRowClickListener(Listener listener) {
        addClickListener(listener);
    }
    
    public void setShortcutListener(Listener listener) {
        shortcut.addClickListener(listener);
    }

    @Override
    protected void clickEffect(boolean isPress) {
        if (isPress) {
            this.setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
        }
        setBackgroundVisible(isPress);
    }

    @Override
    public void onRelease() {
        setBackgroundVisible(false);
    }
    
    protected void display(SkillData data) {
        icon.setIcon(data.getProto().getIcon());
        
        body.setDesText(ResourceManager.getObjectDes(data.getId()));
        num.setText(data.getLevel() + "/" + data.getMaxLevel());
        
        Actor actor = skillPanel.getActor();
        if (actor != null && actor.getData().getLevel() < data.getNeedLevel()) {
            body.setDisabled(true);
            body.setNameText(ResourceManager.getObjectName(data) 
                    + "(" + ResourceManager.get(ResConstants.COMMON_NEED_LEVEL, new Object[] {data.getNeedLevel()}) + ")");
        } else {
            body.setDisabled(false);
            body.setNameText(ResourceManager.getObjectName(data));
        }
    }
}
