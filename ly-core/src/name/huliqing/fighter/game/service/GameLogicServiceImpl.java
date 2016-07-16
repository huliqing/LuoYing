/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.data.GameLogicData;
import name.huliqing.fighter.object.DataFactory;
import name.huliqing.fighter.object.gamelogic.GameLogic;

/**
 *
 * @author huliqing
 */
public class GameLogicServiceImpl implements GameLogicService {

    @Override
    public void inject() {
        // ignore
    }

    @Override
    public GameLogicData loadGameLogicData(String id) {
        return DataFactory.createData(id);
    }

    @Override
    public GameLogic loadGameLogic(String id) {
        GameLogicData data = loadGameLogicData(id);
        return DataFactory.createProcessor(data);
    }

    @Override
    public GameLogic loadGameLogic(GameLogicData data) {
        return DataFactory.createProcessor(data);
    }
    
}
