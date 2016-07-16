/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.data.GameLogicData;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.data.SceneData;
import name.huliqing.fighter.object.DataFactory;
import name.huliqing.fighter.object.DataLoader;

/**
 * @author huliqing
 * @param <T>
 */
public class GameDataLoader<T extends GameData> implements DataLoader<T>{

    @Override
    public void load(Proto proto, T store) {
        SceneData sceneData = DataFactory.createData(store.getAttribute("scene"));
        store.setSceneData(sceneData);
        
        String[] gameLogicsArr = proto.getAsArray("gameLogics");
        if (gameLogicsArr != null && gameLogicsArr.length > 0) {
            List<GameLogicData> gameLogics = new ArrayList<GameLogicData>(gameLogicsArr.length);
            store.setGameLogics(gameLogics);
            for (String gla : gameLogicsArr) {
                gameLogics.add((GameLogicData) DataFactory.createData(gla));
            }
        }
        
        store.setAvailableActors(store.getAsList("availableActors"));
    }
    
}
