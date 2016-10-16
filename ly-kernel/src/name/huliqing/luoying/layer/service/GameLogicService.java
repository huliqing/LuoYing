/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.Inject;
import name.huliqing.luoying.data.GameLogicData;
import name.huliqing.luoying.object.gamelogic.GameLogic;

/**
 *
 * @author huliqing
 */
public interface GameLogicService extends Inject {
    
    /**
     * 载入gameLogic数据
     * @param id gameLogicId
     * @return 
     */
    GameLogicData loadGameLogicData(String id);
    
    /**
     * 载入gameLogic
     * @param id gameLogicId
     * @return 
     */
    GameLogic loadGameLogic(String id);
    
    /**
     * 载入gameLogic
     * @param data gameLogicData
     * @return 
     */
    GameLogic loadGameLogic(GameLogicData data);
}
