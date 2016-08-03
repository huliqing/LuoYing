/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import name.huliqing.core.data.GameData;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.object.game.Game;

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

    @Override
    public GameData loadGameData(String gameId) {
        return DataFactory.createData(gameId);
    }
    
}
