/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.data.SceneData;
import name.huliqing.fighter.object.DataFactory;
import name.huliqing.fighter.object.DataLoader;

/**
 * @author huliqing
 * @param <T>
 */
public class GameLoader<T extends GameData> implements DataLoader<T>{

    @Override
    public void load(Proto proto, T store) {
        SceneData sceneData = DataFactory.createData(store.getAttribute("scene"));
        store.setSceneData(sceneData);
        store.setAvailableActors(store.getAsList("availableActors"));
    }
    
}
