/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.actor;

import name.huliqing.ly.Factory;
import name.huliqing.ly.ui.tiles.ColumnBody;
import name.huliqing.ly.ui.tiles.ColumnText;
import name.huliqing.ly.ui.tiles.ColumnIcon;
import name.huliqing.ly.constants.InterfaceConstants;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.ly.data.SkillData;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.ly.layer.service.ActorService;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.skill.Skill;
import name.huliqing.ly.ui.UIFactory;
import name.huliqing.ly.ui.Row;

/**
 *
 * @author huliqing
 */
public class SkillRow extends Row<Skill> {
    private final ActorService actorService = Factory.get(ActorService.class);
    
    private final SkillPanel skillPanel;
    
    private Skill data;
    
    // 物品
    private final ColumnIcon icon;
    private final ColumnBody body;
    private final ColumnText num;
    private final ColumnIcon shortcut;
    
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
    public final void displayRow(Skill data) {
        this.data = data;
        display(this.data);
    }
    
    public Skill getData() {
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
    
    protected void display(Skill data) {
        icon.setIcon(data.getData().getIcon());
        
        body.setDesText(ResourceManager.getObjectDes(data.getData().getId()));
        num.setText(data.getData().getLevel() + "/" + data.getData().getMaxLevel());
        
        Actor actor = skillPanel.getActor();
        if (actor != null && actorService.getLevel(actor) < data.getData().getLevelLimit()) {
            body.setDisabled(true);
            body.setNameText(ResourceManager.getObjectName(data.getData()) 
                    + "(" + ResourceManager.get(ResConstants.COMMON_NEED_LEVEL, new Object[] {data.getData().getLevelLimit()}) + ")");
        } else {
            body.setDisabled(false);
            body.setNameText(ResourceManager.getObjectName(data.getData()));
        }
    }
}
