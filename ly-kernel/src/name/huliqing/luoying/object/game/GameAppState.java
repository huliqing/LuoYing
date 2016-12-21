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
package name.huliqing.luoying.object.game;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.PlayService;

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
        Factory.get(PlayService.class).registerGame(game);
        game.initialize(app);
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
