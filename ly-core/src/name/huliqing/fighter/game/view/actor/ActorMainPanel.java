/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.view.actor;

import java.util.List;
import name.huliqing.fighter.constants.InterfaceConstants;
import name.huliqing.fighter.data.SkinData;
import name.huliqing.fighter.data.TalentData;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.actor.ItemListener;
import name.huliqing.fighter.object.actor.SkinListener;
import name.huliqing.fighter.object.actor.TalentListener;
import name.huliqing.fighter.object.actor.TaskListener;
import name.huliqing.fighter.object.task.Task;
import name.huliqing.fighter.ui.UIFactory;
import name.huliqing.fighter.ui.FrameLayout;
import name.huliqing.fighter.ui.Icon;
import name.huliqing.fighter.ui.LinearLayout;
import name.huliqing.fighter.ui.UI;
import name.huliqing.fighter.ui.Window;

/**
 *
 * @author huliqing
 */
public class ActorMainPanel extends Window implements ItemListener, SkinListener, TalentListener, TaskListener {
    private Actor actor;
    
    private LinearLayout tabPanel;
    private TabButton btnAttr;  // 人物属性面板
    private TabButton btnTalent;  // 人物属性面板
    private TabButton btnWeapon;    // 武器
    private TabButton btnArmor; // 装备面板
    private TabButton btnSkill; // 技能列表
    private TabButton btnItem; // 物品列表
    private TabButton btnTask; // 任务列表
    
    private LinearLayout bodyPanel;
    private AttributePanel attrPanel;
    private TalentPanel talentPanel;
    private WeaponPanel weaponPanel;
    private ArmorPanel armorPanel;
    private SkillPanel skillPanel;
    private ItemPanel itemPanel;
    private TaskPanel taskPanel;
    
    private int globalPageSize = 7;
    // 当前激活的tab和激活的ActorView
    private int index;
    private ActorPanel indexPanel;

    public ActorMainPanel(float width, float height) {
        super(width, height);
        setTitle(ResourceManager.get("common.characterPanel"));
        setLayout(Layout.horizontal);
        
        tabPanel = new LinearLayout();
        bodyPanel = new LinearLayout();
        addView(tabPanel);
        addView(bodyPanel);
        
        float cw = getContentWidth();
        float ch = getContentHeight();
        
        // body panel 
        attrPanel = new AttributePanel();
        attrPanel.setVisible(false);

        armorPanel = new ArmorPanel(cw, ch);
        armorPanel.setPageSize(globalPageSize);
        armorPanel.setVisible(false);
        
        skillPanel = new SkillPanel(cw, ch);
        skillPanel.setPageSize(globalPageSize);
        skillPanel.setVisible(false);
        
        itemPanel = new ItemPanel(cw, ch);
        itemPanel.setPageSize(globalPageSize);
        itemPanel.setVisible(false);
        
        weaponPanel = new WeaponPanel(cw, ch);
        weaponPanel.setPageSize(globalPageSize);
        weaponPanel.setVisible(false);
        
        talentPanel = new TalentPanel(cw, ch);
        talentPanel.setPageSize(globalPageSize);
        talentPanel.setVisible(false);
        
        taskPanel = new TaskPanel(cw, ch);
        taskPanel.setPageSize(globalPageSize);
        taskPanel.setVisible(false);
        
        bodyPanel.addView(attrPanel);
        bodyPanel.addView(talentPanel);
        bodyPanel.addView(weaponPanel);
        bodyPanel.addView(armorPanel);
        bodyPanel.addView(skillPanel);
        bodyPanel.addView(itemPanel);
        bodyPanel.addView(taskPanel);
        
        // tab button
        btnAttr = new TabButton(InterfaceConstants.UI_ITEM_ATTR, attrPanel);
        btnTalent = new TabButton(InterfaceConstants.UI_ITEM_TALENT, talentPanel);
        btnArmor = new TabButton(InterfaceConstants.UI_ITEM_ARMOR, armorPanel);
        btnSkill = new TabButton(InterfaceConstants.UI_ITEM_SKILL, skillPanel);
        btnItem = new TabButton(InterfaceConstants.UI_ITEM_ITEM, itemPanel);
        btnWeapon = new TabButton(InterfaceConstants.UI_ITEM_WEAPON, weaponPanel);
        btnTask = new TabButton(InterfaceConstants.UI_ITEM_TASK, taskPanel);
        tabPanel.addView(btnAttr);
        tabPanel.addView(btnTalent);
        tabPanel.addView(btnTask);
        tabPanel.addView(btnSkill);
        tabPanel.addView(btnWeapon);
        tabPanel.addView(btnArmor);
        tabPanel.addView(btnItem);
        
        // 默认显示最后一个tab
        index = tabPanel.getViews().size() - 1;
        
    }

    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        float tabWidth = getContentWidth() * 0.2f;
        tabPanel.setWidth(tabWidth);
        tabPanel.setHeight(getContentHeight());
        
        List<UI> tabBtns = tabPanel.getViews();
        float tabHeight = getContentHeight() / tabBtns.size();
        for (UI btn : tabBtns) {
            btn.setMargin(0, 0, 0, 0);
            btn.setWidth(tabWidth);
            btn.setHeight(tabHeight);
        }
        
        bodyPanel.setWidth(getContentWidth() * 0.8f);
        bodyPanel.setHeight(getContentHeight());
        bodyPanel.setMargin(0, 0, 0, 0);
        List<UI> bodyChildren = bodyPanel.getViews();
        for (UI child : bodyChildren) {
            child.setWidth(bodyPanel.getWidth());
            child.setHeight(bodyPanel.getHeight());
        }
    }
    
    public void setActor(Actor actor) {
        if (actor == null) {
            return;
        }
        
        // 1.先清理上一个角色的侦听
        if (this.actor != null) {
            this.actor.removeItemListener(this);
            this.actor.removeSkinListener(this);
            this.actor.removeTalentListener(this);
            this.actor.removeTaskListener(this);
        }
        
        // 2.更新角色并更新面板内容
        this.actor = actor;
        this.setTitle(ResourceManager.get("common.characterPanel") + "-" + actor.getModel().getName());
        
        // remove20160324,不需要一打开时把所有panel都update一次，按需update就可以
        // 3.即打开哪一个tab就更新哪一个就行，以避免panel太多，在手机上影响性能。
//        List<UI> cuis=  bodyPanel.getViews();
//        for (UI child : cuis) {
//            ((ActorPanel) child).setPanelUpdate(actor);
//        }
        
        // 4.显示指定的tab
        showTab(index);
        
        // 5.为新的角色添加侦听器以便实时更新面板内容
        this.actor.addItemListener(this);
        this.actor.addSkinListener(this);
        this.actor.addTalentListener(this);
        this.actor.addTaskListener(this);
    }
    
    private void showTab(int index) {
        showTab((TabButton) tabPanel.getViews().get(index));
    }
    
    private void showTab(TabButton activeTab) {
        List<UI> tabButtons = tabPanel.getViews();
        for (UI child : tabButtons) {
            TabButton temp = (TabButton) child;
            if (temp != activeTab) {
                temp.setActive(false);
            }
        }
        activeTab.setActive(true);
        index = tabButtons.indexOf(activeTab);
        indexPanel = activeTab.actorPanel;
    }
    
    // 更新指定的面板
    private void updatePanel(ActorPanel... actorPanels) {
        if (!isVisible()) {
            return;
        }
        for (ActorPanel ap : actorPanels) {
            if (ap == indexPanel) {
                ap.setPanelUpdate(actor);
                break;
            }
        }
    }
    
    public void cleanup() {
        if (actor != null) {
            actor.removeItemListener(this);
            actor.removeSkinListener(this);
            actor.removeTalentListener(this);
            actor.removeTaskListener(this);
        }
    }

    // 物口添加或减少的时候要更新指定面板信息
    @Override
    public void onItemAdded(Actor actor, String itemId, int trueAdded) {
        updatePanel(this.itemPanel, this.armorPanel, this.weaponPanel, this.skillPanel);
    }

    @Override
    public void onItemRemoved(Actor actor, String itemId, int trueRemoved) {
        updatePanel(this.itemPanel, this.armorPanel, this.weaponPanel, this.skillPanel);
    }

    @Override
    public void onSkinAttached(Actor actor, SkinData data) {
        updatePanel(armorPanel, weaponPanel, attrPanel);
    }

    @Override
    public void onSkinDetached(Actor actor, SkinData data) {
        updatePanel(armorPanel, weaponPanel, attrPanel);
    }

    @Override
    public void onTalentAdded(Actor actor, TalentData talentData) {
        // 因为天赋影响属性，所以属性面板也需要更新
        updatePanel(talentPanel, attrPanel);
    }

    @Override
    public void onTalentPointsChange(Actor actor, String talentId, int pointsAdded) {
        updatePanel(talentPanel, attrPanel);
    }

    @Override
    public void onTaskAdded(Actor source, Task task) {
        updatePanel(taskPanel);
    }

    @Override
    public void onTaskCompleted(Actor actor, Task task) {
        updatePanel(taskPanel);
    }
    
    private class TabButton extends FrameLayout {
        
        private Icon tabIcon;
        private ActorPanel actorPanel;
        
        public TabButton(String icon, ActorPanel actorPanel) {
            super();
            this.actorPanel = actorPanel;
            this.tabIcon = new Icon(icon);
            addView(tabIcon);
            
            setBackground(UIFactory.getUIConfig().getBackground(), true);
            setBackgroundColor(UIFactory.getUIConfig().getBodyBgColor(), true);
            
            addClickListener(new Listener() {
                @Override
                public void onClick(UI ui, boolean isPress) {
                    if (!isPress) {
                        showTab(TabButton.this);
                    }
                }
            });
        }

        @Override
        protected void updateViewChildren() {
            super.updateViewChildren();
            tabIcon.setWidth(height * 0.75f);
            tabIcon.setHeight(tabIcon.getWidth());
        }

        @Override
        protected void updateViewLayout() {
            super.updateViewLayout(); 
            tabIcon.setToCorner(Corner.CC);
        }
        
        /**
         * 激活当前面板
         * @param active 
         */
        public void setActive(boolean active) {
            if (active) {
                setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
            } else {
                setBackgroundColor(UIFactory.getUIConfig().getBodyBgColor(), true);
            }
            actorPanel.setPanelVisible(active);
            actorPanel.setPanelUpdate(actor);
        }
        
        @Override
        protected void clickEffect(boolean isPress) {
           // ignore
        }
    }
}
