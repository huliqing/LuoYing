/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.loader;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.GameData;
import name.huliqing.luoying.data.GameLogicData;
import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.data.SceneData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.xml.DataLoader;

/**
 * @author huliqing
 * @param <T>
 */
public class GameDataLoader<T extends GameData> implements DataLoader<T>{

    @Override
    public void load(Proto proto, T store) {
        String scene = store.getAsString("scene");
        if (scene != null) {
            store.setSceneData((SceneData)Loader.loadData(scene));
        }
        String guiScene = proto.getAsString("guiScene");
        if (guiScene != null) {
            store.setGuiSceneData((SceneData) Loader.loadData(guiScene));
        }
        String[] gameLogicsArr = proto.getAsArray("gameLogics");
        if (gameLogicsArr != null && gameLogicsArr.length > 0) {
            List<GameLogicData> gameLogics = new ArrayList<GameLogicData>(gameLogicsArr.length);
            store.setGameLogicDatas(gameLogics);
            for (String gla : gameLogicsArr) {
                gameLogics.add((GameLogicData) DataFactory.createData(gla));
            }
        }
        store.setAvailableActors(store.getAsStringList("availableActors"));
    }
    
}
