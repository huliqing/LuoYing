/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import name.huliqing.ly.Inject;
import name.huliqing.ly.data.GameLogicData;
import name.huliqing.ly.object.gamelogic.GameLogic;

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
