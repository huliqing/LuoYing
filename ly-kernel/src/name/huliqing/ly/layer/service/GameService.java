/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import name.huliqing.ly.Inject;
import name.huliqing.ly.data.GameData;
import name.huliqing.ly.object.game.Game;

/**
 *
 * @author huliqing
 */
public interface GameService extends Inject{
    
    Game loadGame(String gameId);
    
    Game loadGame(GameData data);
    
    GameData loadGameData(String gameId);
}
