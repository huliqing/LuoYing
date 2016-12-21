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
package name.huliqing.luoying.save;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import java.io.IOException;

/**
 * 这个类主要用于存档客户端所控制的角色的唯一ID，以及存档时角色所在的游戏的ID
 * @author huliqing
 */
public class ClientData implements Savable {
    
    // 客户端的唯一标识
    private String clientId;
    
    // 客户端存档的时候所在的游戏id
    private String gameId;
    
    // 客户端所控制的角色的唯一ID
    private long actorId;

    /**
     * 获取客户端ID标识
     * @return 
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * 设置客户端id标识
     * @param clientId 
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * 获取存档时的游戏ID
     * @return 
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * 设置存档时所在的游戏id
     * @param gameId 
     */
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    /**
     * 获取客户端所控制的角色的唯一ID
     * @return 
     */
    public long getActorId() {
        return actorId;
    }

    /**
     * 设置客户端所控制的角色的唯一ID
     * @param actorId 
     */
    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(clientId, "clientId", null);
        oc.write(gameId, "gameId", null);
        oc.write(actorId, "actorId", -1);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        clientId = ic.readString("clientId", null);
        gameId = ic.readString("gameId", null);
        actorId = ic.readLong("actorId", -1);
    }
    
}
