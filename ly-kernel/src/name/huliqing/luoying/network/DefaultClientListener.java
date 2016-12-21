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
package name.huliqing.luoying.network;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.mess.GameMess;

/**
 * 默认的局域网客户端监听来自服务端的消息的侦听器
 * @author huliqing 
 */
public abstract class DefaultClientListener extends AbstractClientListener {
    private static final Logger LOG = Logger.getLogger(DefaultClientListener.class.getName());
    
    private final List<GameMess> messageQueue = new LinkedList<GameMess>();
    private double offset = Double.MIN_VALUE;
    private final double maxDelay = 0.50;
    
    private boolean onGameInitializing;
    private int totalEntityInitialize;
    private int countEntityInitialize;
    
    @Override
    protected final void onReceiveGameMess(GameClient gameClient, GameMess m) {
        
        // 统一把接收到的消息放到队列中
        GameMess message = (GameMess) m;
        if (offset == Double.MIN_VALUE) {
            offset = gameClient.time - message.getTime();
        }
        double delayTime = (message.getTime() + offset) - gameClient.time;
        if (delayTime > maxDelay) {
            offset -= delayTime - maxDelay;
        } else if (delayTime < 0) {
            offset -= delayTime;
        }
        messageQueue.add(message);

    }
    
    @Override
    public void update(float tpf, GameClient gameClient) {
        super.update(tpf, gameClient);
        
        // 在客户端场景初始化载入entity的时候是一个一个执行mess的,以便客户端看到进度更新
        if (onGameInitializing) {
            GameMess gameMess = null;
            for (Iterator<GameMess> it = messageQueue.iterator(); it.hasNext();) {
                gameMess = it.next();
                processGameMess(gameClient, gameMess);
                it.remove();
                break;
            }
            countEntityInitialize++;
            
            LOG.log(Level.INFO, "initialize scene entity: {0}/{1} ==> {2}", new Object[] {countEntityInitialize, totalEntityInitialize, gameMess});
            onEntityInitializeCount(countEntityInitialize, totalEntityInitialize);
            
            if (countEntityInitialize >= totalEntityInitialize) {
                onGameInitializing = false;
                LOG.log(Level.INFO, "Scene entity initialized! Client can start game now...");
                onEntityInitialized();
            }
            return;
        }
        
        // 客户端场景Entity初始化完毕后.
        for (Iterator<GameMess> it = messageQueue.iterator(); it.hasNext();) {
            GameMess message = it.next();
            if (message.getTime() >= gameClient.time + offset) {
                processGameMess(gameClient, message);
                it.remove();
            }
        }
    }
    
    /**
     * 当客户端接收到来自服务端的GameMess消息时该方法被自动调用，默认情况下，
     * 该方法将直接调用GameeMess的applyOnClient方法。
     * @param gameClient 客户端
     * @param gameMess 从服务端发来的消息
     */
    protected void processGameMess(GameClient gameClient, GameMess gameMess) {
        gameMess.applyOnClient(gameClient);
    }

    @Override
    protected void onGameInitialize(int initEntityTotal) {
        // 开始转入实体的初始化。
        onGameInitializing = true;
        totalEntityInitialize = initEntityTotal;
        LOG.log(Level.INFO, "onGameInitialize start..., needEntityInitialize={0}", initEntityTotal);
    }
    
    /**
     * 覆盖这个方法来监听客户端实体的初始化进度。
     * @param count 已经处理的实体数
     * @param total 总共需要处理的实体数
     */
    protected void onEntityInitializeCount(int count, int total) {
        // 
    }
    
    /**
     * 当客户端完成场景实体的初始化后，该方法被立即调用，通常意味着客户端可以开始游戏。
     */
    protected abstract void onEntityInitialized();
}
