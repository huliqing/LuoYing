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
package name.huliqing.luoying.object.entity;

import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import name.huliqing.luoying.data.ModelEntityData;

/**
 * 模型类的场景物体.
 * @author huliqing
 * @param <T>
 */
public abstract class ModelEntity<T extends ModelEntityData> extends AbstractEntity<T> {
    
    @Override
    public void setData(T data) {
        super.setData(data);
    }

    @Override
    public T getData() {
        return super.getData();
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setShadowMode(spatial.getShadowMode());
        data.setCullHint(spatial.getCullHint());
        data.setQueueBucket(spatial.getQueueBucket());
    }
    
    @Override
    public void initEntity() {
        ShadowMode sm = data.getShadowMode();
        if (sm != null) {
            spatial.setShadowMode(sm);
        }
        CullHint ch = data.getCullHint();
        if (ch != null) {
            spatial.setCullHint(ch);
        }
        Bucket qb = data.getQueueBucket();
        if (qb != null) {
            spatial.setQueueBucket(qb);
        }
    }

    @Override
    protected final Spatial initSpatial() {
        return loadModel();
    }
    
    /**
     * 载入模型，这个方法需要返回一个代表当前ModelEntity的Spatial.
     * @return 
     */
    protected abstract Spatial loadModel();
    
}
