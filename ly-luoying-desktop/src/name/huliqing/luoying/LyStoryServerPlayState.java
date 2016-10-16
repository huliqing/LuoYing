/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying;

import name.huliqing.luoying.data.GameData;
import name.huliqing.luoying.save.SaveStory;
import name.huliqing.luoying.state.StoryServerPlayState;

/**
 *
 * @author huliqing
 */
public class LyStoryServerPlayState extends StoryServerPlayState {
    
    private LuoYing app;
    
    public LyStoryServerPlayState(LuoYing app, GameData gameData, SaveStory saveStory) {
        super(app, gameData, saveStory);
        this.app = app;
    }

    @Override
    public void exit() {
        super.exit();
        app.changeStartState();
    }
    
}
