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
package name.huliqing.luoying.layer.network;

import com.jme3.network.HostedConnection;
import java.util.Random;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.manager.RandomManager;
import name.huliqing.luoying.mess.ActorFightMess;
import name.huliqing.luoying.mess.EntityAddMess;
import name.huliqing.luoying.mess.EntityRemoveMess;
import name.huliqing.luoying.mess.RandomSeedMess;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.scene.Scene;

/**
 * @author huliqing
 */
public class PlayNetworkImpl implements PlayNetwork {
    private final Network network = Network.getInstance();
    private PlayService playService;
    
    @Override
    public void inject() {
        playService = Factory.get(PlayService.class);
    }

    @Override
    public void addEntity(Entity entity) {
        if (network.isClient()) {
            return;
        }
        
        entity.updateDatas();
        EntityAddMess mess = new EntityAddMess();
        mess.setEntityData(entity.getData());
        network.broadcast(mess);
        
        playService.addEntity(entity);
    }
    
    @Override
    public void addGuiEntity(Entity entity) {
        if (network.isClient()) {
            return;
        }
   
        // remove20161205,GUI场景实体只作为本地端实体，不共享到客户端。
//        entity.updateDatas();
//        EntityAddMess mess = new EntityAddMess();
//        mess.setEntityData(entity.getData());
//        mess.setGuiScene(true);
//        network.broadcast(mess);
        
        entity.updateDatas();
        playService.addEntity(entity);
    }

    /**
     * 向指定的场景添加实体，注：如果指定场景是主场景，则entity会被添加到本地场景，并广播到所有客户端。
     * 如果是GUI场景，则entity只添加到本地，不进行广播。
     * @param scene
     * @param entity 
     */
    @Override
    public void addEntity(Scene scene, Entity entity) {
        if (network.isClient())
            return;
        
        Scene mainScene = playService.getGame().getScene();
        Scene guiScene = playService.getGame().getGuiScene();
        if (mainScene == scene) {
            addEntity(entity);
        } else if (guiScene == scene) {
            addGuiEntity(entity);
        } else {
            throw new IllegalStateException("Unknow scene, the scene must be the main scene or GUI scene of the game, "
                    + "scene=" + scene + ", mainScene=" + mainScene + ", guiScene=" + guiScene);
        }
    }

    @Override
    public void addEntityToClient(HostedConnection conn, Entity entity) {
        if (network.isClient()) {
            return;
        }
        entity.updateDatas();
        EntityAddMess mess = new EntityAddMess();
        mess.setEntityData(entity.getData());
        network.sendToClient(conn, mess);
    }

    @Override
    public void removeEntity(Entity entity) {
        if (network.isClient()) {
            return;
        }
        EntityRemoveMess mess = new EntityRemoveMess();
        mess.setEntityId(entity.getEntityId());
        network.broadcast(mess);
        
        playService.removeEntity(entity);
    }
    
    @Override
    public void attack(Entity actor, Entity target) {
        // On client
        if (network.isClient()) {
            ActorFightMess mess = new ActorFightMess();
            mess.setTargetId(target != null ? target.getData().getUniqueId() : -1);
            network.sendToServer(mess);
            return;
        }

        // On Server，这个命令服务端不需要广播到客户端。
        playService.attack(actor, target);
    }
    
    @Override
    public void updateRandomSeed() {
        if (network.isClient()) {
            return;
        }
        int seed = new Random().nextInt();
        
        // 将种子发送到客户端，由客户端更新随机数
        if (network.hasConnections()) {
            RandomSeedMess mess = new RandomSeedMess();
            mess.setRandomSeed(seed);
            network.broadcast(mess);
        }
        
        // 服务端更新随机种子。不要实现 PlayService.updateRandomSeed()方法。
        RandomManager.setRandomSeed(seed);
        
    }
    
}
