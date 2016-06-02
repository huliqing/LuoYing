/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import name.huliqing.fighter.ui.Text;
import name.huliqing.fighter.ui.UI.Corner;
import name.huliqing.fighter.ui.UIFactory;

/**
 * 显示侦率
 * @author huliqing
 */
public class FpsState extends AbstractAppState {
    protected Application app;
    protected float secondCounter = 0.0f;
    protected int frameCounter = 0;
    protected Node guiNode;
    
    private boolean showFps = true;
    private Text fpsLabel;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;
        this.fpsLabel = new Text("FPS:xx");
        this.fpsLabel.setFontSize(UIFactory.getUIConfig().getDesSize());
        this.fpsLabel.setMargin(0, 0, 2, 0);
        this.fpsLabel.setToCorner(Corner.RB);
        
        if (app instanceof SimpleApplication) {
            SimpleApplication simpleApp = (SimpleApplication)app;
            simpleApp.getGuiNode().attachChild(fpsLabel);
        }
        
        // remove20160313,不能放在UIState中，这会在切换AppState的时候被清理掉
//        UIState.getInstance().addUI(fpsLabel);
    }

    @Override
    public void update(float tpf) {
        if (showFps) {
            secondCounter += app.getTimer().getTimePerFrame();
            frameCounter ++;
            if (secondCounter >= 1.0f) {
                int fps = (int) (frameCounter / secondCounter);
                fpsLabel.setText("FPS:" + fps);
                secondCounter = 0.0f;
                frameCounter = 0;
            }          
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }
    
}
