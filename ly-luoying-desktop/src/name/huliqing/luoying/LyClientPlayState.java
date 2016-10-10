/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying;

import name.huliqing.ly.network.GameClient;
import name.huliqing.luoying.state.ClientPlayState;

/**
 *
 * @author huliqing
 */
public class LyClientPlayState extends ClientPlayState {
    
    private final LuoYing fighter;

    public LyClientPlayState(LuoYing app, GameClient gameClient) {
        super(app, gameClient);
        this.fighter = app;
    }

    @Override
    public void exit() {
        super.exit(); 
        fighter.changeStartState();
    }
    
}
