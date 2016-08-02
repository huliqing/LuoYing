/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.game.state;

import com.jme3.app.Application;
import com.jme3.scene.Spatial;
import name.huliqing.core.ui.UIUtils;
import name.huliqing.core.LY;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.game.service.PlayService;
import name.huliqing.ly.game.view.TeamView;
import name.huliqing.ly.game.view.actor.ActorMainPanel;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.ly.game.view.FaceView;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.object.anim.Anim;
import name.huliqing.core.ui.Icon;
import name.huliqing.core.ui.UI;
import name.huliqing.core.ui.UI.Corner;
import name.huliqing.core.ui.state.UIState;

/**
 * 场景控制,包含设置,人物控制,UI等
 * @author huliqing
 */
public class LanPlayStateUI extends PlayStateUI {
    private final PlayService playService = Factory.get(PlayService.class);
    
    // 队伍视图及目标视图
    protected TeamView teamView;
    protected FaceView targetFace;
    
    // 角色面板
    private ActorMainPanel userPanel;
    private Anim userPanelAnim;
    
    // UI:技能按钮
    private UI attack;
    
    private final GameState gameState;
    
    public LanPlayStateUI(GameState gameState) {
        super();
        this.gameState = gameState;
    }

    @Override
    public void initialize(Application app) {
        // UI在LanPlayState中的initialize中进行了手动ui.initialize()，所以这里
        // 要避免重复载入
        if (isInitialized()) {
            return;
        }
        super.initialize(app);
        
        // UI size
        float fullWidth = LY.getSettings().getWidth();
        float fullHeight = LY.getSettings().getHeight();
        
        // ---- 玩家头像和目标头像
        
        float faceWidth = LY.getSettings().getWidth() * 0.28f;
        float faceHeight = LY.getSettings().getHeight() * 0.12f;
        
        teamView = new TeamView(faceWidth, faceHeight);
        teamView.setToCorner(name.huliqing.core.ui.AbstractUI.Corner.LT);
        
        targetFace = new FaceView(faceWidth, faceHeight);
        targetFace.setToCorner(name.huliqing.core.ui.AbstractUI.Corner.CT);
        targetFace.setVisible(false);
        
        // ---- 角色面板及动画控制
        userPanel = new ActorMainPanel(fullWidth * 0.8f, fullHeight * 0.8f);
        userPanel.setToCorner(Corner.CC);
        userPanel.setCloseable(true);
        userPanel.setDragEnabled(true);
        userPanelAnim = Loader.loadAnimation(IdConstants.ANIM_VIEW_MOVE);
        userPanelAnim.setTarget(userPanel);
        
        // ---- 人物属性开关铵钮
        // 按钮：人物面板,包含装甲、武器、技能、属性、任务等等
        Icon userBtn = new Icon("Interface/icon/bag.png");
        userBtn.setUseAlpha(true);
        userBtn.addClickListener(new name.huliqing.core.ui.UI.Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                displayUserPanel(null);
            }
        });
        
        // ---- 攻击按钮
        float iconWidth = fullHeight * 0.25f;
        attack = UIUtils.createMultView(iconWidth, iconWidth, "Interface/icon/weapon.png", "Interface/ui/circleButton/circleButton.png");
        attack.setMargin(0, 0, 10, 10);
        attack.setToCorner(UI.Corner.RB);
        attack.setDragEnabled(false); // 拖久了或拖得太猛可能出现莫明消失的BUG（出屏幕边界，暂不允许拖动）
        attack.addClickListener(new UI.Listener() {
            @Override
            public void onClick(UI ui, boolean isPressed) {
                if (isPressed) return;
                gameState.attack();
            }
        });
        
        // ---- 添加到界面
        playState.addObject(teamView, true);
        playState.addObject(targetFace, true);
        playState.addObject(attack.getDisplay(), true);
        toolsView.addView(userBtn, 0);
    }

    @Override
    public void doLogic(float tpf) {
        teamView.update(tpf);
        targetFace.update(tpf);
    }

    public TeamView getTeamView() {
        return teamView;
    }
    
    public void setTargetFace(Actor face) {
        this.targetFace.setActor(face);
        this.targetFace.setVisible(true);
    }

    public FaceView getTargetFace() {
        return targetFace;
    }
    
    public ActorMainPanel getUserPanel() {
        return userPanel;
    }
    
    private void displayUserPanel(Actor actor) {
        if (userPanel.getParent() == null) {
            userPanel.setActor(actor != null ? actor : playState.getPlayer());
            UIState.getInstance().addUI(userPanel);
            playService.addAnimation(userPanelAnim);
        } else {
            playState.removeObject(userPanel);
        } 
    }
    
    @Override
    public void cleanup() {
        remove(teamView);
        remove(targetFace);
        remove(attack.getDisplay());
        super.cleanup();
    }
    
    private void remove(Spatial spatial) {
        if (spatial != null) {
            playState.removeObject(spatial);
        }
    }

}
