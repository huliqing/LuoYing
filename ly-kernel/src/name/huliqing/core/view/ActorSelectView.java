/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.mvc.service.SkinService;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.mvc.service.LogicService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.ui.Icon;
import name.huliqing.core.ui.LinearLayout;
import name.huliqing.core.ui.Text;
import name.huliqing.core.ui.UIFactory;
import name.huliqing.core.ui.UI;

/**
 *
 * @author huliqing
 */
public class ActorSelectView extends LinearLayout {
    private final SkillService skillService = Factory.get(SkillService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkinService skinService = Factory.get(SkinService.class);
    private final LogicService logicService = Factory.get(LogicService.class);
    
    public interface SelectedListener {
        
        /**
         * 当选择角色时
         * @param actorId 角色id
         * @param name 角色名字
         */
        void onSelected(String actorId, String name);
    }
    
    // 可选的角色id列表
    private List<String> models;
    
    private LinearLayout namePanel;
    private Text nameView;  // 名字显示栏
    private Icon diceIcon;  // 用于产生随机名称
    
    // 显示角色的位置
    private Node actorView;
    
    // 左按钮、右按钮和确认按钮
    private Icon btnPrevious;
    private Icon btnNext;
    
    private LinearLayout btnPanel;
    private SimpleBtn btnConfirm;
    
    // 当前显示的model角色的索引
    private int current = 0;
    
    // 随机名称
    private String[] randomNames;
    
    private SelectedListener selectedListener;
    
    public ActorSelectView(float width, float height, Node guiRoot) {
        super();
        this.width = width;
        this.height = height;
        this.actorView = new Node("actorView");
        this.actorView.setLocalTranslation(0, 10, 0);
        float btnW = width * 0.2f;
        float btnH = btnW * 0.5f;
        
        nameView = new Text(getRandomName());
        diceIcon = new Icon("Interface/icon/dice.png");
        diceIcon.setWidth(height * 0.07f);
        diceIcon.setHeight(height * 0.07f);
        diceIcon.setMargin(20, 0, 0, 0);
        diceIcon.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                nameView.setText(getRandomName());
            }
        });
        namePanel = new LinearLayout();
        namePanel.setLayout(Layout.horizontal);
        namePanel.setMargin(0, height * 0.05f, 0, 0);
        namePanel.addView(nameView);
        namePanel.addView(diceIcon);
        
        btnPrevious = new Icon("Interface/icon/arrow_left.png");
        btnPrevious.setWidth(btnW);
        btnPrevious.setHeight(btnH);
        
        btnNext = new Icon("Interface/icon/arrow_right.png");
        btnNext.setWidth(btnW);
        btnNext.setHeight(btnH);

        btnPanel = new LinearLayout();
        btnPanel.setBackground(UIFactory.getUIConfig().getBackground(), true);
        btnPanel.setWidth(width);
        btnPanel.setHeight(height * 0.2f);
        btnPanel.setBackgroundColor(new ColorRGBA(0.75f,0.75f,0.75f, 1f), true);
        
        btnConfirm = new SimpleBtn(ResourceManager.get("lan.actorSelect.ok"));
        btnConfirm.setWidth(width * 0.2f);
        btnConfirm.setHeight(height * 0.1f);
        btnConfirm.setBackgroundVisible(false);
        btnConfirm.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                if (selectedListener != null 
                        && models != null
                        && models.size() > 0) {
                    selectedListener.onSelected(models.get(current), nameView.getText());
                }
            }
        });
        btnPanel.addView(btnConfirm);
        
        addView(namePanel);
        addView(btnPrevious);
        addView(btnNext);
        addView(btnPanel);
        
        // event.
        btnPrevious.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                previous();
            }
        });
        btnNext.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                next();
            }
        });
    }

    @Override
    protected void updateViewLayout() {
        super.updateViewLayout(); 
        namePanel.setToCorner(Corner.CT);
        btnPrevious.setToCorner(Corner.LC);
        btnNext.setToCorner(Corner.RC);
        btnPanel.setToCorner(Corner.CB);
        btnConfirm.setToCorner(Corner.CC);
    }
    
    public Spatial getActorView() {
        return actorView;
    }
    
    /**
     * 设置可用于选择的角色ID列表
     * @param models 
     */
    public void setModels(List<String> models) {
        this.models = models;
        show(current);
    }
    
    /**
     * 获取当前选中的角色ID,如果没有选中任何角色，则返回null
     * @return 
     */
    public String getSelected() {
        if (models == null || models.isEmpty()) 
            return null;
        return models.get(current);
    }
    
    public void setSelectedListener(SelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }
    
    // 显示前一个角色
    private void previous() {
        if (models == null) return;
        int index = current - 1;
        if (index < 0) {
            index = models.size() - 1;
        }
        show(index);
    }
    
    // 显示下一个角色
    private void next() {
        if (models == null) return;
        int index = current + 1;
        if (index > models.size() - 1) {
            index = 0;
        }
        show(index);
    }
    
    private void show(int index) {
        if (models == null || index < 0 || index > models.size() - 1) {
            return;
        }
        // Actor actor = Loader.loadActor(models.get(index));
        Actor actor = actorService.loadActor(models.get(index));
        
        
//        actor.setLocation(new Vector3f(0, 0.75f, 0));
//        actor.setViewDirection(new Vector3f(1, 0, 0));
//        actor.setEnabled(false);
        logicService.setAutoLogic(actor, false); // 必须去掉AI
        actorService.setLocation(actor, new Vector3f(0, 0.75f, 0));
        actorService.setViewDirection(actor, new Vector3f(1, 0, 0));
        actorService.setPhysicsEnabled(actor, false);
        
        skillService.playSkill(actor, skillService.getSkill(actor, SkillType.wait), false);
        skinService.takeOnWeapon(actor, true);
        actorView.detachAllChildren();
        actorView.attachChild(actor.getSpatial());
        current = index;
    }
    
    private String getRandomName() {
        if (randomNames == null) {
            randomNames = ResourceManager.get("npc.name.full").split(",");
        }
        return randomNames[FastMath.nextRandomInt(0, randomNames.length - 1)];
    }
}
