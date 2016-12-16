/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.appstate;

import com.jme3.app.Application;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppStateManager;

/**
 *
 * @author huliqing
 */
public class CustomStatsAppState  extends StatsAppState{

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        statsView.setLocalTranslation(statsView.getLocalTranslation().addLocal(0, 200, 0));
        fpsText.setLocalTranslation(fpsText.getLocalTranslation().addLocal(0, 200, 0));
        darkenFps.setLocalTranslation(darkenFps.getLocalTranslation().addLocal(0, 200, 0));
        darkenStats.setLocalTranslation(darkenStats.getLocalTranslation().addLocal(0, 200, 0));
    }
    
}
