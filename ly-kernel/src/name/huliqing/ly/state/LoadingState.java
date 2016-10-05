/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.state;

import name.huliqing.ly.ui.Text;
import name.huliqing.ly.ui.UI.Corner;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

/**
 * 用于在界面显示"载入"提示
 * @author huliqing
 */
public class LoadingState extends AbstractAppState{
    
    private SimpleApplication app;
    private final PlayState playState;
    private final GameState gameState;
    
    private int frameCount;
    private Text text;
    
    public LoadingState(PlayState playState, GameState gameState) {
        this.playState = playState;
        this.gameState = gameState;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); 
        this.app = (SimpleApplication) app;
        text = new Text("Loading...");
        text.setToCorner(Corner.CC);
        this.app.getGuiNode().attachChild(text);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        // 这里使帧率计数到2时才切换以便”loading“提示可以先得到更新显示在界面是，否则看起来会像卡住了一样。
        frameCount++;
        if (frameCount == 2) {
            playState.changeGameState(gameState);
        }
        if (gameState.isInitialized()) {
            this.app.getStateManager().detach(this);
        }
    }

    @Override
    public void cleanup() {
        frameCount = 0;
        if (text != null) {
            text.removeFromParent();
        }
        super.cleanup(); 
    }
    
}
