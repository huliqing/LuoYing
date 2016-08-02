/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.game;

import name.huliqing.core.data.GameData;
import name.huliqing.core.save.SaveStory;
import name.huliqing.core.state.StoryServerPlayState;

/**
 *
 * @author huliqing
 */
public class LyStoryServerPlayState extends StoryServerPlayState {
    
    private Fighter app;
    
    public LyStoryServerPlayState(Fighter app, GameData gameData, SaveStory saveStory) {
        super(app, gameData, saveStory);
        this.app = app;
    }

    @Override
    public void exit() {
        super.exit();
        app.changeStartState();
    }
    
}
