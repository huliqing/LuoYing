/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view;

import name.huliqing.luoying.ui.UIUtils;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.anim.Anim;
import name.huliqing.luoying.object.anim.AnimationControl;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.game.Game;
import name.huliqing.luoying.object.game.GameListener;
import name.huliqing.luoying.ui.Icon;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UI.Corner;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.object.game.SimpleRpgGame;

/**
 * 场景控制,包含设置,人物控制,UI等
 * @author huliqing
 */
public class RpgMainUI extends PlayStateUI implements GameListener {
    private SimpleRpgGame rpgGame;
    
    // 队伍视图及目标视图
    private TeamView teamView;
    private FaceView targetFace;
    
    // 角色面板 
    private ActorMainPanel userPanel;
    private Anim userPanelAnim;
    private AnimationControl animationControl;
    
    // UI:技能按钮
    private UI attack;

    @Override
    protected void logicInit(Game game) {
        super.logicInit(game); 
        
        rpgGame = (SimpleRpgGame) game;
        
        // UI size
        float fullWidth = LuoYing.getSettings().getWidth();
        float fullHeight = LuoYing.getSettings().getHeight();
        
        // ---- 玩家头像和目标头像
        
        float faceWidth = fullWidth * 0.28f;
        float faceHeight = fullHeight * 0.12f;
        
        teamView = new TeamView(faceWidth, faceHeight);
        teamView.setToCorner(name.huliqing.luoying.ui.AbstractUI.Corner.LT);
        teamView.setScene(game.getScene());
        
        targetFace = new FaceView(faceWidth, faceHeight);
        targetFace.setToCorner(name.huliqing.luoying.ui.AbstractUI.Corner.CT);
        targetFace.setVisible(false);
        
        // ---- 角色面板及动画控制
        userPanel = new ActorMainPanel(fullWidth * 0.8f, fullHeight * 0.8f);
        userPanel.setToCorner(Corner.CC);
        userPanel.setCloseable(true);
        userPanel.setDragEnabled(true);
        userPanelAnim = Loader.load(IdConstants.ANIM_VIEW_MOVE);
        userPanelAnim.setTarget(userPanel);
        animationControl = new AnimationControl(userPanelAnim);
        rpgGame.getScene().getRoot().addControl(animationControl);
        
        // ---- 人物属性开关铵钮
        // 按钮：人物面板,包含装甲、武器、技能、属性、任务等等
        Icon userBtn = new Icon("Interface/icon/bag.png");
        userBtn.setUseAlpha(true);
        userBtn.addClickListener(new name.huliqing.luoying.ui.UI.Listener() {
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
                rpgGame.attack();
            }
        });
        
        // ---- 添加到界面
        UIState.getInstance().addUI(teamView);
        UIState.getInstance().addUI(targetFace);
        UIState.getInstance().addUI(attack.getDisplay());
        toolsView.addView(userBtn, 0);
        
        rpgGame.addListener(this);
    }
    
    @Override
    public void onGameSceneLoaded(Game game) {
        super.onGameSceneLoaded(game);
        teamView.setScene(game.getScene());
    }

    @Override
    public void cleanup() {
        rpgGame.removeListener(this);
        teamView.removeFromParent();
        targetFace.removeFromParent();
        attack.getDisplay().removeFromParent();
        super.cleanup();
    }

    @Override
    public void logicUpdate(float tpf) {
        teamView.update(tpf);
        targetFace.update(tpf);
    }

    public TeamView getTeamView() {
        return teamView;
    }
    
    public void setTargetFace(Entity face) {
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
            userPanel.setActor(actor != null ? actor : rpgGame.getPlayer());
            UIState.getInstance().addUI(userPanel);
            userPanelAnim.start();
        } else {
            userPanel.removeFromParent();
        }
    }



}
