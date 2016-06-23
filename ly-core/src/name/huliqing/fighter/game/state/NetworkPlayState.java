/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.state.PlayState;
import name.huliqing.fighter.game.state.lan.Network;
import name.huliqing.fighter.game.view.ClientsWin;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.anim.Anim;
import name.huliqing.fighter.object.anim.Listener;
import name.huliqing.fighter.object.anim.ScaleAnim;
import name.huliqing.fighter.object.game.Game.GameListener;
import name.huliqing.fighter.ui.Icon;
import name.huliqing.fighter.ui.UI;

/**
 * 联网游戏的基类
 * @author huliqing
 */
public abstract class NetworkPlayState extends PlayState implements LanGame {
    private final PlayService playService = Factory.get(PlayService.class);
    protected final Network network = Network.getInstance();

    // 客户端列表界面
    private ClientsWin clientsWin;
    private ScaleAnim clientsWinAnim;
    private Icon lanBtn;
    
    public NetworkPlayState(Application app, GameData gameData) {
        super(app, gameData);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); 
        // 初始化Network
        network.initialize();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        network.update(tpf);
    }
    
    @Override
    public void cleanup() {
        network.cleanup();
        super.cleanup(); 
    }

    @Override
    public void changeGameState(GameState newGameState) {
        super.changeGameState(newGameState);
        gameState.getGame().addListener(new GameListener() {
            @Override
            public void onSceneLoaded() {
                createLanUI();
            }
        });
    }

    @Override
    public boolean isServer() {
        return network.isServer();
    }
    
    /**
     * 刷新客户端界面列表
     */
    @Override
    public void onClientListUpdated() {
        if (clientsWin.isVisible()) {
            clientsWin.setClients(getClients());
        }
    }

    @Override
    public void onActorSelected(ConnData connData, Actor actor) {
        if (gameState != null) {
            gameState.getGame().onActorSelected(actor);
        }
        connData.setActorId(actor.getData().getUniqueId());
        connData.setActorName(actor.getData().getName());
    }

    private void createLanUI() {
         // ---- 联网状态按钮,用于打开局域网客户端列表面板
        float fullWidth = Common.getSettings().getWidth();
        float fullHeight = Common.getSettings().getHeight();
        clientsWin = new ClientsWin(this, fullWidth * 0.75f, fullHeight * 0.8f);
        clientsWin.setToCorner(UI.Corner.CC);
        clientsWin.setCloseable(true);
        clientsWin.setDragEnabled(true);
        clientsWinAnim = new ScaleAnim();
        clientsWinAnim.setStartScale(0.5f);
        clientsWinAnim.setEndScale(1f);
        clientsWinAnim.setRestore(true);
        clientsWinAnim.setUseTime(0.4f);
        clientsWinAnim.setLocalScaleOffset(new Vector3f(clientsWin.getWidth() * 0.5f
                    , clientsWin.getHeight() * 0.5f
                    , 0));
        clientsWinAnim.setTarget(clientsWin);
        clientsWinAnim.addListener(new Listener() {
            @Override
            public void onDone(Anim anim) {
                playService.removeAnimation(anim);
            }
        });
                
        lanBtn = new Icon("Interface/icon/link.png");
        lanBtn.setUseAlpha(true);
        lanBtn.addClickListener(new name.huliqing.fighter.ui.UI.Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                displayLanPanel();
            }
        });
        // 把按钮添加到工具栏
        gameState.getMenuTool().addMenu(lanBtn, 0);
    }
    
    private void displayLanPanel() {
        if (clientsWin.getParent() == null) {
            addObject(clientsWin, true);
            clientsWin.setClients(getClients());
            playService.addAnimation(clientsWinAnim);
        } else {
            removeObject(clientsWin);
        }
    } 
    
}
