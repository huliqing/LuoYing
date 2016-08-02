/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.game;

import name.huliqing.core.network.GameClient;
import name.huliqing.core.state.ClientPlayState;

/**
 *
 * @author huliqing
 */
public class LyClientPlayState extends ClientPlayState {
    
    private final Fighter app;

    public LyClientPlayState(Fighter app, GameClient gameClient) {
        super(app, gameClient);
        this.app = app;
    }

    @Override
    public void exit() {
        super.exit(); 
        app.changeStartState();
    }
    
}
