/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
