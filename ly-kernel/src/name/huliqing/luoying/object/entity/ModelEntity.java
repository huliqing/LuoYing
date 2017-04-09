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
import name.huliqing.luoying.data.EntityData;

/**
 * 模型类的场景物体.
 * @author huliqing
 */
public abstract class ModelEntity extends AbstractEntity {
    
    private final static String ATTR_SHADOW_MODE = "shadowMode";
    private final static String ATTR_CULL_HINT = "cullHint";
    private final static String ATTR_QUEUE_BUCKET = "queueBucket";
    private final static String ATTR_PREFER_UNSHADED = "preferUnshaded";
    
    private ShadowMode shadowMode;
    private CullHint cullHint;
    private Bucket bucket;
    
    // Will deprecate
    private boolean preferUnshaded;

    @Override
    public void setData(EntityData data) {
        super.setData(data);
        String tempSM= data.getAsString(ATTR_SHADOW_MODE);
        if (tempSM != null) {
            for (ShadowMode sm : ShadowMode.values()) {
                if (sm.name().equals(tempSM)) {
                    shadowMode = sm;
                    break;
                }
            }
        }
        
        String tempCH = data.getAsString(ATTR_CULL_HINT);
        if (tempCH != null) {
            for (CullHint ch : CullHint.values()) {
                if (ch.name().equals(tempCH)) {
                    cullHint = ch;
                    break;
                }
            }
        }
        
        String tempBK = data.getAsString(ATTR_QUEUE_BUCKET);
        if (tempBK != null) {
            for (Bucket b : Bucket.values()) {
                if (b.name().equals(tempBK)) {
                    bucket = b;
                    break;
                }
            }
        }
        
        preferUnshaded = data.getAsBoolean(ATTR_PREFER_UNSHADED, false);
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute(ATTR_SHADOW_MODE, spatial.getShadowMode().name());
        data.setAttribute(ATTR_CULL_HINT, spatial.getCullHint().name());
        data.setAttribute(ATTR_QUEUE_BUCKET, spatial.getQueueBucket().name());
        data.setAttribute(ATTR_PREFER_UNSHADED, preferUnshaded);
    }
    
    public boolean isPreferUnshaded() {
        return preferUnshaded;
    }
    
    public void setPreferUnshaded(boolean preferUnshaded) {
        this.preferUnshaded = preferUnshaded;
    }
    
    @Override
    public void initEntity() {
        if (shadowMode != null) {
            spatial.setShadowMode(shadowMode);
        }
        if (cullHint != null) {
            spatial.setCullHint(cullHint);
        }
        if (bucket != null) {
            spatial.setQueueBucket(bucket);
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
