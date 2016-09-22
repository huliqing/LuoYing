/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.game;

import name.huliqing.core.network.GameServer;
import name.huliqing.core.network.LanServerPlayState;

/**
 *
 * @author huliqing
 */
public class LyLanServerPlayState extends LanServerPlayState {
    
    private Fighter app;
    
    public LyLanServerPlayState(Fighter app, GameServer gameServer) {
        super(app, gameServer);
        this.app = app;
    }

    @Override
    public void exit() {
        super.exit();
        app.changeStartState();
    }
    
}
