/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view;

import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.layer.service.SkinService;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.layer.service.LogicService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.ui.Icon;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.UI;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 *
 * @author huliqing
 */
public class ActorSelectView extends LinearLayout {
    private final SkillService skillService = Factory.get(SkillService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkinService skinService = Factory.get(SkinService.class);
    private final LogicService logicService = Factory.get(LogicService.class);
    private final GameService gameService = Factory.get(GameService.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    
    public interface SelectedListener {
        
        /**
         * 当选择角色时
         * @param playerData 角色id
         */
        void onSelected(EntityData playerData);
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
    private Entity currentEntity;
    
    // 随机名称
    private String[] randomNames;
    
    private SelectedListener selectedListener;
    
    public ActorSelectView(float width, float height, Node actorPanelRoot) {
        super();
        this.width = width;
        this.height = height;
        this.actorView = new Node("actorView");
        this.actorView.setLocalTranslation(0, 10, 0);
        this.actorView.addLight(new AmbientLight());
        actorPanelRoot.attachChild(this.actorView);
        
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
                actorView.removeFromParent();
                ActorSelectView.this.removeFromParent();
                if (selectedListener != null && models != null && models.size() > 0) {
                    gameService.setName(currentEntity, nameView.getText());
                    currentEntity.updateDatas();
                    selectedListener.onSelected(currentEntity.getData());
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
        
        currentEntity = Loader.load(models.get(index));
        gameService.setAutoLogic(currentEntity, false);
        actorService.setLocation(currentEntity, new Vector3f(0, 0.75f, 0));
        actorService.setViewDirection(currentEntity, new Vector3f(1, 0, 0));
        actorService.setPhysicsEnabled(currentEntity, false);
        skillService.playSkill(currentEntity, skillService.getSkillWaitDefault(currentEntity), false);
        skinService.takeOnWeapon(currentEntity);
        actorView.detachAllChildren();
        actorView.attachChild(currentEntity.getSpatial());
        current = index;
    }
    
    private String getRandomName() {
        if (randomNames == null) {
            randomNames = ResourceManager.get("npc.name.full").split(",");
        }
        return randomNames[FastMath.nextRandomInt(0, randomNames.length - 1)];
    }
}
