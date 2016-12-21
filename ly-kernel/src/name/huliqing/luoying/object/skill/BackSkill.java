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
package name.huliqing.luoying.object.skill;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.layer.network.ActorNetwork;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.object.entity.TerrainEntity;
import name.huliqing.luoying.object.scene.Scene;

/**
 * 回城、瞬移技能
 * @author huliqing
 */
public class BackSkill extends AbstractSkill {
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    
    // 人物消失的时间插值点
    private float backPoint = 0.75f;
    
    // ---- 内部参数
    
    // 标记是否已经完成回传
    private boolean backed;
    
    @Override
    public void setData(SkillData data) {
        super.setData(data); 
        this.backPoint = data.getAsFloat("backPoint", backPoint);
    }

    @Override
    protected void doSkillUpdate(float tpf) {
        if (!backed && time >= trueUseTime * backPoint) {
            Vector3f loc = actor.getSpatial().getLocalTranslation();
            
            // 从场景中获取一个随机点
            getRandomTerrainPoint(actor.getScene(), loc);
            
            // 注意：因为涉及到随机传送，所以必须统一由服务端处理。use Network
            actorNetwork.setLocation(actor, loc);
            backed = true;
        }
    }
    
    private Vector3f getRandomTerrainPoint(Scene scene, Vector3f store) {
        List<TerrainEntity> tes = scene.getEntities(TerrainEntity.class, null);
        if (tes == null || tes.isEmpty()) {
            return store.set(0, 0, 0);
        }
        TerrainEntity te = tes.get(FastMath.nextRandomInt(0, tes.size() - 1));
        if (te.getSpatial() == null) {
            store.set(0, 0, 0);
            return store;
        }
        BoundingBox bb = (BoundingBox) te.getSpatial().getWorldBound();
        float xe = bb.getXExtent() * 0.5f; // x 0.5防止掉出边界
        float ze = bb.getZExtent() * 0.5f;
        
        store.set(FastMath.nextRandomFloat() * xe * 2 - xe, 0, FastMath.nextRandomFloat() * ze * 2 - ze);
        store.set(te.getHeight(store.x, store.z));
        return store;
    }

    @Override
    public void cleanup() {
        backed = false;
        
        // 同步位置,必须的，否则服务端与客户端位置可能错位
        Network.getInstance().syncTransformDirect(actor);
        
        super.cleanup();
    }
    
}
