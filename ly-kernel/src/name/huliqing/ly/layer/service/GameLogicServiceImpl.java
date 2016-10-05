/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import name.huliqing.ly.data.GameLogicData;
import name.huliqing.ly.xml.DataFactory;
import name.huliqing.ly.object.gamelogic.GameLogic;

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
