/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import java.util.List;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.GameData;
import name.huliqing.luoying.network.LanGame;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.object.anim.Anim;
import name.huliqing.luoying.object.anim.AnimationControl;
import name.huliqing.luoying.object.anim.Listener;
import name.huliqing.luoying.object.anim.ScaleAnim;
import name.huliqing.luoying.object.env.ChaseCameraEnv;
import name.huliqing.luoying.ui.Icon;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.ly.view.ActorSelectView;
import name.huliqing.ly.view.ClientsWin;

/**
 *
 * @author huliqing
 */
public abstract class NetworkRpgGame extends SimpleRpgGame implements LanGame {
    
    protected final Network network = Network.getInstance();
    
    // 选择角色用的界面
    protected ActorSelectView actorPanel;
    
    // 客户端列表界面
    protected ClientsWin clientsWin;
    protected ScaleAnim clientsWinAnim;
    protected AnimationControl winAnimControl;
    protected Icon lanBtn;
    
    public NetworkRpgGame() {}
 
    public NetworkRpgGame(GameData gameData) {
        setData(gameData);
    }
    
    @Override
    public void initialize(Application app) {
        super.initialize(app); 
        // 初始化Network
        network.initialize(app);
        
        // 创建LanUI界面
        createLanUI();
    }
    
    @Override
    protected void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);
        network.update(tpf);
    }
    
    @Override
    public void cleanup() {
        network.cleanup();
        super.cleanup(); 
    }
    
     /**
     * 显示角色选择面板
     * @param selectableActors 
     */
    public final void showSelectPanel(List<String> selectableActors) {
        if (actorPanel == null) {
            actorPanel = new ActorSelectView(LuoYing.getSettings().getWidth(), LuoYing.getSettings().getHeight(), this.app.getGuiNode());
            actorPanel.setSelectedListener(new ActorSelectView.SelectedListener() {
                @Override
                public void onSelected(String actorId, String actorName) {
                    actorPanel.removeFromParent();
                    actorPanel.getActorView().removeFromParent();
 
                    onSelectPlayer(actorId, actorName);
                }
            });
        }
        actorPanel.setModels(selectableActors);
        
        UIState.getInstance().addUI(actorPanel);
        
        ChaseCameraEnv cc = getChaseCamera();  
        if (cc != null) {
            cc.setChase(actorPanel.getActorView());
        }
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

    private void createLanUI() {
         // ---- 联网状态按钮,用于打开局域网客户端列表面板
        float fullWidth = LuoYing.getSettings().getWidth();
        float fullHeight = LuoYing.getSettings().getHeight();
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
                winAnimControl.setEnabled(false);
            }
        });
        
        winAnimControl = new AnimationControl(clientsWinAnim);
        clientsWin.addControl(winAnimControl);
                
        lanBtn = new Icon("Interface/icon/link.png");
        lanBtn.setUseAlpha(true);
        lanBtn.addClickListener(new name.huliqing.luoying.ui.UI.Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                displayLanPanel();
            }
        });
        // 把按钮添加到工具栏
        getMenuTool().addMenu(lanBtn, 0);
    }
    
    private void displayLanPanel() {
        if (clientsWin.getParent() == null) {
            UIState.getInstance().addUI(clientsWin);
            clientsWin.setClients(getClients());
            winAnimControl.setEnabled(true);
            clientsWinAnim.start();
        } else {
            clientsWin.removeFromParent();
        }
    } 
    
    /**
     * 当玩家在本地选择了一个角色进行游戏后进。
     * @param actorId
     * @param actorName
     */
    protected abstract void onSelectPlayer(String actorId, String actorName);
}
