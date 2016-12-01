/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.sceneloader;

import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.scene.Scene;

/**
 * 使用阻塞的方式载入场景实体。
 * @author huliqing
 */
public class BlockSceneLoader extends AbstractSceneLoader {
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    
    @Override
    protected void loadEntities(Scene scene, List<EntityData> entityDatas) {
        for (EntityData ed : entityDatas) {
            Entity entity = Loader.load(ed);
            playNetwork.addEntity(scene, entity);
        }
        notifyLoadedOK();
    }
}
