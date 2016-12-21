/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.ly.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UI.Corner;
import name.huliqing.luoying.ui.UIFactory;

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
