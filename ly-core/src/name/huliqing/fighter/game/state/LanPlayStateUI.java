/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import name.huliqing.fighter.ui.UIUtils;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.IdConstants;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.state.lan.play.LanPlayState;
import name.huliqing.fighter.game.view.ClientsWin;
import name.huliqing.fighter.game.view.TeamView;
import name.huliqing.fighter.game.view.actor.ActorMainPanel;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.game.view.FaceView;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.object.anim.Anim;
import name.huliqing.fighter.object.anim.ScaleAnim;
import name.huliqing.fighter.ui.Icon;
import name.huliqing.fighter.ui.UI;
import name.huliqing.fighter.ui.UI.Corner;

/**
 * 场景控制,包含设置,人物控制,UI等
 * @author huliqing
 */
public class LanPlayStateUI extends PlayStateUI {
    private final PlayService playService = Factory.get(PlayService.class);
    private LanPlayState playState;
    
    // 队伍视图及目标视图
    protected TeamView teamView;
    protected FaceView targetFace;
    
    // 角色面板
    private ActorMainPanel userPanel;
    private Node userPanelControl;
    private Anim userPanelAnim;
    
    // 客户端列表界面
    private ClientsWin clientsWin;
    private ScaleAnim clientsWinAnim;
    
    // UI:技能按钮
    private UI attack;
    
    public LanPlayStateUI(LanPlayState playState) {
        super(playState);
        this.playState = playState;
    }

    @Override
    public void initialize() {
        // UI在LanPlayState中的initialize中进行了手动ui.initialize()，所以这里
        // 要避免重复载入
        if (isInitialized()) {
            return;
        }
        
        super.initialize();
        // UI size
        float fullWidth = Common.getSettings().getWidth();
        float fullHeight = Common.getSettings().getHeight();
        
        // ---- 玩家头像和目标头像
        
        float faceWidth = Common.getSettings().getWidth() * 0.28f;
        float faceHeight = Common.getSettings().getHeight() * 0.12f;
        
        teamView = new TeamView(faceWidth, faceHeight);
        teamView.setToCorner(name.huliqing.fighter.ui.AbstractUI.Corner.LT);
        
        targetFace = new FaceView(faceWidth, faceHeight);
        targetFace.setToCorner(name.huliqing.fighter.ui.AbstractUI.Corner.CT);
//        targetFace.setDragEnabled(true);// remove20160420
        targetFace.setVisible(false);
        
        // ---- 角色面板及动画控制
        userPanel = new ActorMainPanel(fullWidth * 0.8f, fullHeight * 0.8f);
        userPanel.setToCorner(Corner.CC);
        userPanel.setCloseable(true);
        userPanel.setDragEnabled(true);
        userPanelControl = new Node();
        userPanelAnim = Loader.loadAnimation(IdConstants.ANIM_VIEW_MOVE);
        userPanelAnim.setTarget(userPanelControl);
        // UIState.getInstance().addUI(userPanelControl); // remove20160420
        
        // ---- 人物属性开关铵钮
        // 按钮：人物面板,包含装甲、武器、技能、属性、任务等等
        Icon userBtn = new Icon("Interface/icon/bag.png");
        userBtn.setUseAlpha(true);
        userBtn.addClickListener(new name.huliqing.fighter.ui.UI.Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                displayUserPanel(null);
            }
        });
        
        // ---- 联网状态按钮,用于打开局域网客户端列表面板
        clientsWin = new ClientsWin(playState, fullWidth * 0.75f, fullHeight * 0.8f);
        clientsWin.setToCorner(Corner.CC);
        clientsWin.setCloseable(true);
        clientsWin.setDragEnabled(true);
        clientsWinAnim = new ScaleAnim();
        clientsWinAnim.setStartScale(0.5f);
        clientsWinAnim.setEndScale(1f);
        clientsWinAnim.setBoundFactor(0.2f);
        clientsWinAnim.setRestore(true);
        clientsWinAnim.setUseTime(0.4f);
        clientsWinAnim.setLocalScaleOffset(new Vector3f(clientsWin.getWidth() * 0.5f
                    , clientsWin.getHeight() * 0.5f
                    , 0));
        clientsWinAnim.setTarget(clientsWin);
        
        Icon lanBtn = new Icon("Interface/icon/link.png");
        lanBtn.setUseAlpha(true);
        lanBtn.addClickListener(new name.huliqing.fighter.ui.UI.Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                displayLanPanel();
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
                playState.attack();
            }
        });
        
        // ---- 添加到界面
        playState.addObject(teamView, true);
        playState.addObject(targetFace, true);
        playState.addObject(attack.getDisplay(), true);
//        playState.addObject(userPanelControl, true);
        toolsView.addView(lanBtn, 0);
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
    
    public ClientsWin getClientsWin() {
        return clientsWin;
    }
    
    public ActorMainPanel getUserPanel() {
        return userPanel;
    }
    
    private void displayUserPanel(Actor actor) {
        if (userPanel.getParent() == null) {
            // userPanel在close的时候会自动detach掉，所以这里需要重新attach上去.
            userPanelControl.attachChild(userPanel);
            playState.addObject(userPanelControl, true);
            userPanel.setActor(actor != null ? actor : playState.getPlayer());
            playService.addAnimation(userPanelAnim);
        } else {
            userPanel.close();
            // 这里要把userPanelControl从场景移除，以便下次attach的时候可以覆盖在其它UI上面。
            // 不然会导致该面板的层叠顺序不变。
            userPanelControl.removeFromParent();
        }
    }
    
    private void displayLanPanel() {
        if (clientsWin.getParent() == null) {
            playState.addObject(clientsWin, true);
            clientsWin.setClients(playState.getClients());
            playService.addAnimation(clientsWinAnim);
        } else {
            playState.removeObject(clientsWin);
        }
    } 
    
    @Override
    public void cleanup() {
        remove(teamView);
        remove(targetFace);
        remove(attack.getDisplay());
        remove(userPanelControl);
        remove(clientsWin);
        super.cleanup();
    }
    
    private void remove(Spatial spatial) {
        if (spatial != null) {
            playState.removeObject(spatial);
        }
    }

}
