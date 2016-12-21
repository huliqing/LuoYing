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
package name.huliqing.luoying.mess;

import com.jme3.network.serializing.Serializable;

/**
 * 服务端响应客户端的角色选择结果.
 * @author huliqing
 */
@Serializable
public class ActorSelectResultMess extends GameMess {

    // 从服务端返回的角色唯一ID，该ID即为客户端所选的玩家角色
    private long actorId;
    // 服务端的应答结果，true表示允许选择，false表示选择失败
    private boolean success;
    // 如果选择失败，则error中包含错误信息
    private String error;
    
    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    /**
     * true:选择没有问题；false：选择失败
     * @return 
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 设置选择结果
     * @param result 
     */
    public void setSuccess(boolean result) {
        this.success = result;
    }

    /**
     * 如果选择失败，可以从这个方法中获得失败原因
     * @return 
     */
    public String getError() {
        return error;
    }

    /**
     * 设置选择失败的原因
     * @param error 
     */
    public void setError(String error) {
        this.error = error;
    }

}
