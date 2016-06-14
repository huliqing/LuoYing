/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.Inject;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.object.game.Game;

/**
 *
 * @author huliqing
 */
public interface GameService extends Inject{
    
    Game loadGame(String gameId);
    
    Game loadGame(GameData data);
    
    GameData loadGameData(String gameId);
}
