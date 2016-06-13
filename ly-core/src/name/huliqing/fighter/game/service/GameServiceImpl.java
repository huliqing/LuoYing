/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.object.game.Game;

/**
 *
 * @author huliqing
 */
public class GameServiceImpl implements GameService {

    @Override
    public void inject() {
        //
    }

    @Override
    public Game loadGame(String gameId) {
        return Loader.loadGame(gameId);
    }

    @Override
    public Game loadGame(GameData data) {
        return Loader.loadGame(data);
    }
    
}
