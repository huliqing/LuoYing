/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

/**
 * GameAppState,用来包装运行游戏。使用示例：
 * <code>
 * <pre>
 * public void simpleInitApp() {
 *      Game game = Loader.load(gameData);
 *      stateManager.attach(new GameAppState(game));
 * }
 * </pre>
 * </code>
 * @author huliqing
 */
public class GameAppState extends AbstractAppState {

    private final Game game;
    
    public GameAppState(Game game) {
        this.game = game;
    }
    
    public Game getGame() {
        return game;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        game.initialize();
    }

    @Override
    public void update(float tpf) {
        game.update(tpf);
    }

    @Override
    public void cleanup() {
        game.cleanup();
        super.cleanup(); 
    }
}
