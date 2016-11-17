/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.state.lan;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.GameData;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.object.Loader;
import name.huliqing.ly.view.HelpView;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.ly.Start;

/**
 * 创建房间的界面
 * @author huliqing
 */
public class CreateRoomState extends AbstractAppState {

    private Start app;
    
    // 可见的游戏列表
    private CreateRoomStateGameListPanel gameList;
    // 创建游戏时的帮助提示
    private HelpView helpCreate;
    // 按钮
    private CreateRoomStateBtnPanel btnPanel;
    
    public CreateRoomState() {}

    @Override
    public void initialize(AppStateManager stateManager, Application _app) {
        super.initialize(stateManager, _app); 
        this.app = (Start) _app;
        float width = LuoYing.getSettings().getWidth();
        float height = LuoYing.getSettings().getHeight();
        
        float btnHeight = UIFactory.getUIConfig().getButtonHeight() * 1.5f;
        float helpHeight = UIFactory.getUIConfig().getButtonHeight();
        float gameListHeight = height - btnHeight - helpHeight;
        
        gameList = new CreateRoomStateGameListPanel(width, gameListHeight);
        helpCreate = new HelpView(width, helpHeight, ResourceManager.get("lan.help.create"));
        helpCreate.setMargin(10, 0, 0, 0);
        btnPanel = new CreateRoomStateBtnPanel(width, btnHeight, this);
        
        LinearLayout localUIRoot = new LinearLayout(width, height);
        localUIRoot.addView(gameList);
        localUIRoot.addView(helpCreate);
        localUIRoot.addView(btnPanel);
        UIState.getInstance().addUI(localUIRoot);
    }
    
    /**
     * 创建房间（服务器）
     */
    public void createRoom() {
        String gameId = gameList.getSelected();
        if (gameId == null) {
            Logger.getLogger(CreateRoomState.class.getName()).log(Level.WARNING, "Need to select a game!");
            return;
        }
        GameData gameData = Loader.loadData(gameId);
        RoomStateServerImpl roomState = new RoomStateServerImpl(gameData);
        app.changeState(roomState);
    }
    
    /**
     * 返回到局域网(房间列表界面)
     */
    public void backToLanState() {
        app.changeState(new LanState());
    }

    @Override
    public void cleanup() {
        UIState.getInstance().clearUI();
        super.cleanup();
    }
    
}
