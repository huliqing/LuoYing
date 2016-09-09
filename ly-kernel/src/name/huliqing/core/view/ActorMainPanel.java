/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view;

import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.InterfaceConstants;
import name.huliqing.core.data.ItemData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.data.TalentData;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.mvc.service.ItemService;
import name.huliqing.core.mvc.service.SkinService;
import name.huliqing.core.mvc.service.TalentService;
import name.huliqing.core.mvc.service.TaskService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.ItemListener;
import name.huliqing.core.object.module.SkinListener;
import name.huliqing.core.object.module.TalentListener;
import name.huliqing.core.object.module.TaskListener;
import name.huliqing.core.object.skin.Skin;
import name.huliqing.core.object.task.Task;
import name.huliqing.core.ui.UIFactory;
import name.huliqing.core.ui.FrameLayout;
import name.huliqing.core.ui.Icon;
import name.huliqing.core.ui.LinearLayout;
import name.huliqing.core.ui.UI;
import name.huliqing.core.ui.Window;
import name.huliqing.core.view.actor.ActorPanel;
import name.huliqing.core.view.actor.ArmorPanel;
import name.huliqing.core.view.actor.AttributePanel;
import name.huliqing.core.view.actor.ItemPanel;
import name.huliqing.core.view.actor.SkillPanel;
import name.huliqing.core.view.actor.TalentPanel;
import name.huliqing.core.view.actor.TaskPanel;
import name.huliqing.core.view.actor.WeaponPanel;

/**
 * 角色主面板，这个面板包含角色所有的“属性","武器","装备","天赋"...等面板
 * @author huliqing
 */
public class ActorMainPanel extends Window implements ItemListener, SkinListener, TalentListener, TaskListener {
    private final ItemService itemService = Factory.get(ItemService.class);
    private final SkinService skinService = Factory.get(SkinService.class);
    private final TalentService talentService = Factory.get(TalentService.class);
    private final TaskService taskService = Factory.get(TaskService.class);
    
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
        attrPanel = new AttributePanel(cw, ch);
        attrPanel.setVisible(false);
        attrPanel.setPageSize(10);

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
            itemService.removeItemListener(this.actor, this);
            skinService.removeSkinListener(this.actor, this);
            talentService.removeTalentListener(this.actor, this);
            taskService.removeTaskListener(this.actor, this);
        }
        
        // 2.更新角色并更新面板内容
        this.actor = actor;
        this.setTitle(ResourceManager.get("common.characterPanel") + "-" + actor.getSpatial().getName());
        
        // remove20160324,不需要一打开时把所有panel都update一次，按需update就可以
        // 3.即打开哪一个tab就更新哪一个就行，以避免panel太多，在手机上影响性能。
//        List<UI> cuis=  bodyPanel.getViews();
//        for (UI child : cuis) {
//            ((ActorPanel) child).setPanelUpdate(actor);
//        }
        
        // 4.显示指定的tab
        showTab(index);
        
        // 5.为新的角色添加侦听器以便实时更新面板内容
        itemService.addItemListener(this.actor, this);
        skinService.addSkinListener(this.actor, this);
        talentService.addTalentListener(this.actor, this);
        taskService.addTaskListener(this.actor, this);
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
            itemService.removeItemListener(actor, this);
            skinService.removeSkinListener(actor, this);
            talentService.removeTalentListener(actor, this);
            taskService.removeTaskListener(actor, this);
        }
    }

    // 物口添加或减少的时候要更新指定面板信息
    @Override
    public void onItemAdded(Actor actor, ItemData itemId, int trueAdded) {
        updatePanel(this.itemPanel, this.armorPanel, this.weaponPanel, this.skillPanel);
    }

    @Override
    public void onItemRemoved(Actor actor, ItemData itemId, int trueRemoved) {
        updatePanel(this.itemPanel, this.armorPanel, this.weaponPanel, this.skillPanel);
    }

    @Override
    public void onSkinAttached(Actor actor, Skin data) {
        updatePanel(armorPanel, weaponPanel, attrPanel);
    }

    @Override
    public void onSkinDetached(Actor actor, Skin data) {
        updatePanel(armorPanel, weaponPanel, attrPanel);
    }

    @Override
    public void onSkinAdded(Actor actor, SkinData data) {
        updatePanel(armorPanel, weaponPanel, attrPanel);
    }

    @Override
    public void onSkinRemoved(Actor actor, SkinData data) {
        updatePanel(armorPanel, weaponPanel, attrPanel);
    }

    @Override
    public void onTalentAdded(Actor actor, TalentData talentData) {
        // 因为天赋影响属性，所以属性面板也需要更新
        updatePanel(talentPanel, attrPanel);
    }

    @Override
    public void onTalentPointsChange(Actor actor, TalentData talentData, int pointsAdded) {
        updatePanel(talentPanel, attrPanel);
    }

    @Override
    public void onTalentRemoved(Actor actor, TalentData talentData) {
        updatePanel(talentPanel, attrPanel);
    }

    @Override
    public void onTaskAdded(Actor source, Task task) {
        updatePanel(taskPanel);
    }

    @Override
    public void onTaskRemoved(Actor actor, Task taskRemoved) {
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
