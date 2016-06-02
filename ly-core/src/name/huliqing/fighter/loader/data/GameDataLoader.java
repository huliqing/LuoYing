/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader.data;

import java.util.Arrays;
import java.util.List;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.constants.IdConstants;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.data.SceneData;
import name.huliqing.fighter.object.DataFactory;

/**
 *
 * @author huliqing
 */
public class GameDataLoader implements DataLoader<GameData>{

    @Override
    public GameData loadData(Proto proto) {
        GameData data = new GameData(proto.getId());
        
        // 场景信息
        String sceneId = proto.getAttribute("scene");
        if (sceneId != null) {
            SceneData scendData = (SceneData) DataFactory.createData(sceneId);
            data.setSceneData(scendData);
        }
        
        // 可选的角色列表
        String[] availableActors = proto.getAsArray("availableActors");
        if (availableActors != null && availableActors.length > 0) {
            List<String> actors = data.getAvailableActors();
            if (Config.debug) {
                actors.add(IdConstants.ACTOR_PLAYER_TEST);
            }
            actors.addAll(Arrays.asList(availableActors));
        }
        
        return data;
    }
    
}
